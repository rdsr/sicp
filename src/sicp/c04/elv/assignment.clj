(ns sicp.c04.elv.assignment
  (use [sicp.c04.elv.util]))

;; -- assignment
(defn assignemt? [exp]
  (tagged-list? exp 'set!))
(defn assignemt-variable [exp]
  (second exp))
(defn assignemt-value [exp]
  (nth exp 2))

