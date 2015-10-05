(ns sicp.c04.ex4-3
  (:require [sicp.c04.apply :refer [apply]]
            [sicp.c04.elv.application :refer [eval-operands]]
            [sicp.c04.elv.assignment :refer [assignment? eval-assignment]]
            [sicp.c04.elv.begin :refer [begin? begin-actions eval-sequence]]
            [sicp.c04.elv.cond :refer [cond? cond->if]]
            [sicp.c04.elv.definition :refer [definition? eval-definition]]
            [sicp.c04.elv.env :refer [lookup-variable-value]]
            [sicp.c04.elv.if :refer [if? eval-if]]
            [sicp.c04.elv.procedure :refer [mk-procedure]]
            [sicp.c04.elv.lambda :refer [lambda? lambda-parameters lambda-body]]
            [sicp.c04.elv.quote :refer [quoted? text-of-quotation]]
            [sicp.c04.elv.sym :refer [self-evaluating? variable?]]
            [sicp.c04.elv.util :as u])
  (:refer-clojure :exclude [eval apply true? false?]))


(defn application? [exp] (u/tagged-list? exp 'call))
(defn operator [exp] (second exp))
(defn operands [exp] (-> exp rest rest))

(declare eval)

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

(defn eval [exp env]
  (cond
    (self-evaluating? exp) exp
    (variable? exp) (lookup-variable-value exp env)
    (has-fn? exp) ((get-fn exp) exp env)
    (application? exp) (apply (eval (operator exp) env)
                              (eval-operands (operands exp) env))))
