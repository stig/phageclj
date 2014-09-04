(ns phage.render
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(def ^:private *initial*
  {:moves-left {:S 7, :T 7, :C 7, :D 7, :s 7, :t 7, :c 7, :d 7},
   :cells [:d nil nil nil nil nil nil nil nil nil :t nil nil nil nil nil nil nil nil nil :s nil nil nil nil nil nil nil nil nil :c nil nil :C nil nil nil nil nil nil nil nil nil :S nil nil nil nil nil nil nil nil nil :T nil nil nil nil nil nil nil nil nil :D],
   :history []})

(def ^:private *match* (atom *initial*))

(defn piece?
  [piece]
  (if piece
    [(keyword (str "div." (name piece)))]
    ""))

(defn cell
  [idx piece]
  ^{:key idx} [:div.cell (piece? piece)])

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
  [idx piece]
  ^{:key idx} [:div.moves-left-row
               [:div.moves-left-cell (piece? piece)]])

(defn moves-left
  [pieces]
  [:div.moves-left
   (map-indexed moves-left-row pieces)])

(defn board []
  [:div.board
   [:div.left [moves-left [:D :T :S :C]]]
   [:div.middle [grid (:cells @*match*)]]
   [:div.right [moves-left [:c :s :t :d]]]]
)

(reagent/render-component [board] (.getElementById js/document "main"))
