(ns sicp.c04.elv.env.frames
  (:require [clojure.spec :as s]))

(s/def ::frame (s/and map? (fn [m] (every? symbol? (keys m)))))

(defn all-vars [f] (keys f))
(s/fdef all-vars
        :args ::frame
        :ret (s/* keyword?))

(defn all-values [f] (vals f))
(s/fdef all-values :args ::frame)


(defn has-binding? [f var] (contains? f var))
(s/fdef has-binding?
        :args (s/cat ::frame symbol?)
        :ret boolean?)

(defn add-binding [f var value] (assoc f var value))


(defn read-binding-value [f var]
  {:pre [(has-binding? f var)]}
  (get f var))


(defn replace-binding [f var value]
  {:pre [(has-binding? f var)]}
  (assoc f var value))

(defn remove-binding [f var]
  {:pre [(has-binding? f var)]}
  (dissoc f var))

(defn mk-frame [vars values] (zipmap vars values))