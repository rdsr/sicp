(ns sicp.c04.elv.assignment
  (:require [sicp.c04.elv.util :as u]
            [sicp.c04.elv.env :as e])
  (:refer-clojure :exclude [eval apply true? false?]))


;; -- assignment
(defn assignment? [exp]
  (u/tagged-list? exp 'set!))
(defn assignemt-variable [exp]
  (second exp))
(defn assignemt-value [exp]
  (nth exp 2))

(declare eval)

(defn eval-assignment [exp env]
  (e/set-variable-value!
    env
    (assignemt-variable exp)
    (eval (assignemt-value exp) env)))