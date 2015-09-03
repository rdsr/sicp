(ns sicp.c04.ex4-6
  (:require [sicp.c04.base :refer :all])
  (:refer-clojure :exclude [eval apply]))

(defn let? [exp]
  (tagged-list? exp 'let))

(defn let-args [exp]
  (second exp))

(defn let-body [exp]
  (nth exp 2))

(defn let-arg-vars [args]
  (map first args))

(defn let-arg-exps [args]
  (map second args))

(defn let->lambda [exp]
  (let [args (let-args exp)
        body (let-body exp)
        vars (let-arg-vars args)
        exps (let-arg-exps args)]
    (cons (mk-lambda vars body) exps)))
