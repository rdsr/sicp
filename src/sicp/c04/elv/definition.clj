(ns sicp.c04.elv.definition
  (use [sicp.c04.elv.util])
  (require [sicp.c04.elv.lambda :refer [mk-lambda]]))

;; -- definition
(defn definition? [exp]
  (tagged-list? exp 'define))

(defn definition-variable [exp]
  (let [e (second exp)]
    (if (list? e)
      (first e)
      e)))

(defn definition-value [exp]
  (let [e (second exp)]
    (if (symbol? e)
      (nth exp 2)
      (mk-lambda (rest (second exp))
                 (nth exp 2)))))

(defn mk-definition [variable value]
  (list 'define variable value))

