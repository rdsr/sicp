(ns sicp.c04.elv.procedure
  (:require [sicp.c04.elv.util :as u])
  (:refer-clojure :exclude [eval apply true? false?]))

;; -- procedure
(defn mk-procedure [parameters body env]
  (list 'procedure parameters body env))

(defn procedure-parameters [p] (second p))
(defn procedure-body [p] (nth p 2))
(defn procedure-env [p] (nth p 3))

(defn compound-procedure? [p] (u/tagged-list? p 'procedure))
(defn primitive-procedure? [p] (u/tagged-list? p 'primitive))
(defn primitive-implementation [p] (second p))

(defn apply-primitive-procedure [p args]
  (clojure.core/apply (primitive-implementation p) args))

(defn primitive-procedures []
  [['car first]
   ['cdr second]
   ['+ +]
   ['- -]
   ['* *]
   ['cons cons]
   ['null? nil?]])

(defn primitive-procedure-names []
  (map first (primitive-procedures)))

(defn primitive-procedure-objects []
  (map (fn [o] (list 'primitive (second o))) (primitive-procedures)))