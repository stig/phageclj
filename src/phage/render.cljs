(ns phage.render
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(def rand-state
{:lookup
 {:D [2 7],
  :T [4 6],
  :C [4 0],
  :s [0 2],
  :c [2 3],
  :d [0 5],
  :t [7 1],
  :S [2 2]},
 :grid
 {[7 6] nil,
  [7 1] :t,
  [4 3] :X,
  [2 2] :S,
  [0 0] :x,
  [7 7] :X,
  [1 0] nil,
  [2 3] :c,
  [2 5] :x,
  [7 2] :x,
  [6 7] nil,
  [7 4] :X,
  [0 6] :x,
  [3 3] :x,
  [5 4] :X,
  [1 1] :x,
  [6 3] nil,
  [0 5] :d,
  [3 4] :x,
  [7 3] :X,
  [4 2] :x,
  [3 0] nil,
  [6 6] nil,
  [5 3] :X,
  [4 7] nil,
  [6 5] :X,
  [4 1] :X,
  [5 2] nil,
  [4 6] :T,
  [1 4] nil,
  [5 7] nil,
  [1 3] :X,
  [1 5] :x,
  [1 7] :X,
  [6 4] nil,
  [0 3] nil,
  [5 1] :X,
  [6 1] nil,
  [5 6] nil,
  [0 7] :x,
  [5 5] :X,
  [2 7] :D,
  [2 4] :x,
  [3 6] :x,
  [4 5] :X,
  [7 0] :X,
  [0 2] :s,
  [2 0] nil,
  [0 4] :x,
  [3 1] :X,
  [2 1] nil,
  [1 6] nil,
  [4 4] :X,
  [3 7] :x,
  [7 5] nil,
  [2 6] :x,
  [5 0] nil,
  [6 2] nil,
  [6 0] nil,
  [1 2] :x,
  [3 5] :x,
  [3 2] :X,
  [0 1] nil,
  [4 0] :C},
 :history
 [[[0 0] [0 4]]
  [[5 3] [3 1]]
  [[1 2] [4 2]]
  [[4 1] [3 2]]
  [[0 4] [0 7]]
  [[7 7] [1 7]]
  [[3 6] [3 7]]
  [[3 2] [4 3]]
  [[3 7] [1 5]]
  [[6 5] [5 5]]
  [[1 5] [3 5]]
  [[5 5] [5 4]]
  [[3 5] [2 6]]
  [[4 3] [7 0]]
  [[0 7] [0 6]]
  [[1 7] [2 7]]
  [[2 4] [3 3]]
  [[7 0] [7 4]]
  [[3 3] [1 1]]
  [[7 4] [7 3]]
  [[4 2] [7 2]]
  [[5 4] [4 4]]
  [[1 1] [0 2]]
  [[3 1] [1 3]]
  [[7 2] [7 1]]
  [[7 3] [5 1]]
  [[0 6] [0 5]]
  [[4 4] [4 5]]
  [[2 6] [2 5]]
  [[1 3] [2 2]]
  [[2 5] [3 4]]
  [[5 1] [4 0]]
  [[3 4] [2 3]]
  [[4 5] [4 6]]],
 :moves-left {:D 5, :T 2, :C 0, :s 4, :c 0, :d 3, :t 4, :S 4}})

(def ^:private *match* (atom rand-state))

(defn piece-div
  [piece]
  [(keyword (str "div." (name piece)))])

(defn cell
  [idx piece]
  ^{:key idx} [:div.cell
               (if piece
                 (piece-div piece)
                 "")])

(defn line
  [idx cells]
  ^{:key idx} [:div.line
               (map-indexed cell cells)])

(defn grid [cells]
  [:div.grid
   (->> (for [x (reverse (range 8))
              y (range 8)]
          [x y])
        (map cells)
        (partition 8)
        (reverse)
        (map-indexed line))])

(defn make-mark
  [mrk]
  [(keyword (str "div.moves-left-mark." mrk))])

(defn moves-left-count
  [cnt]
  (let [counters (concat
                  (repeat cnt "unnused")
                  (repeat (- 7 cnt) "used"))]
    (->> counters (map make-mark))))

(defn moves-left-row
  [idx [piece count]]
  ^{:key idx} [:div.moves-left-row
               [:div.moves-left-piece (piece-div piece)]
               [:div.moves-left-count (moves-left-count count)]])

(defn moves-left-column
  [pieces moves-left]
  [:div.moves-left
   (->> pieces
        (map #(vector % (% moves-left)))
        (map-indexed moves-left-row))])

(defn board []
  (let [{cells :grid, moves-left :moves-left} @*match*]
    [:div.board
     [:div.left [moves-left-column [:D :T :S :C] moves-left]]
     [:div.middle [grid cells]]
     [:div.right [moves-left-column [:c :s :t :d] moves-left]]]))

(reagent/render-component [board] (.getElementById js/document "main"))
