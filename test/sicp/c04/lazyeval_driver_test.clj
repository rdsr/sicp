(ns sicp.c04.lazyeval-driver-test
  (:require [clojure.test :refer :all])
  (:refer-clojure :exclude [eval apply true? false?])
  (:use [sicp.c04.lazyeval]))

(deftest testing-lazyeval-driver
  (testing "regression"
    (is (= (driver
             "((lambda (x)
                 (+ x 45))
               3)")
           48))
    (is (= (driver "(car (list 1 2))")
           1))
    (is (= (driver "(cdr (list 1 2))"))
        '(2))
    (is (= (driver
             "((lambda
                (x)
                ((lambda
                  (y) (+ y 1))
                  (+ x 1)))
               3)")
           5))
    (is (= (driver "(define z (lambda (x) (+ x 1)))
                    (z 1)")
           2))

    ;; todo test multiple assertions
    (is (= (driver "(define (append x y)
                      (if (empty? x)
                        y
                        (cons (car x) (append (cdr x) y))))
                    (append (list 1 2 3) (list 4 5 6))
                    (append '(a b c) '(d e f))")
           '(a b c d e f)))

    (is (= (driver "(define (x)
                      (define (f n)
                        (if (= n 1)
                          1
                          (* n
                             (f (- n 1)))))
                      (f 3))
                    (x)")
           6))
    )

  (testing "ycombinator"
    (is (= (driver
             "((lambda (n)
                 ((lambda (fact)
                     (fact fact n))
                  (lambda (ft k)
                    (if (= k 1)
                      1
                      (* k (ft ft (- k 1)))))))
              5)")
           6)))

  (testing "lazy evaluation"
    (is (= (driver "(define (try a b)
                      (if (= a 0) 1 b))
                    (try 0 (/ 1 0))")
           1)))
  )






