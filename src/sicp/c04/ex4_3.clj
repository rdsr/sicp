(ns sicp.c04.ex4-3
  (:require [sicp.c04.elv.application :refer [no-operands? first-operand rest-operands]]
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
            [sicp.c04.elv.util :refer :all])
  (:refer-clojure :exclude [eval apply true? false?]))


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


(defn application? [exp] (tagged-list? exp 'call))
(defn operator [exp] (second exp))
(defn operands [exp] (-> exp rest rest))

(def ops
  {'quote  text-of-quotation
   'set!   eval-assignment
   'define eval-definition
   'if     eval-if

   'begin (fn [exp env]
            (eval-sequence (begin-actions exp) env))


   'cond   (fn [exp env]
             (eval (cond->if exp) env))

   'lambda (fn [exp env]
             (mk-procedure
               (lambda-parameters exp)
               (lambda-body exp)
               env))
   })

(defn- get-fn [exp] (get ops (first exp)))
(defn- has-fn? [exp] (contains? ops (first exp)))

(declare apply)

(defn eval [exp env]
  (cond
    (self-evaluating? exp) exp
    (variable? exp) (lookup-variable-value exp env)
    (has-fn? exp) ((get-fn exp) exp env)
    (application? exp) (apply (eval (operator exp) env)
                              (eval-operands (operands exp) env))))

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