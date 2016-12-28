(ns sicp.c04.ex4-28)

;; Exercise 4.28.  Eval uses actual-value rather than eval to evaluate the operator
;; before passing it to apply, in order to force the value of the operator.
;; Give an example that demonstrates the need for this forcing.

;; (define (f g) (g 1))
;; If eval is used to evaluate the operator, it will evaluate g to a thunk
;; which would be meaningless when applied to args -> 1




