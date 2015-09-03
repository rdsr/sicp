(ns sicp.c04.ex4-6
  (:require [clojure.test :refer :all])
  (:refer-clojure :exclude [eval apply]))

(deftest testing-exps
  (testing "let->lambda"
    (is (= (let->lambda
             '(let ((a 1)
                     (b 2))
                (+ a b)))
           '((lambda (a b) (+ a b)) 1 2)))))

