(ns sicp.c04.elv.begin
  (:use [sicp.c04.elv.util])
  (:refer-clojure :exclude [eval apply true? false?]))

;; -- begin
(defn begin? [exp] (tagged-list? exp 'begin))
(defn begin-actions [exp] (rest exp))
(defn last-exp? [sq] (-> sq rest empty?))
(defn first-exp [sq] (first sq))
(defn rest-exps [sq] (rest sq))
(defn mk-begin [sq] (cons 'begin sq))

(defn sequence->exp [sq]
  (cond
    (nil? sq) sq
    (last-exp? sq) (first-exp sq)
    :else (mk-begin sq)))

(declare eval)

(defn eval-sequence [sq env]
  (cond
    (last-exp? sq) (eval (first-exp sq) env)
    :else (do (eval (first-exp sq) env)
              (eval-sequence (rest-exps sq) env))))