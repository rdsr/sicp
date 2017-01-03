(ns sicp.c04.ex4-30
  (:require [sicp.c04.lazyeval :as l]))

;; Exercise 4.30.  Cy D. Fect, a reformed C programmer, is worried that some side effects may never take place, because the lazy evaluator doesn't force the expressions in a sequence. Since the value of an expression in a sequence other than the last one is not used (the expression is there only for its effect, such as assigning to a variable or printing), there can be no subsequent use of this value (e.g., as an argument to a primitive procedure) that will cause it to be forced. Cy thus thinks that when evaluating sequences, we must force all expressions in the sequence except the final one. He proposes to modify eval-sequence from section 4.1.1 to use actual-value rather than eval:

;; (define (eval-sequence exps env)
;;   (cond ((last-exp? exps) (eval (first-exp exps) env))
;;         (else (actual-value (first-exp exps) env)
;;               (eval-sequence (rest-exps exps) env))))

;; a. Ben Bitdiddle thinks Cy is wrong. He shows Cy the for-each procedure described in exercise 2.23, which gives an important example of a sequence with side effects:

;; (define (for-each proc items)
;;   (if (null? items)
;;       'done
;;       (begin (proc (car items))
;;              (for-each proc (cdr items)))))

;; He claims that the evaluator in the text (with the original eval-sequence) handles this correctly:

;; ;;; L-Eval input:
;; (for-each (lambda (x) (newline) (display x))
;;           (list 57 321 88))
;; 57
;; 321
;; 88
;; ;;; L-Eval value:
;; done

;; --

;; Explain why Ben is right about the behavior of for-each.

;; for-each behaves correctly here. When the body of the procedure 'for-each' is evaluated, 'proc'
;; is forced since it is the operator and 'items' is forced since it is an arg. to a primitive procedure

;; ---

;; b. Cy agrees that Ben is right about the for-each example, but says that that's not the kind of program he was thinking about when he proposed his change to eval-sequence. He defines the following two procedures in the lazy evaluator:

;; (define (p1 x)
;;   (set! x (cons x '(2)))
;;   x)

;; (define (p2 x)
;;   (define (p e)
;;     e
;;     x)
;;   (p (set! x (cons x '(2)))))

(l/driver
  "(define (p1 x)
     (set! x (cons x '(2)))
     x)")

(l/driver
  "(define (p2 x)
     (define (p e)
       e
       x)
     (p (set! x (cons x '(2)))))"
  )

(l/driver "(p1 1)")
;; This correctly evals to '(1 2)

(l/driver "(p2 1)")
;; This evals to 1 since the assignment is never executed as eval-sequence does not force each of its exps
;; With Cy's proposed changed this exp is evaluated correctly as '(1 2)


;; c. Cy also points out that changing eval-sequence as he proposes does not affect the behavior of the example in part a. Explain why this is true.
;; It does not affect since in our case the body of the for-each only has a single exp, in which case original eval-sequence and Cy's eval-sequence are identical

;; d. It depends in a language where mutation is supported, I'd prefer Cy's method which has correct behavior, whereas in a pure lazy language where side effects are not allowed, I'd prefer the original approach






