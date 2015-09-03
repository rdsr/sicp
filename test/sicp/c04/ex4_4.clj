(ns sicp.c04.ex4-4
  (:require [clojure.test :refer :all])
  (:refer-clojure :exclude [eval apply]))

(deftest testing-exps
  (testing "and->if"
    (is (= (and->if '(and 1 0 (+ 2 3)))
           '(if 1 (if 0 (+ 2 3) false) false)))
    (is (or->if '(or 1 0 (+ 2 3)))
        '(if 1 1 (if 0 0 (+ 2 3))))))

