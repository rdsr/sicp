(ns sicp.c04.ex4-1
  (:require [sicp.c04.eval :refer :all])
  (:refer-clojure :exclude [eval apply true? false?]))


;(defn list-of-values [exps env]
;  (if (no-operands? exps)
;    '()
;    (let [f (eval (first-operand exps) env)]
;      (cons f (list-of-values
;                (rest-operands exps)
;                env)
;            ))))
