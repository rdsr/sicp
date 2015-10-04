(ns sicp.c04.elv.lambda
  (use [sicp.c04.elv.util]))

;; -- lambda
(defn lambda? [exp] (tagged-list? exp 'lambda))
(defn lambda-parameters [exp] (second exp))
(defn lambda-body [exp] (nth exp 2))
(defn mk-lambda [args body] (list 'lambda args body))
