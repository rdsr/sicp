(ns sicp.c04.elv.logical
  (:require [sicp.c04.elv.if :refer [mk-if]])
  (:require [sicp.c04.elv.util :refer [tagged-list?]])
  (:refer-clojure :exclude [eval apply true? false?]))

(defn and? [exp]
  (tagged-list? exp 'and))

(defn- and-clauses [exp]
  (rest exp))
(defn- expand-and-clauses [clauses]
  (cond (empty? clauses) true
        (empty? (rest clauses)) (first clauses)
        :else (let [fc (first clauses)
                    rc (rest clauses)]
                (mk-if fc (expand-and-clauses rc) false))))

(defn and->if [exp]
  (expand-and-clauses (and-clauses exp)))

;; ---
(defn or? [exp]
  (tagged-list? exp 'or))

(defn- or-clauses [exp]
  (rest exp))

(defn- expand-or-clauses [clauses]
  (cond
    (empty? clauses) false
    (empty? (rest clauses)) (first clauses)
    :else (let [fc (first clauses)
                rc (rest clauses)]
            (mk-if fc fc (expand-or-clauses rc)))))

(defn or->if [exp]
  (expand-or-clauses (or-clauses exp)))

