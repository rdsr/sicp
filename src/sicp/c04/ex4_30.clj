(ns sicp.c04.ex4-30
  (:require [sicp.c04.lazyeval :as l]))

(l/driver
  "(define (p2 x)
     (define (p e)
       e
       x)
     (p (set! x (cons x '(2)))))"
  )

(l/driver "(p2 1)")
