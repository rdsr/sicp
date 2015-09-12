(ns sicp.c04.elv.env)

(defn mk-frame [vars values] (zipmap vars values))
(defn frame-variables [frame] (keys frame))
(defn frame-values [frame] (vals frame))
(defn add-binding-to-frame [var value frame] (assoc frame var value))


(defn lookup-var-value [var env]
  (letfn [(lookup [env]
                  (if (empty? env)
                    (throw (Error. ("Unbound variable" var)))
                    (let [f (first env)
                          r (rest env)]
                      (if (contains? f var)
                        (f var)
                        (lookup r)))))]
    (lookup @env)))

(defn extend-env! [vars values base-env]
  (let [cvr (count vars)
        cvl (count values)]
    (if (= cvr cvl)
      (swap! base-env conj (mk-frame vars values))
      (if (< cvr cvl)
        (throw (Error. (str "Too many values supplied" vars values)))
        (throw (Error. (str "Too few values supplied" vars values)))))))

(defn define-variable! [var value env]
  (swap! env (fn [env]
               (if (empty? env)
                 (list {var value})
                 (let [f (first env)
                       r (rest env)]
                   (conj r (assoc f var value)))))))
(defn set-variable-value! [var value env]
  ;; todo can this be done in a single iteration?
  (if (some #(contains? % var) @env)
    (swap! env (fn [env]
                 (map (fn [f]
                        (if (contains? f var)
                          (assoc f var value)
                          f))
                      env)))
    (throw (Error. (str "Unbound variable set!" var)))))

(defn enclosing-env [env] (rest env))
(defn first-frame [env] (first env))
(def empty-env '())



