(ns sicp.c04.ex4-6
  (:require [sicp.c04.base :refer :all])
  (:refer-clojure :exclude [eval apply]))

(defn let? [exp]
  (tagged-list? exp 'let))

(defn- named-let? [exp]
  (symbol? (second exp)))

(defn- let-name [exp]
  (second exp))

(defn let-args [exp]
  (if (named-let? exp)
    (nth exp 2)
    (second exp)))

(defn let-body [exp]
  (if (named-let? exp)
    (nth exp 3)
    (nth exp 2)))

(defn let-arg-vars [args]
  (map first args))

(defn let-arg-exps [args]
  (map second args))

(defn let->combination [exp]
  (let [args (let-args exp)
        body (let-body exp)
        vars (let-arg-vars args)
        exps (let-arg-exps args)
        lambda (mk-lambda vars body)]
    (if (named-let? exp)
      (let [name (let-name exp)]
        (sequence->exp
          (list (mk-definition name lambda)
                (cons name exps))))
      (cons lambda exps))))

(defn mk-let [args body]
  (list 'let args body))