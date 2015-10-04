(ns sicp.c04.elv.env.frames)

(defn all-vars [f] (keys f))
(defn all-values [f] (vals f))
(defn has-binding? [f var] (contains? f var))
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