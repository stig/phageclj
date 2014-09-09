(ns phage.styles
  (:require [garden.color :as colour :refer [rgb]]
            [garden.def :refer [defstyles]]
            [garden.units :refer [px px- px-div percent]]))

(def red (rgb 255 0 0))
(def blue (rgb 0 0 255))

(def cell-size (px 50))
(def piece-size (px 45))
(def half-piece-size (px-div piece-size 2))
(def diamond-size (px 33))

(defn square
  [colour]
  {:width piece-size
   :height piece-size
   :margin-left :auto
   :margin-right :auto
   :background colour})

(defn circle
  [colour]
  {:width piece-size
   :height piece-size
   :margin-left :auto
   :margin-right :auto
   :border-radius piece-size
   :background colour})

(defn diamond
  [colour]
  {:background colour
   :width diamond-size
   :height diamond-size
   :margin-left :auto
   :margin-right :auto
   :-webkit-transform "rotate(45deg)"}  )

(defn triangle
  [dir colour]
  {:width 0
   :height 0
   :margin-left :auto
   :margin-right :auto
   :border-left [[half-piece-size :solid :transparent]]
   :border-right [[half-piece-size :solid :transparent]]
   (if (= dir :up) :border-bottom :border-top) [[piece-size :solid colour]]})

(defn blocked
  [c]
  {:height (percent 100)
   :width (percent 100)
   :background (colour/lighten c 30)})

(def pieces
  [[:.s (square red)]
   [:.c (circle red)]
   [:.t (triangle :up red)]
   [:.d (diamond red)]
   [:.x (blocked red)]

   [:.S (square blue)]
   [:.C (circle blue)]
   [:.T (triangle :down blue)]
   [:.D (diamond blue)]
   [:.X (blocked blue)]])

(def moves-left
  {:display :table
   :border-spacing (px 2)})

(def moves-left-row
  {:display :table-row})

(def moves-left-piece
  {:width cell-size
   :height cell-size
   :display :table-cell
   :vertical-align :middle
   :border [[(px 1) :solid :transparent]]})

(def moves-left-count
  {:display :table-cell
   :border [[(px 1) :solid :transparent]]
   :vertical-align :middle})

(def moves-left-mark
  {:height piece-size
   :width (px 5)
   :display :table-cell})

(def used {:background :gray })

(def unnused {:background :white})

(def cell
  {:width cell-size
   :height cell-size
   :display :table-cell
   :vertical-align :middle
   :border [[(px 1) :solid]]})

(def line
  {:display :table-row})

(def grid
  {:display :table
   :border-spacing (px 2)})

(def board
  {:display :table})

(defn column
  [valign]
  {:display :table-cell
   :vertical-align valign})

(defstyles screen
  [[:.board board]
   [:.grid grid]
   [:.line line]
   [:.cell cell]
   [:.left (column :top)]
   [:.middle (column :middle)]
   [:.right (column :bottom)]
   [:.moves-left moves-left]
   [:.moves-left-row moves-left-row]
   [:.moves-left-piece moves-left-piece]
   [:.moves-left-count moves-left-count]
   [:.moves-left-mark moves-left-mark]
   [:.used used]
   [:.unnused unnused]
   [pieces]])
