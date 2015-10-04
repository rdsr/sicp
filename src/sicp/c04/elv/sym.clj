(ns sicp.c04.elv.sym
  (:refer-clojure :exclude [eval apply true? false?]))

(defn false? [x] (= x false))
(defn true? [x] (not (false? x)))

(defn self-evaluating? [exp]
  (cond
    (number? exp) true
    (string? exp) true
    :else false))

(defn variable? [exp]
  (symbol? exp))


