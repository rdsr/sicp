(ns sicp.c04.ex4-7
  (:require [clojure.test :refer :all])
  (:refer-clojure :exclude [eval apply]))

(deftest testing-let*-exps
  (testing "let*->nested-let"
    (is (= (let*->nested-let
             '(let ((a 1)
                     (b (+ a 2)))
                (+ a b)))
           '(let ((a 1))
              (let ((b (+ a 2)))
                (+ a b)))))))
