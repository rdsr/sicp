(ns sicp.c04.elv.sym
  (:refer-clojure :exclude [eval apply true? false?]))

(defn true? [x] (= x true))
(defn false? [x] (= x false))

(defn self-evaluating? [exp]
  (or (number? exp)
      (string? exp)))

(defn variable? [exp]
  (or (symbol? exp)
      (= exp 'true)
      (= exp 'false)))
