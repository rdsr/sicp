(ns sicp.c04.elv.application
  (:refer-clojure :exclude [eval apply true? false?]))


;; -- application
(defn application? [exp] (list? exp))
(defn operator [exp] (first exp))
(defn operands [exp] (rest exp))
(defn no-operands? [ops] (empty? ops))
(defn first-operand [ops] (first ops))
(defn rest-operands [ops] (rest ops))

(declare eval)

(defn eval-operands [exps env]
  (if (no-operands? exps)
    ()
    (cons (eval (first-operand exps) env)
          (eval-operands (rest-operands exps) env))))