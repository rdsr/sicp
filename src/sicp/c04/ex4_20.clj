(ns sicp.c04.ex4-20
  (:require [sicp.c04.elv.let :as l]
            [sicp.c04.elv.begin :as b]))

;; Because internal definitions look sequential but are actually simultaneous,
;; some people prefer to avoid them entirely, and use the special form letrec
;; instead. Letrec looks like let, so it is not surprising that the variables
;; it binds are bound simultaneously and have the same scope as each other.
;; The sample procedure f above can be written without internal definitions,
;; but with exactly the same meaning, as

;; (define (f x)
;;   (letrec ((even?
;;             (lambda (n)
;; 	      (if (= n 0)
;; 		  true
;; 		  (odd? (- n 1)))))
;;            (odd?
;;             (lambda (n)
;; 	      (if (= n 0)
;; 		  false
;; 		  (even? (- n 1))))))
;;     <rest of body of f>))

;; Letrec expressions, which have the form
;; (letrec ((<var 1 > <exp 1 >) ... (<var n > <exp n >))
;;   <body>)
;; are a variation on let in which the expressions <exp k > that provide the
;; initial values for the variables <var k > are evaluated in an environment
;; that includes all the letrec bindings. This permits recursion in the bindings,
;; such as the mutual recursion of even? and odd? in the example above,
;; or the evaluation of 10 factorial with

;; (letrec ((fact
;; 	  (lambda (n)
;; 	    (fact 10))
;; 	  (if (= n 1) 1
;; 	      (* n (fact (- n 1)))))))

;; a. Implement letrec as a derived expression, by transforming a letrec expression
;; into a let expression as shown in the text above or in exercise 4.18. That is,
;; the letrec variables should be created with a let and then be assigned their
;; values with set!.

(defn letrec-bindings [exp]
  (second exp))
(defn letrec-body [exp]
  (nth exp 2))
(defn letrec-binding-vars [exp]
  (map first (letrec-bindings exp)))

(defn letrec->let [exp]
  (let [vars (letrec-binding-vars exp)
        sentinals (for [var vars] (list var '*unassigned*))
        set-values (for [binding (letrec-bindings exp)]
                     (cons 'set! binding))]
    (l/mk-let sentinals
              (b/sequence->exp
                ;; adding element (letrec-body exp)
                ;; at the end of the list.
                ;; todo: find a better way
                (concat set-values
                        [(letrec-body exp)])))))


;; b. Louis Reasoner is confused by all this fuss about internal definitions. The way he sees it, if you don't like to use
;; define inside a procedure, you can just use let. Illustrate what is loose about his reasoning by drawing an environment
;; diagram that shows the environment in which the <rest of body of f> is evaluated during evaluation of the expression (f 5),
;; with f defined as in this exercise. Draw an environment diagram for the same evaluation, but with let in place of letrec in
;; the definition of f.

;; When f is defined, using let:-
;;    lambdas even? and odd? capture the same env: e.
;; The let exp is transformed to lambda capturing an env which adds the names even? and odd?.
;; When we evaluate the body of f, a call to even?/odd? will evaluate these functions,
;; but their captured env. do not have those names (even/odd) defined.
;;
;; When f is defined, using letrec:
;; the function names even?/odd? are defined (to values '*unassigned*) and are captured in
;; the lambda, defined by let, hence when we evaluate the lambdas even?/odd? the function
;; names are already in their captured envs.
