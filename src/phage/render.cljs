(ns phage.render
  (:require [chord.client :refer [ws-ch]]
            [cljs.core.async :refer [chan <! >! put! close! timeout]]
            [reagent.core :as reagent :refer [atom]])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(enable-console-print!)

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
  [idx mrk]
  ^{:key idx} [(keyword (str "div.moves-left-mark." mrk))])

(defn moves-left-count
  [cnt]
  (let [counters (concat
                  (repeat cnt "unnused")
                  (repeat (- 7 cnt) "used"))]
    (->> counters
         (map-indexed make-mark))))

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

(defn render-board
  [match]
  (let [{cells :grid, moves-left :moves-left} match]
    [:div.board
     [:div.left [moves-left-column [:D :T :S :C] moves-left]]
     [:div.middle [grid cells]]
     [:div.right [moves-left-column [:c :s :t :d] moves-left]]]))

(go
  (let [url "ws://localhost:8080/ws"
        container (.getElementById js/document "main")
        {:keys [ws-channel error]} (<! (ws-ch url))]
    (if error
      (prn "couldn't open websocket connection: " error)
      (go-loop []
        (when-let [{:keys [message]} (<! ws-channel)]
          (reagent/render-component [render-board message] container)
          (recur))))))
