(ns sicp.c04.elv.env
  (:use [sicp.c04.elv.env.frames]))

(defn empty-env? [e] (empty? e))
(defn add-frame [e f] (conj e f))
(defn first-frame [e] (first e))
(defn enclosing-env [e] (rest e))

(defn lookup-variable-value [e var]
  (if (empty-env? e)
    (throw (Error. (str "Unbound variable -- lookup-var-value " var)))
    (let [f (first-frame e)]
      (if (has-binding? @f var)
        (read-binding-value @f var)
        (recur (enclosing-env e) var)))))

(defn extend-env [e vars values]
  (let [cvr (count vars)
        cvl (count values)]
    (if (= cvr cvl)
      ;; each frame is wrapped in an atom. We need that to implement
      ;; set-variable! and mk-unbound! correctly
      (add-frame e (atom (mk-frame vars values)))
      (if (< cvr cvl)
        (throw (Error. (str "Too many values supplied" vars values)))
        (throw (Error. (str "Too few values supplied" vars values)))))))

(defn define-variable! [e var value]
  ;; We assume the env always has atleast one
  ;; frame (with predefined vars and procedures
  (swap! (first-frame e) add-binding var value) 'ok)

(defn set-variable-value! [e var value]
  (if (empty-env? e)
    (throw (Error. (str "Unbound variable -- set! " var)))
    (let [f (first-frame e)]
      (if (has-binding? @f var)
        (do (swap! f replace-binding var value) nil)
        (recur (enclosing-env e) var value)))))

(defn mk-unbound! [e var]
  (when-not (empty-env? e)
    (let [f (first-frame e)]
      (if (has-binding? @f var)
        (do (swap! f remove-binding var) nil)
        (recur (enclosing-env e) var)))))

(def empty-env ())

(defn setup-env []
  (let [intial-env
        (extend-env empty-env
                    []         ; (primitive-procedure-names)
                    []         ; (primitive-procedure-objects)
                    )]
    (define-variable! 'true true intial-env)
    (define-variable! 'false false intial-env)
    intial-env))

;(defn global-env (setup-env))
