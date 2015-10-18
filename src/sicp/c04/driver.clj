(ns sicp.c04.driver
  (:refer-clojure :exclude [eval apply true? false?])
  (:require [sicp.c04.elv.procedure :as p]
            [sicp.c04.eval :as elv]
            [sicp.c04.elv.env :as env])
  (:import (java.io Reader StringReader PushbackReader)))

(defn display [s]
  (newline) (println s) (newline))

(defn user-print [object]
  (if (p/compound-procedure? object)
    (display [:compound-procedure
              :args (p/procedure-parameters object)
              :body (p/procedure-body object)
              '<procedure-env>])
    (display object)))

(defn driver [s]
  (with-open [pbr (-> s StringReader. PushbackReader.)]
    (binding [*read-eval* false]
      (loop [e (read pbr false nil)]
        (when-not (nil? e)
          (user-print (elv/eval e env/global-env))
          (recur (read pbr false nil)))))))

(driver
  "((lambda
     (x)
     (+ x 45))
    3)")

(driver "(car (list 1 2))")
(driver "(cdr (list 1 2))")
(driver "(lambda
          (x)
          ((lambda
             (y) (+ y 1))
            (+ x 1)))")
(driver "((lambda
              (x)
              ((lambda
                 (y) (+ y 1))
                (+ x 1)))
             3)")
(driver "(define z (lambda (x) (+ x 1))) (z 1)")
(driver "(define (append x y)
            (if (empty? x)
              y
              (cons (car x) (append (cdr x) y))))
         ")

(driver "(append (list 1 2 3) (list 4 5 6))")
(driver "(append '(a b c) '(d e f))")
