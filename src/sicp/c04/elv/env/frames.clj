(ns sicp.c04.elv.env.frames)

(defprotocol IFrame
  (all-vars [f])
  (all-values [f])
  (has-binding? [f var])
  (read-binding-value [f var])
  (add-binding [f var value])
  (replace-binding [f var value])
  (remove-binding [f var]))

(defrecord FrameR [vars values]
  IFrame
  (all-vars [f] vars)
  (all-values [f] values)
  (has-binding? [f var] (contains? f var))
  (read-binding-value [f var] (f var))
  (add-binding [f var value] (assoc f var value))

  (replace-binding [f var value]
    {:pre [(has-binding? f var)]}
    (assoc f var value))

  (remove-binding [f var]
    {:pre [(has-binding? f var)]}
    (dissoc f var)))

(defn mk-frame [vars values]
  (FrameR. vars values))