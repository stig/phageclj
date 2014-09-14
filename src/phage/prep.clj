(ns phage.prep
  (:require [phage.core :refer :all]))

(defn prep
  "Prepares a match for sending to the frontend client."
  [state]
  (-> state
      (assoc-in [:game-over?] (game-over? state))
      (assoc-in [:draw?] (draw? state))
      (assoc-in [:winner] (winner state))
      (assoc-in [:legal-moves] (into #{} (moves state)))))
