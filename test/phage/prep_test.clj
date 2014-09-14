(ns phage.prep-test
  (:require [phage.prep :refer :all]
            [clojure.test :refer :all]
            [phage.core :refer :all]))


(deftest basics
  (let [p (prep start)]
    (is (not (:game-over? p)))
    (is (= (-> p :grid vals) (-> start :grid vals)))
    (is (= (-> p :history) (:history start)))
    (is (= (-> p :moves-left) (:moves-left start)))
    (is (= (-> p :legal-moves count) 61))))

(deftest game-over
  (let [m (loop [s start]
            (if (game-over? s)
              s
              (recur (move s (rand-nth (moves s))))))
        p (prep m)]
    (is (true? (:game-over? p)))))
