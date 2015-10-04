(ns sicp.c04.ex4-13
  (:require [sicp.c04.elv.env.frames :refer [has-binding? remove-binding]])
  (:require [sicp.c04.elv.env :refer [empty-env? enclosing-env first-frame]]))

(defn mk-unbound! [e var]
  (when-not
    (empty-env? e)
    (let [f (first-frame e)]
      (if (has-binding? @f var)
        (do (swap! f remove-binding var) nil)
        (recur (enclosing-env e) var)))))