(ns phage.core)

(def ^:private n-rows 8)
(def ^:private n-columns 8)

(def ^:private init-moves-left
  (->> [:S :T :C :D :s :t :c :d]
       (map (fn [x] [x 7]))
       (into {})))

(def ^:private init-lookup
  {:D [7 7]
   :T [6 5]
   :S [5 3]
   :C [4 1]
   :c [3 6]
   :s [2 4]
   :t [1 2]
   :d [0 0]})

(def ^:private diamond-vectors #{[1 0] [0 1] [0 -1] [-1 0]})
(def ^:private square-vectors #{[1 1] [1 -1] [-1 1] [-1 -1]})
(def ^:private circle-vectors (into square-vectors diamond-vectors))
(def ^:private piece-vectors {:d diamond-vectors
                              :D diamond-vectors
                              :s square-vectors
                              :S square-vectors
                              :c circle-vectors
                              :C circle-vectors
                              :t #{[1 0] [0 -1] [0 1]}
                              :T #{[-1 0] [0 1] [0 -1]}})


(def player-1-pieces #{:c :d :s :t})
(def player-2-pieces #{:C :D :S :T})

(def locations
  (for [x (range 0 n-rows)
        y (range 0 n-columns)]
    [x y]))

(def ^:private init-grid
  (-> locations
      (zipmap (repeat nil))
      (merge (clojure.set/map-invert init-lookup))))

(def start
  "The starting state."
  {:moves-left init-moves-left
   :grid init-grid
   :lookup init-lookup
   :history []})

(defn occupied?
  "If the current position is occupied, return the piece."
  [state coord]
  (-> state :grid (get coord)))

(defn moves-left?
  "Return the number of moves left for a piece, or nil if not a piece."
  [state piece]
  (-> state :moves-left piece))

(defn player-pieces
  [state]
  (if (= 0 (-> state :history count (rem 2)))
    player-1-pieces
    player-2-pieces))

(defn player-turn?
  "Truthy if the piece at this location belongs to current player."
  [state xy]
  (when-some [piece (occupied? state xy)]
    (piece (player-pieces state))))

(defn- add
  "Add a move vector to a coordinate"
  [[x y] [dx dy]]
  [(+ x dx) (+ y dy)])

(defn moves
  "Return all legal moves from this state."
  [state]
  (for [p (player-pieces state)
        v (piece-vectors p)
        :when (< 0 (moves-left? state p))
        :let [f ((:lookup state) p)]
        t (rest (iterate (partial add v) f))
        :while (and (contains? (:grid state) t) (not (occupied? state t)))]
    [f t]))

(defn legal-move?
  "Determines whether a move is legal."
  [state move]
  (some #{move} (moves state)))

(defn move
  "Perform a move. Returns the new state, or nil on error."
  [state [from to]]
  (when (legal-move? state [from to])
    (let [piece (occupied? state from)]
      (-> state
          (assoc-in [:lookup piece] to)
          (update-in [:history] #(conj % [from to]))
          (update-in [:moves-left piece] dec)
          (assoc-in [:grid from] (if (piece player-1-pieces) :x :X))
          (assoc-in [:grid to] piece)))))

(defn game-over?
  "Returns true if game is over."
  [state]
  (empty? (moves state)))

(defn draw?
  "Returns true if the game is a draw."
  [state]
  (-> state
      (update-in [:history] (fn [x] (conj x :dummy)))
      game-over?))

(defn winner
  "Returns the winning player."
  [state]
  (- 2 (-> state :history count (rem 2))))

(defn- moves-left
  [s p] (str "  " (name p) ":" (moves-left? s p)))

(defn to-string
  "Return a string representation of the board."
  [state]
  (let [grid (:grid state)
        rows (->> locations
                  (map #(get grid %))
                  (map (fn [x] (if (keyword? x) (name x) ".")))
                  (partition n-rows)
                  (map #(apply str %))
                  (map-indexed (fn [i s] (str i " " s)))
                  (reverse))

        cols ["  01234567\n"]

        meta (-> []
                 (conj (str (moves-left state :C) (moves-left state :c)))
                 (conj (str (moves-left state :S) (moves-left state :s)))
                 (conj (str (moves-left state :T) (moves-left state :t)))
                 (conj (str (moves-left state :D) (moves-left state :d))))]

    (clojure.string/join "\n" (concat rows cols meta))))
