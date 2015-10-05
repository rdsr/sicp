(ns sicp.c04.apply
  (:require [sicp.c04.elv.procedure :as p]
            [sicp.c04.elv.begin :refer [eval-sequence]]
            [sicp.c04.elv.env :refer [extend-env]])
  (:refer-clojure :exclude [eval apply true? false?]))

(defn apply [procedure arguments]
  (cond
    (p/primitive-procedure? procedure)
    (p/apply-primitive-procedure procedure arguments)
    (p/compound-procedure? procedure)
    (eval-sequence
      (p/procedure-body procedure)
      (extend-env
        (p/procedure-parameters procedure)
        arguments
        (p/procedure-env procedure)))
    :else (Error. (str "Unknown procedure type -- apply " procedure))))