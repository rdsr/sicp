(ns sicp.c04.elv.lambda
  (:use [sicp.c04.elv.util])
  (:refer-clojure :exclude [eval apply true? false?]))

;; -- (lambda (x) (+ x 1))
(defn lambda? [exp] (tagged-list? exp 'lambda))
(defn lambda-parameters [exp] (second exp))
(defn lambda-body [exp] (-> exp rest rest))
(defn mk-lambda [args body] (list 'lambda args body))
