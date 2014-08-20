(ns phage.core)

(def ^:private n-rows 8)
(def ^:private n-columns 8)

(defn idx
  "Find index for row/column."
  [row column]
  (+ (* row n-columns) column))

(def ^:private init-moves-left
  (->> [:S :T :C :D :s :t :c :d]
       (map (fn [x] [x 7]))
       (into {})))

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
  (-> {}
      (assoc :moves-left init-moves-left)
      (assoc :cells init-cells)))


                
                       


