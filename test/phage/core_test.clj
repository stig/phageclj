(ns phage.core-test
  (:require [clojure.test :refer :all]
            [phage.core :refer :all]))


(deftest basics
  (testing "starting cells"
    (is (= (-> start (:cells) (frequencies)) {nil 56
                                              :C 1 :S 1 :T 1 :D 1
                                              :c 1 :s 1 :t 1 :d 1}))
    (is (= (occupied? start 0) :d))
    (is (= (occupied? start (idx 7 7)) :D))
    (is (nil? (occupied? start (idx 0 1))))

    (is (= (moves-left? start :d) 7))
    (is (nil? (moves-left? start :x)))
    (is (nil? (moves-left? start :X)))
    (is (nil? (moves-left? start 0))))

  (testing "mobility"
    (is (= (player-turn? start 0) :d))
    (is (nil? (player-turn? start (idx 7 7))))

    (is (legal-move? start [0 1]))
    (is (nil? (legal-move? start [0 (idx  1 1)])))
    (is (-> start
            (assoc-in [:legal-moves :d] 0)
            (legal-move? [0 1])))    

    (is (legal-move? start [(idx 6 5) (idx 6 4)]))
    (is (nil? (legal-move? start [(idx 6 5) (idx 7 5)])))))


(deftest making-moves
  (testing "first move"
    (let [s1 (successor start [0 1])]
      (is (= (-> s1 (:cells) (frequencies) { nil 55 :x 1
                                            :C 1 :S 1 :T 1 :D 1
                                            :c 1 :s 1 :t 1 :d 1})))
      (is (= (occupied? s1 0) :x))
      (is (= (occupied? s1 1) :d))

      (is (not (legal-move? s1 [1 0]))))))


(deftest stringification
  (testing "starting position"
    (is (= (to-string start)
           (str "7 .......D\n"
                "6 .....T..\n"
                "5 ...S....\n"
                "4 .C......\n"
                "3 ......c.\n"
                "2 ....s...\n"
                "1 ..t.....\n"
                "0 d.......\n"
                "  01234567\n"
                "\n"
                "  C:7  c:7\n"
                "  S:7  s:7\n"
                "  T:7  t:7\n"
                "  D:7  d:7"))))

  (testing "after 1 move"
    (is (= (-> start
               (successor [0 2])
               (to-string))
           (str "7 .......D\n"
                "6 .....T..\n"
                "5 ...S....\n"
                "4 .C......\n"
                "3 ......c.\n"
                "2 ....s...\n"
                "1 ..t.....\n"
                "0 x.d.....\n"
                "  01234567\n"
                "\n"
                "  C:7  c:7\n"
                "  S:7  s:7\n"
                "  T:7  t:7\n"
                "  D:7  d:6"))))

(testing "after 2 moves"
    (is (= (-> start
               (successor [0 2])
               (successor [(idx 4 1) (idx 4 4)])
               (to-string))
           (str "7 .......D\n"
                "6 .....T..\n"
                "5 ...S....\n"
                "4 .X..C...\n"
                "3 ......c.\n"
                "2 ....s...\n"
                "1 ..t.....\n"
                "0 x.d.....\n"
                "  01234567\n"
                "\n"
                "  C:6  c:7\n"
                "  S:7  s:7\n"
                "  T:7  t:7\n"
                "  D:7  d:6")))))
