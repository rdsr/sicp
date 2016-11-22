(ns sicp.c04.ex4-14
  (:require [sicp.c04.driver :as d]))

(d/driver "(define (map f s)
             (if (empty? s)
               '()
               (cons (f (car s))
                     (map f (cdr s)))))")

(d/driver "(map (lambda (x) (* x 1)) (list 1 2 3 4))")
