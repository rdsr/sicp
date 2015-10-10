(ns sicp.c04.driver
  (:refer-clojure :exclude [eval apply true? false?])
  (:require [sicp.c04.eval :as elv]
            [sicp.c04.elv.env :as env]))

(def input-prompt ">")
(def output-prompt "|")

(defn display [s]
  (newline) (println s) (newline))

(defn driver [s]
  ;(display input-prompt)
  (let [out (elv/eval s env/global-env)]
    (println "here")
    (display output-prompt)
    (display out)))

(elv/eval '((lambda (x) (+ x 45)) 3) env/global-env)
;(.printStackTrace *e)

