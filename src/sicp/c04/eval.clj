(ns sicp.c04.eval
  (:refer-clojure :exclude [eval apply true? false?])
  (:require [sicp.c04.apply :refer [apply]]
            [sicp.c04.elv.application :refer [application? eval-operands operator operands]]
            [sicp.c04.elv.assignment :refer [assignment? eval-assignment]]
            [sicp.c04.elv.begin :refer [begin? begin-actions eval-sequence]]
            [sicp.c04.elv.cond :refer [cond? cond->if]]
            [sicp.c04.elv.definition :refer [definition? eval-definition]]
            [sicp.c04.elv.env :refer [lookup-variable-value]]
            [sicp.c04.elv.if :refer [if? eval-if]]
            [sicp.c04.elv.procedure :refer [mk-procedure]]
            [sicp.c04.elv.lambda :refer [lambda? lambda-parameters lambda-body]]
            [sicp.c04.elv.quote :refer [quoted? text-of-quotation]]
            [sicp.c04.elv.sym :refer [self-evaluating? variable?]]))

(defn eval [exp env]
  (cond
    (self-evaluating? exp) exp
    (variable? exp) (lookup-variable-value env exp)
    (quoted? exp) (text-of-quotation exp)
    (assignment? exp) (eval-assignment exp env)
    (definition? exp) (eval-definition exp env)
    (if? exp) (eval-if exp env)
    (lambda? exp) (mk-procedure (lambda-parameters exp)
                                (lambda-body exp)
                                env)
    (begin? exp) (eval-sequence (begin-actions exp) env)
    (cond? exp) (eval (cond->if exp) env)
    (application? exp) (apply (eval (operator exp) env)
                              (eval-operands (operands exp) env))
    :else (Error. (str "Unknown expression type -- eval" exp))))
