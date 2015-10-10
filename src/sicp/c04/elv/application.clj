(ns sicp.c04.elv.application
  (:refer-clojure :exclude [eval apply true? false?]))

;; -- application
(defn application? [exp] (list? exp))
(defn operator [exp] (first exp))
(defn operands [exp] (rest exp))
(defn no-operands? [ops] (empty? ops))
(defn first-operand [ops] (first ops))
(defn rest-operands [ops] (rest ops))

(defn eval-operands [eval-fn exps env]
  (if (no-operands? exps)
    ()
    (cons (eval-fn (first-operand exps) env)
          (eval-operands eval-fn (rest-operands exps) env))))