(ns txture.hooks
  "Provide hooks for plugins."
  (:use
     clojure.contrib.find-namespaces)
  (:import
     [java.io File]))

;; name of functions to look for in each plugin namespace
(def *post-mod-name* "modify-post")
(def *head-add-name* "add-to-head")

(defn- get-fns-by-name
  "Retrieve a list of functions, all with `namestr` somewhere in their names,
  from the visible namespaces."
  [namestr]
  (let [nss (find-namespaces-in-dir (new File "."))
        plugin-nss (filter #(re-find #"txture.plugins.*" (str %)) nss)
        import-nss (dorun (map require plugin-nss)) ;; only for side-effects
        pubs (reduce merge (map #(ns-publics %) plugin-nss))
        only-fns (map #(second %) pubs)
        patt (re-pattern namestr)]
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
