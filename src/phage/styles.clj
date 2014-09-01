(ns phage.styles
  (:require [garden.def :refer [defstyles]]
            [garden.units :refer [px]]))

(def cell [:.cell
           {:width (px 50)
            :height (px 50)
            :float :left
            :position :relative
            :margin (px 2)
            :border {:style :solid :width (px 1)}}])

(def line [:.line
           {:clear :both}])

(defstyles screen
  [[line]
   [cell]])
