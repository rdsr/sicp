(ns sicp.c04.elv.env.frames)

;; Defining a protocol for a frame, since later I'd
;; like to implement frames maybe not as records (maps)
;; but something else

(defprotocol IFrame
  (all-vars [f])
  (all-values [f])
  (has-binding? [f var])
  (read-binding-value [f var])
  (add-binding [f var value])
  (replace-binding [f var value])
  (remove-binding [f var]))


(defrecord Frame []
  IFrame
  (all-vars [f] (keys f))
  (all-values [f] (vals f))
  (has-binding? [f var] (contains? f var))
  (read-binding-value [f var] (get f var))
  (add-binding [f var value] (assoc f var value))

  (replace-binding [f var value]
    {:pre [(has-binding? f var)]}
    (assoc f var value))

  (remove-binding [f var]
    {:pre [(has-binding? f var)]}
    (dissoc f var)))

(defn mk-frame [vars values]
  (reduce (fn [r [k v]]
            (assoc r k v))
          (Frame.)
          (zipmap vars values)))