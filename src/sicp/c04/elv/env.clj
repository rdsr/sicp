(ns sicp.c04.elv.env
  (:import (sicp.c04.elv.env Frame)))


;; -- frames data abstraction
(defn mk-frame [vars values] (atom (zipmap vars values)))

(defn frame-variables [frame] (keys @frame))

(defn frame-values [frame] (vals @frame))

(defn has-binding? [frame var] (contains? @frame var))

(defn read-binding-value [frame var] (@frame var))

(defn add-binding-to-frame [frame var value] (swap! frame assoc var value))

(defn remove-binding-from-frame [frame var]
  (assert (has-binding? frame var))
  (swap! frame dissoc var))

(defn replace-binding-in-frame [frame var value]
  (assert (has-binding? frame var))
  (add-binding-to-frame frame var value))


(defn enclosing-env [env] (rest env))
(defn first-frame [env] (first env))
(defn empty-env? [env] (empty? env))
(def empty-env ())
(defn add-frame-to-env [env f] (cons f env))

(defn lookup-var-value [env var]
  (if (empty-env? env)
    (throw (Error. (str "Unbound variable -- lookup-var-value " var)))
    (let [f (first-frame env)
          r (enclosing-env env)]
      (if (has-binding? f var)
        (read-binding-value f var)
        (recur r var)))))

(defn extend-env [base-env vars values]
  (let [cvr (count vars)
        cvl (count values)]
    (if (= cvr cvl)
      (add-frame-to-env base-env
                        (mk-frame vars values))
      (if (< cvr cvl)
        (throw (Error. (str "Too many values supplied" vars values)))
        (throw (Error. (str "Too few values supplied" vars values)))))))

(defn define-variable! [env var value]
  ;; We assume the env always has atleast one frame?
  (add-binding-to-frame (first-frame env) var value)
  env)

(defn set-variable-value! [env var value]
  (if (empty-env? env)
    (throw (Error. (str "Unbound variable -- set! " var)))
    (let [f (first-frame env)
          r (enclosing-env env)]
      (if (has-binding? f var)
        (replace-binding-in-frame f var value)
        (recur r var value)))))

(defn mk-unbound! [env var]
  (if (empty-env? env)
    false
    (let [f (first-frame env)]
      (if (has-binding? f var)
        (do (remove-binding-from-frame f var)
            true)
        (recur (enclosing-env env) var)))))

(defn setup-env []
  (let [intial-env (extend-env (primitive-procedure-names)
                               (primitive-procedure-objects)
                               empty-env)]
    (define-variable! 'true true intial-env)
    (define-variable! 'false false intial-env)
    intial-env))

(defn global-env (setup-env))
