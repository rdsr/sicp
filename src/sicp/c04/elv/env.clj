(ns sicp.c04.elv.env
  (:use [sicp.c04.elv.env.frames])
  (:require [sicp.c04.elv.procedure :as p])
  (:refer-clojure :exclude [eval apply true? false?]))

(defn empty-env? [e] (empty? e))
(defn add-frame [e f] (conj e f))
(defn first-frame [e] (first e))
(defn enclosing-env [e] (rest e))

(defn lookup-variable-value [var e]
  (if (empty-env? e)
    (throw (Error. (str "Unbound variable -- lookup-variable-value " var)))
    (let [f (first-frame e)]
      (if (has-binding? @f var)
        (read-binding-value @f var)
        (recur var
               (enclosing-env e))))))

(defn extend-env [vars values e]
  (let [cvr (count vars)
        cvl (count values)]
    (if (= cvr cvl)
      ;; each frame is wrapped in an atom. We need that to implement
      ;; set-variable! and mk-unbound! correctly
      (add-frame e (atom (mk-frame vars values)))
      (if (< cvr cvl)
        (throw (Error. (str "Too many values supplied" vars values)))
        (throw (Error. (str "Too few values supplied" vars values)))))))

(defn define-variable! [var value e]
  ;; We assume the env always has atleast one
  ;; frame (with predefined vars and procedures
  (swap! (first-frame e) add-binding var value) 'ok)

(defn set-variable-value! [var value e]
  (if (empty-env? e)
    (throw (Error. (str "Unbound variable -- set! " var)))
    (let [f (first-frame e)]
      (if (has-binding? @f var)
        (do (swap! f replace-binding var value) nil)
        (recur var value
               (enclosing-env e))))))

(defn mk-unbound! [var e]
  (when-not (empty-env? e)
    (let [f (first-frame e)]
      (if (has-binding? @f var)
        (do (swap! f remove-binding var) nil)
        (recur var
               (enclosing-env e))))))

(def empty-env ())

(defn setup-env []
  (let [intial-env
        (extend-env (p/primitive-procedure-names)
                    (p/primitive-procedure-objects)
                    empty-env)]
    (define-variable! 'true true intial-env)
    (define-variable! 'false false intial-env)
    intial-env))

(def global-env (setup-env))
(doseq [a global-env]
  (let [frame @a]
    (doseq [name (keys frame)]
      (println name))))