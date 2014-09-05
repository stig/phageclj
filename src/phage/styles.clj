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

(def pieces
  [[:.s (square :red)]
   [:.c (circle :red)]
   [:.t (triangle :up :red)]
   [:.d (diamond :red)]

   [:.S (square :blue)]
   [:.C (circle :blue)]
   [:.T (triangle :down :blue)]
   [:.D (diamond :blue)]])

(def moves-left
  {:display :table
   :border-spacing (px 2)})

(def moves-left-row
  {:display :table-row})

(def moves-left-piece
  {:width (px 50)
   :height (px 50)
   :display :table-cell
   :vertical-align :middle
   :border [[(px 1) :solid :transparent]]})

(def moves-left-count
  {:display :table-cell
   :border [[(px 1) :solid :transparent]]
   :vertical-align :middle})

(def moves-left-mark
  {:height (px 45)
   :width (px 5)
   :display :table-cell})

(def used {:background :gray })

(def unnused {:background :white})

(def cell
  {:width (px 50)
   :height (px 50)
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
