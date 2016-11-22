(ns sicp.c04.apply
  (:require [sicp.c04.elv.procedure :as p]
            [sicp.c04.elv.begin :refer [eval-sequence]]
            [sicp.c04.elv.env :refer [extend-env]])
  (:refer-clojure :exclude [eval apply true? false?]))


(defn apply [eval-fn proc args]
  (cond
    (p/primitive-procedure? proc)
    (p/apply-primitive-procedure proc args)

    (p/compound-procedure? proc)
    (eval-fn
      (p/procedure-body proc)
      (extend-env
        (p/procedure-parameters proc)
        args
        (p/procedure-env proc)))

    :else (throw (Error. (str "Unknown procedure type: " proc)))))