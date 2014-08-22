(ns phage.core-test
  (:require [clojure.test :refer :all]
            [phage.core :refer :all]))


(deftest basics
  (testing "starting cells"
    (is (= (-> start (:cells) (frequencies)) {nil 56
                                              :C 1 :S 1 :T 1 :D 1
                                              :c 1 :s 1 :t 1 :d 1}))
    (is (= (occupied? start 0 0) :d))
    (is (= (occupied? start 7 7) :D))
    (is (nil? (occupied? start 0 1)))

    (is (= (moves-left? start 0 0) 7))
    (is (nil? (moves-left? start 0 1))))

  (testing "mobility"
    (is (= (player-turn? start 0 0) :d))
    (is (nil? (player-turn? start 7 7)))))
