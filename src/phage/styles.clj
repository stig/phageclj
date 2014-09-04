(ns phage.styles
  (:require [garden.def :refer [defstyles]]
            [garden.units :refer [px px-]]))

(defn square
  [colour]
  {:width (px 45)
   :height (px 45)
   :margin-left :auto
   :margin-right :auto
   :background colour})

(defn circle
  [colour]
  {:width (px 45)
   :height (px 45)
   :margin-left :auto
   :margin-right :auto
   :border-radius (px 50)
   :background colour})

(defn diamond
  [colour]
  {:background colour
   :width (px 33)
   :height (px 33)
   :margin-left :auto
   :margin-right :auto
   :-webkit-transform "rotate(45deg)"}  )

(defn triangle
  [dir colour]
  {:width 0
   :height 0
   :margin-left :auto
   :margin-right :auto
   :border-left [[(px 22.5) :solid :transparent]]
   :border-right [[(px 22.5) :solid :transparent]]
   (if (= dir :up) :border-bottom :border-top) [[(px 45) :solid colour]]})

(def pieces [[:.s (square :red)]
             [:.c (circle :red)]
             [:.t (triangle :up :red)]
             [:.d (diamond :red)]

             [:.S (square :blue)]
             [:.C (circle :blue)]
             [:.T (triangle :down :blue)]
             [:.D (diamond :blue)]])

(def cell [:.cell
           {:width (px 50)
            :height (px 50)
            :display :table-cell
            :vertical-align :middle
            :border [[(px 1) :solid]]}])

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
