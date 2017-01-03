(ns sicp.c04.lazyeval-driver-test
  (:refer-clojure :exclude [eval apply true? false?])
  (:use [sicp.c04.lazyeval]))

(driver
  "((lambda (x)
      (+ x 45))
    3)")

(driver "(car (list 1 2))")
(driver "(cdr (list 1 2))")

(driver
  "((lambda
     (x)
     ((lambda
       (y) (+ y 1))
       (+ x 1)))
    3)")

(driver "(define z (lambda (x) (+ x 1)))
                    (z 1)")

(driver "(define (append x y)
                      (if (empty? x)
                        y
                        (cons (car x) (append (cdr x) y))))")
(driver "(append (list 1 2 3) (list 4 5 6))")
(driver "(append '(a b c) '(d e f))")

(driver "(define (x)
                      (define (f n)
                        (if (= n 1)
                          1
                          (* n
                             (f (- n 1)))))
                      (f 3))
                    (x)")

;; ycombinator
(driver
  "((lambda (n)
      ((lambda (fact)
          (fact fact n))
       (lambda (ft k)
         (if (= k 1)
           1
           (* k (ft ft (- k 1)))))))
   5)")

;; lazy eval
(driver "(define (try a b)
                      (if (= a 0) 1 b))
                    (try 0 (/ 1 0))")





