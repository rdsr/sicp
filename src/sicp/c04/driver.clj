(ns sicp.c04.driver
  (:refer-clojure :exclude [eval apply true? false?])
  (:require [sicp.c04.elv.procedure :as p]
            [sicp.c04.eval :as elv]
            [sicp.c04.elv.env :as env])
  (:import (java.io StringReader PushbackReader)))

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
          (let [r (elv/eval e env/global-env)]
            (user-print r))
          (recur (read pbr false nil)))))))
