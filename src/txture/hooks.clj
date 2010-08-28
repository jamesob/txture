(ns txture.hooks
  "Provide hooks for plugins."
  (:require
     txture.plugins.markdown.core))

(def *post-mod-name* "modify-post")
(def *head-add-name* "add-to-head")

(defn- get-fns-by-name
  "Retrieve a list of functions, all with `namestr` somewhere in their names,
  from the visible namespaces."
  [namestr]
  (let [pubs (reduce merge (map #(ns-publics %) (all-ns)))
        only-fns (map #(second %) pubs)
        patt (re-pattern (str "#'txture.plugins.*" namestr))]
    (filter #(re-find patt (str %))
            only-fns)))

(defn- make-thread-fnc
  "Returns a function which threads `foo` through the list `fns`."
  [fns]
  (fn [foo] 
    (loop [fnlist (rest fns)
           result ((first fns) foo)]
      (if (= fnlist ())
        result
        (recur (rest fnlist) ((first fnlist) result))))))


(defn modify-posts
  "Compose all plugins to modify posts somehow."
  [posts]
  (let [all-mod-fns (get-fns-by-name *post-mod-name*)
        thread-fnc (make-thread-fnc all-mod-fns)]
    (println all-mod-fns)
    (if (= () all-mod-fns)
      posts ; nothing to do
      (map thread-fnc posts))))

(defn append-to-head
  []
  (let [all-add-fns (get-fns-by-name *head-add-name*)]
    (reduce str (map #(%) all-add-fns))))

(defn wrap-body)

(defn wrap-head)
