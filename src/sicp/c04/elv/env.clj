(ns sicp.c04.elv.env
  ;(:import (sicp.c04.elv.env.frames IFrame))
  (:use [sicp.c04.elv.env.frames])
  (:import (clojure.lang IPersistentList)))

(defprotocol IEnv
  (empty-env? [e])
  (add-frame [e ^IFrame f])
  (first-frame [e])
  (lookup-var-value [e var])
  (extend-env [e vars values])
  (enclosing-env [e])
  (define-variable! [e var value])
  (set-variable-value! [e var value])
  (mk-unbound! [e var])
  )

(deftype Env [^IPersistentList frames]
  IEnv
  (empty-env? [e] (empty? frames))
  (add-frame [e f] (Env. (conj frames f)))
  (first-frame [e] (first frames))
  (enclosing-env [e] (Env. (rest frames)))

  (lookup-var-value [e var]
    (loop [[f & r] frames]
      (cond
        (nil? f) (throw (Error. (str "Unbound variable -- lookup-var-value " var)))
        (has-binding? @f var) (read-binding-value @f var)
        :else (recur r))))

  (extend-env [e vars values]
    (let [cvr (count vars)
          cvl (count values)]
      (if (= cvr cvl)
        (add-frame e (atom (mk-frame vars values)))
        (if (< cvr cvl)
          (throw (Error. (str "Too many values supplied" vars values)))
          (throw (Error. (str "Too few values supplied" vars values)))))))

  (define-variable! [e var value]
    ;; We assume the env always has atleast one frame
    ;; TODO: Verify or make sure that this assumption is valid
    (swap! (first-frame e) add-binding var value)
    'ok)

  (set-variable-value! [e var value]
    (loop [[f & r] frames]
      (cond (nil? f) (throw (Error. (str "Unbound variable -- set! " var)))
            (has-binding? @f var) (do (swap! f replace-binding var value) 'ok)
            :else (recur r))))

  (mk-unbound! [e var]
    (loop [[f & r] frames]
      (cond (nil? f) (s)
            (has-binding? @f var) (do (swap! f remove-binding var) 'ok)
            :else (recur r))))
  )

(def empty-env (Env. ()))

(defn setup-env []
  (let [intial-env (extend-env empty-env
                               []         ; (primitive-procedure-names)
                               []         ; (primitive-procedure-objects)
                               )]
    (define-variable! 'true true intial-env)
    (define-variable! 'false false intial-env)
    intial-env))

;(defn global-env (setup-env))
