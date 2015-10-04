(ns sicp.c04.elv.begin
  (use [sicp.c04.elv.util]))

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
