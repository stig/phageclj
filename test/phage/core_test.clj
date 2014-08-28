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
    (is (not (-> start
             (assoc-in [:moves-left :d] 0)
             (legal-move? [0 1]))))

    (is (legal-move? start [(idx 1 2) (idx 2 2)]))
    (is (nil? (legal-move? start [(idx 1 2) (idx 0 2)])))

    (is (= (count (moves start)) 61))
    (is (every? (partial legal-move? start) (moves start)))

    (is (= 38 (count (moves (-> start (assoc-in [:moves-left :c] 0))))))))

(deftest making-moves
  (testing "first move"
    (let [s1 (move start [0 1])]
      (is (= (-> s1 :cells frequencies) { nil 55 :x 1
                                         :C 1 :S 1 :T 1 :D 1
                                         :c 1 :s 1 :t 1 :d 1}))
      (is (= [[0 1]] (-> s1 :history)))
      (is (= 6 (-> s1 :moves-left :d)))
      (is (= (occupied? s1 0) :x))
      (is (= (occupied? s1 1) :d))
      (is (not (legal-move? s1 [1 0])))
      (is (not (legal-move? s1 [1 2]))))))

(deftest game-over
  (testing "nowhere to go"
    (let [cells (->> start (:cells) (replace {nil :x}))
          s1 (assoc start :cells cells)]
      (is (game-over? s1))
      (is (draw? s1))))
  (testing "no moves left"
    (let [s1 (-> start (assoc :moves-left {}))]
      (is (game-over? s1))
      (is (draw? s1))))
  (testing "not a draw?"
    (let [s1 (-> start (assoc :moves-left {:T 1}))]
      (is (game-over? s1))
      (is (not (draw? s1)))
      (is (= (winner s1) 2)))))

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
               (move [0 2])
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
               (move [0 2])
               (move [(idx 4 1) (idx 4 4)])
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
