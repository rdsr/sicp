(ns sicp.c04.elv.if
  (:require [sicp.c04.elv.util :refer [tagged-list?]]
            [sicp.c04.elv.sym :refer [true?]])
  (:refer-clojure :exclude [eval apply true? false?]))

;; -- if
(defn if? [exp]
  (tagged-list? exp 'if))
(defn if-predicate [exp]
  (second exp))
(defn if-consequent [exp]
  (nth exp 2))
(defn if-alternative [exp]
  (if (= (count exp) 4)
    (nth exp 3)
    'false))
(defn mk-if [pred consequent alt]
  (list 'if pred consequent alt))

(declare eval)

(defn eval-if [exp env]
  (if (true? (eval (if-predicate exp) env))
    (eval (if-consequent exp) env)
    (eval (if-alternative exp) env)))