(ns sicp.c04.ex4-13
  (:require [sicp.c04.elv.env :refer :all]))

(defn mk-unbound! [var env]
  (if (empty-env? env)
    env
    ))
