(ns sicp.c04.ex4-3
  (:require [sicp.c04.base :refer :all :exclude [application?]])
  (:refer-clojure :exclude [eval apply]))

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

(defn- get-fn [exp]
  (get m (first exp)))
(defn- has-fn? [exp]
  (contains? m (first exp)))

(defn eval [exp env]
  (cond
    (self-evaluating? exp) exp
    (variable? exp) (lookup-variable-value exp env)
    (has-fn? exp) ((get-fn exp) exp env)
    (application? exp) (apply (eval (operator exp) env)
                              (list-of-values (operands exp) env))))