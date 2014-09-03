(ns phage.render
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(def ^:private *initial*
  {:moves-left {:S 7, :T 7, :C 7, :D 7, :s 7, :t 7, :c 7, :d 7},
   :cells [:d nil nil nil nil nil nil nil nil nil :t nil nil nil nil nil nil nil nil nil :s nil nil nil nil nil nil nil nil nil :c nil nil :C nil nil nil nil nil nil nil nil nil :S nil nil nil nil nil nil nil nil nil :T nil nil nil nil nil nil nil nil nil :D],
   :history []})

(def piece-map {:C 'circle2
                :D 'diamond2
                :S 'square2
                :T 'triangle2
                :X 'blocked2

                :c 'circle1
                :d 'diamond1
                :s 'square1
                :t 'triangle1
                :x 'blocked1

                nil 'empty})


(def ^:private *match* (atom *initial*))

(defn cell [piece]
  [:div.cell
   (if piece
     [(keyword (str "div." (piece-map piece)))]
     "")])

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
