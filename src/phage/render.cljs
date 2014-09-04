(ns phage.render
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(def ^:private *initial*
  {:moves-left {:S 5, :T 6, :C 4, :D 7, :s 2, :t 1, :c 3, :d 0},
   :cells [:d nil nil nil nil nil nil nil nil nil :t nil nil nil nil nil nil nil nil nil :s nil nil nil nil nil nil nil nil nil :c nil nil :C nil nil nil nil nil nil nil nil nil :S nil nil nil nil nil nil nil nil nil :T nil nil nil nil nil nil nil nil nil :D],
   :history []})

(def ^:private *match* (atom *initial*))

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
   (->> cells
        (partition 8)
        (reverse)
        (map-indexed line))])

(defn moves-left-row
  [idx [piece count]]
  ^{:key idx} [:div.moves-left-row
               [:div.moves-left-piece (piece-div piece)]
               [:div.moves-left-count count]])

(defn moves-left-column
  [pieces moves-left]
  [:div.moves-left
   (->> pieces
        (map #(vector % (% moves-left)))
        (map-indexed moves-left-row))])

(defn board []
  (let [{cells :cells, moves-left :moves-left} @*match*]
    [:div.board
     [:div.left [moves-left-column [:D :T :S :C] moves-left]]
     [:div.middle [grid cells]]
     [:div.right [moves-left-column [:c :s :t :d] moves-left]]]))

(reagent/render-component [board] (.getElementById js/document "main"))
