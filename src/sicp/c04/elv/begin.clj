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

;; Passing eval as the first parameter
;; This required because of the recursive
;; nature of eval-sequence and eval,
;; eval in eval.clj calls eval-sequence in begin.clj
;; which calls eval.clj.
;; This can probably be refactored.
(defn eval-sequence [eval-fn sq env]
  (cond
    (last-exp? sq) (eval-fn (first-exp sq) env)
    :else (do (eval-fn (first-exp sq) env)
              (recur eval-fn (rest-exps sq) env))))