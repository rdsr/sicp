(ns sicp.c04.ex4-20-test
  (:require [clojure.test :refer :all])
  (:use [sicp.c04.ex4-20]))

(deftest testing-letrec
  (testing "letrec->let"
    (= (letrec->let
        '(letrec ((even?
                    (lambda (n)
                            (if (= n 0)
                              true
                              (odd? (- n 1)))))
                   (odd?
                     (lambda (n)
                             (if (= n 0)
                               false
                               (even? (- n 1))))))
                 (begin
                   (even? 2)
                   (odd? 1))))
      '(let ((even? *unassigned*)
              (odd? *unassigned*))
         (begin
           (set! even? (lambda (n)
                               (if (= n 0)
                                 true
                                 (odd? (- n 1)))))
           (set! odd? (lambda (n)
                              (if (= n 0)
                                false
                                (even? (- n 1)))))
           (begin (even? 2)
                  (odd? 1)))))))