(ns sicp.c04.elv
  (:refer-clojure :exclude [eval apply true? false?])
  (:require [sicp.c04.elv.application :refer :all]
            [sicp.c04.elv.assignment :refer :all]
            [sicp.c04.elv.begin :refer :all]
            [sicp.c04.elv.cond :refer :all]
            [sicp.c04.elv.definition :refer :all]
            [sicp.c04.elv.env :refer :all]
            [sicp.c04.elv.if :refer :all]
            [sicp.c04.elv.lambda :refer :all]
            [sicp.c04.elv.procedure :refer :all]
            [sicp.c04.elv.quote :refer :all]
            [sicp.c04.elv.sym :refer :all]
            [sicp.c04.elv.util :refer :all]))

(declare eval)

(defn eval-assignment [exp env]
  (set-variable-value!
    env
    (assignemt-variable exp)
    (eval (assignemt-value exp) env)))

(defn eval-definition [exp env]
  (define-variable!
    env
    (definition-variable exp)
    (eval (definition-value exp) env)))

(defn eval-if [exp env]
  (if (true? (eval (if-predicate exp) env))
    (eval (if-consequent exp) env)
    (eval (if-alternative exp) env)))

(defn eval-sequence [sq env]
  (cond
    (last-exp? sq) (eval (first-exp sq) env)
    :else (do (eval (first-exp sq) env)
              (eval-sequence (rest-exps sq) env))))

(defn eval-operands [exps env]
  (if (no-operands? exps)
    ()
    (cons (eval (first-operand exps) env)
          (eval-operands (rest-operands exps) env))))

(declare apply)
;; -- eval
(defn eval [exp env]
  (cond
    (self-evaluating? exp) exp
    (variable? exp) (lookup-variable-value env exp)
    (quoted? exp) (text-of-quotation exp)
    (assignemt? exp) (eval-assignment exp env)
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

(defn apply [procedure arguments]
  (cond
    (primitive-procedure? procedure)
    (apply-primitive-procedure procedure arguments)
    (compound-procedure? procedure)
    (eval-sequence
      (procedure-body procedure)
      (extend-env
        (procedure-parameters procedure)
        arguments
        (procedure-env procedure)))
    :else (Error. (str "Unknown procedure type -- apply " procedure))))
