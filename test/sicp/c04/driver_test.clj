(ns sicp.c04.driver-test
  (:use [sicp.c04.driver]))

(driver
  "((lambda
     (x)
     (+ x 45))
    3)")

(driver "(car (list 1 2))")
(driver "(cdr (list 1 2))")
(driver "(lambda
          (x)
          ((lambda
             (y) (+ y 1))
            (+ x 1)))")
(driver "((lambda
              (x)
              ((lambda
                 (y) (+ y 1))
                (+ x 1)))
             3)")
(driver "(define z (lambda (x) (+ x 1))) (z 1)")
(driver "(define (append x y)
            (if (empty? x)
              y
              (cons (car x) (append (cdr x) y))))
         ")

(driver "(append (list 1 2 3) (list 4 5 6))")
(driver "(append '(a b c) '(d e f))")
