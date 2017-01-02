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

(defn eval-assignment [eval-fn exp env]
  (e/set-variable-value!
    (assignemt-variable exp)
    (eval-fn (assignemt-value exp) env)
    env))