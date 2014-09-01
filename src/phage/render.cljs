(ns phage.render
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(def ^:private *initial*
  {:moves-left {:S 7, :T 7, :C 7, :D 7, :s 7, :t 7, :c 7, :d 7},
   :cells [:d nil nil nil nil nil nil nil nil nil :t nil nil nil nil nil nil nil nil nil :s nil nil nil nil nil nil nil nil nil :c nil nil :C nil nil nil nil nil nil nil nil nil :S nil nil nil nil nil nil nil nil nil :T nil nil nil nil nil nil nil nil nil :D],
   :history []})

(def ^:private *match* (atom *initial*))

(defn cell [cell]
  [:div.cell
   (cond
    (keyword? cell) (name cell)
    :else ".")])

(defn line [cells]
  [:div.line
   (map cell cells)])

(defn grid [cells]
  [:div.grid
   (->> cells
        (partition 8)
        (reverse)
        (map line))])

(defn board []
  [:div.board
   [grid (:cells @*match*)]]
)

(reagent/render-component [board] (.getElementById js/document "main"))
