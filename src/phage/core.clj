(ns phage.core)

(def ^:private n-rows 8)
(def ^:private n-columns 8)

(def ^:private init-moves-left
  (->> [:S :T :C :D :s :t :c :d]
       (map (fn [x] [x 7]))
       (into {})))

(def ^:private diamond-vectors #{8 1 -1 -8})
(def ^:private square-vectors #{9 7 -7 -9})
(def ^:private circle-vectors (into square-vectors diamond-vectors))
(def ^:private piece-vectors {:d diamond-vectors
                              :D diamond-vectors
                              :s square-vectors
                              :S square-vectors
                              :c circle-vectors
                              :C circle-vectors
                              :t #{-1 1 8}
                              :T #{-1 1 -8}})

(defn idx
  "Find index for row/column."
  [row column] (+ (* row n-columns) column))

(defn coord
  "Find row/column from index."
  [idx]
  [(quot idx n-columns) (rem idx n-columns)])

(def ^:private init-cells 
  (-> (repeat (* n-rows n-columns) nil)
      (vec)
      (assoc (idx 7 7) :D)
      (assoc (idx 6 5) :T)
      (assoc (idx 5 3) :S)
      (assoc (idx 4 1) :C)
      (assoc (idx 3 6) :c)
      (assoc (idx 2 4) :s)
      (assoc (idx 1 2) :t)      
      (assoc (idx 0 0) :d)))

(def start
  "The starting state." 
  {:moves-left init-moves-left
   :cells init-cells
   :history []})

(defn occupied?
  "If the current position is occupied, return the piece."
  ([state x] (get (:cells state) x)))

(defn moves-left?
  "Return the number of moves left for a piece, or nil if not a piece."
  [state piece]
  ((:moves-left state) piece))
                            
(def player-1-pieces #{:c :d :s :t})
(def player-2-pieces #{:C :D :S :T})

(defn player-pieces
  [state]
  (if (= 0 (rem (count (:history state)) 2))
    player-1-pieces
    player-2-pieces))

(defn player-turn?
  "Truthy if the piece specified belongs to current player."
  [state xy]
  (when-some [piece (occupied? state xy)]
    (piece (player-pieces state))))
      
(defn- v [x] 
  (if (< x 0) -1 (if (> x 0) 1 0)))

(defn move-vector
  "Finds vector from move."
  [from to]
  (let [[x0 y0] (coord from)
        [x1 y1] (coord to) 
        xd (- x1 x0) yd (- y1 y0)]
    (idx (v xd) (v yd))))

(defn- clear-path?
  "Checks whether the path from one location to another is free."
  [state from to v]
  (let [pos1 (+ from v)]
    (when (contains? (:cells state) pos1)
      (cond
       (occupied? state pos1) false
       (= to pos1) true
       :else (recur state pos1 to v)))))

(defn legal-move?
  "Determines whether a move is legal."
  [state [from to]]
  (when-some [piece (occupied? state from)]
    (when (moves-left? state piece)
      (when-some [v ((piece-vectors piece) (move-vector from to))]
        (clear-path? state from to v)))))

(defn move
  "Perform a move. Returns the new state, or nil on error."
  [state [from to]]
  (when (legal-move? state [from to])
    (let [piece (occupied? state from)]
      (-> state
          (update-in [:moves-left piece] dec)
          (assoc-in [:cells from] (if (piece player-1-pieces) :x :X))
          (assoc-in [:cells to] piece)))))

(defn game-over?
  "Returns true if game is over."
  [state]
  (let [moves (for [p (player-pieces state)
                    v (piece-vectors p)
                    :let [f (.indexOf (:cells start) p) 
                          t (+ f v)]]
                [f t])]
    (not-any? #(legal-move? state %) moves)))

(defn draw?
  "Returns true if the game is a draw."
  [state]
  (-> state
      (update-in [:history] (fn [x] (conj x [0 0])))
      (game-over?)))

(defn winner
  "Returns the winning player."
  [state]
  (- 2 (-> state (:history) (count) (rem 2))))

(defn- moves-left
  [s p] (str "  " (name p) ":" (moves-left? s p)))

(defn to-string
  "Return a string representation of the board."
  [state]
  (let [rows (->> state
                  (:cells)
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

