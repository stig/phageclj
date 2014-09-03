(ns phage.styles
  (:require [garden.def :refer [defstyles]]
            [garden.units :refer [px px-]]))

(def pieces [[:.square1 :.square2
              {:width (px 45)
               :height (px 45)
               :margin-left :auto
               :margin-right :auto}]

             [:.circle1 :.circle2
              {:width (px 45)
               :height (px 45)
               :margin-left :auto
               :margin-right :auto
               :border-radius (px 50)}]

             [:.triangle1
              {:width 0
               :height 0
               :margin-left :auto
               :margin-right :auto
               :border-left [[(px 22.5) :solid :transparent]]
               :border-right [[(px 22.5) :solid :transparent]]
               :border-bottom [[(px 45) :solid :red]]
               }]

             [:.triangle2
              {:width 0
               :height 0
               :margin-left :auto
               :margin-right :auto
               :border-left [[(px 22.5) :solid :transparent]]
               :border-right [[(px 22.5) :solid :transparent]]
               :border-top [[(px 45) :solid :blue]]}]

             [:.diamond1
              {:background :red
               :width (px 33)
               :height (px 33)
               :margin-left :auto
               :margin-right :auto
               :-webkit-transform "rotate(45deg)"}]

             [:.diamond2
              {:background :blue
               :width (px 33)
               :height (px 33)
               :margin-left :auto
               :margin-right :auto
               :-webkit-transform "rotate(45deg)"}]

             [:.square1 :.circle1 {:background :red}]

             [:.square2 :.circle2 {:background :blue}]])


(def cell [:.cell
           {:width (px 50)
            :height (px 50)
            :display :table-cell
            :vertical-align :middle
            :text-align :center
            :border {:style :solid :width (px 1)}}])

(def line [:.line
           {:display :table-row}])

(def grid [:.grid
           {:display :table
            :border-spacing (px 2)}])

(defstyles screen
  [[grid]
   [line]
   [cell]
   [pieces]])
