(ns txture.hooks
  "Provide hooks for plugins."
  (:use
     clojure.contrib.find-namespaces)
  (:import
     [java.io File]))

;; name of functions to look for in each plugin namespace
(def *post-mod-name* "modify-post")
(def *head-add-name* "add-to-head")

(defn- get-plugin-nss
  "Return a list of all plugin namespaces visible from `dirname`."
  [dirname]
  (let [nss (find-namespaces-in-dir (new File dirname))]
    (filter #(re-find #"txture.plugins.*" (str %)) nss)))

(defn- require-nss
  "Given a list of namespaces, require all of them."
  [nss]
  (dorun (map require nss)))

(defn- extract-all-fns
  "Given a list of namespaces, return the fully qualified name of
  all functions in the each namespace, merged together into one list."
  [nss]
  (let [pubs (reduce merge (map #(ns-publics %) nss))
        only-fns (map #(second %) pubs)]
    only-fns))

(defn- get-fns-by-name
  "Retrieve a list of functions, all with `namestr` somewhere in their names,
  from the visible namespaces."
  [namestr]
  (let [plugin-nss (get-plugin-nss ".")
        import-nss (require-nss plugin-nss) ;; only for side-effects
        fns (extract-all-fns plugin-nss)
        patt (re-pattern namestr)]
    (filter #(re-find patt (str %)) fns)))

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
