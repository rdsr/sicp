(ns sicp.c04.elv.if
  (use [sicp.c04.elv.util]))

;; -- if
(defn if? [exp]
  (tagged-list? exp 'if))
(defn if-predicate [exp]
  (second exp))
(defn if-consequent [exp]
  (nth exp 2))
(defn if-alternative [exp]
  (if (= (count exp) 4)
    (nth exp 3)
    'false))
(defn mk-if [pred consequent alt]
  (list 'if pred consequent alt))

