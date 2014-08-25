(ns phage.core)

(def ^:private n-rows 8)
(def ^:private n-columns 8)

(def ^:private init-moves-left
  (->> [:S :T :C :D :s :t :c :d]
       (map (fn [x] [x 7]))
       (into {})))

(def ^:private diamond-vectors #{[1 0] [-1 0] [0 1] [0 -1]})
(def ^:private square-vectors #{[1 1] [1 -1] [-1 -1] [-1 1]})
(def ^:private circle-vectors (into square-vectors diamond-vectors))
(def ^:private piece-vectors {:d diamond-vectors
                              :D diamond-vectors
                              :s square-vectors
                              :S square-vectors
                              :c circle-vectors
                              :C circle-vectors
                              :t #{[0 -1] [0 1] [1 0]}
                              :T #{[0 -1] [0 1] [-1 0]}})

(defn- idx
  "Find index for row/column."
  ([[row column]] (idx row column))
  ([row column] (+ (* row n-columns) column)))


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
  ([state [x y]] (get (:cells state) (idx x y))))

(defn moves-left?
  "Return the number of moves left for a piece, or nil if not a piece."
  [state piece]
  ((:moves-left state) piece))
                                    
(defn player-turn?
  "Truthy if the piece specified belongs to current player."
  [state xy]
  (if-some [piece (occupied? state xy)]
    (if (= 0 (mod (count (:history state)) 2))
      (piece #{:c :d :s :t})
      (piece #{:C :D :S :T}))))
      
(defn straight-line?
  "Cheap check for ruling out illegal move destinations."
  [[[x0 y0] [x1 y1]]]
  (or
   (= x0 x1)
   (= y0 y1)
   (= (- x1 x0) (- y1 y0))))

(defn- v [x] 
  (if (< x 0) -1 (if (> x 0) 1 0)))

(defn move-vector
  "Finds vector from move."
  [[[x0 y0] [x1 y1]]] 
  (let [xd (- x1 x0) yd (- y1 y0)]
    [(v xd) (v yd)]))

(defn- new-pos
  [[x y] [xd yd]]
  [(+ x xd) (+ y yd)])

(defn- path-free?
  "Checks whether the path from one location to another is free."
  [state from to v]
  (let [pos1 (new-pos from v)]
    (cond
     (occupied? state pos1) false
     (= to pos1) true
     :else (recur state pos1 to v))))

(defn legal-move?
  "Determines whether a move is legal."
  [state [from to]]
  (when (straight-line? [from to])
    (when-some [piece (occupied? state from)]
      (when (moves-left? state piece)
        (when-some [v ((piece-vectors piece) (move-vector [from to]))]
          (path-free? state from to v))))))

(defn successor
  "Perform a move. Returns the new state, or nil on error."
  [state [from to]]
  (when (legal-move? state [from to])
    (let [piece (occupied? state from)]
      (-> state
          (update-in [:moves-left piece] dec)
          (assoc-in [:cells (idx from)] :x)
          (assoc-in [:cells (idx to)] piece)))))

(defn to-string
  "Return a string representation of the board."
  [state]
  (let [rows (->> state
                  (:cells)
                  (map (fn [x] (if (keyword? x) (name x) ".")))
                  (partition n-rows)
                  (map #(apply str %))
                  (map-indexed (fn [i s] (str i " " s)))
                  (reverse)
                  (vec))
        
        cols ["  01234567\n"]

        meta (-> [] 
                 (conj (str "  C:" (moves-left? state :C) "  c:" (moves-left? state :c)))
                 (conj (str "  S:" (moves-left? state :S) "  s:" (moves-left? state :s)))
                 (conj (str "  T:" (moves-left? state :T) "  t:" (moves-left? state :t)))
                 (conj (str "  D:" (moves-left? state :D) "  d:" (moves-left? state :d))))]
    
    (clojure.string/join "\n" (concat rows cols meta))))

