(ns phage.core-test
  (:require [clojure.test :refer :all]
            [phage.core :refer :all]))


(deftest basics
  (testing "starting cells"
    (is (= (-> start (:cells) (frequencies)) {nil 56
                                              :C 1 :S 1 :T 1 :D 1
                                              :c 1 :s 1 :t 1 :d 1}))
    (is (= (occupied? start [0 0]) :d))
    (is (= (occupied? start [7 7]) :D))
    (is (nil? (occupied? start [0 1])))

    (is (= (moves-left? start [0 0]) 7))
    (is (nil? (moves-left? start [0 1]))))

  (testing "mobility"
    (is (= (player-turn? start [0 0]) :d))
    (is (nil? (player-turn? start [7 7])))

    (is (legal-move? start [[0 0] [0 1]]))
    (is (nil? (legal-move? start [[0 0] [1 1]])))
    
    (is (legal-move? start [[6 5] [6 4]]))
    (is (nil? (legal-move? start [[6 5] [7 5]])))))

;(deftest making-moves
;  (testing "first move"
;    (let [s1 (successor start [[0 0] [0 1]])]
;      (is (= (-> s1 (:cells) (frequencies) { nil 55 :x 1
;                                            :C 1 :S 1 :T 1 :D 1
;                                            :c 1 :s 1 :t 1 :d 1})))
;      (is (= (occupied? s1 [0 0]) :x))
;      (is (= (occupied? s1 [0 1]) :d)))))
