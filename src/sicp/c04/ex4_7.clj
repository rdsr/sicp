(ns sicp.c04.ex4-7
  (:require [sicp.c04.elv :refer :all]
            [sicp.c04.ex4-6 :refer :all])
  (:refer-clojure :exclude [eval apply]))

(defn let*? [exp]
  (tagged-list? exp 'let*))

(defn let*-args [exp]
  (second exp))

(defn let*-body [exp]
  (nth exp 2))

(defn- expand-let*-clauses [args body]
  (cond
    (empty? args) body
    :else (let [fc (first args)
                rc (rest args)]
            (mk-let (list fc) (expand-let*-clauses rc body)))))

(defn let*->nested-let [exp]
  (expand-let*-clauses (let*-args exp) (let*-body exp)))

