(ns txture.hooks
  "Provide hooks for plugins."
  (:use
     clojure.contrib.find-namespaces)
  (:import
     [java.io File]))

;; name of functions to look for in each plugin namespace
(def *post-mod-name* "modify-post")
(def *head-add-name* "append-to-head")
(def *wrap-post-body-name* "wrap-post-body-HTML")
(def *wrap-post-name* "wrap-post-HTML")
(def *body-end-add-name* "append-to-body-end")

;;; utility functions
;;; -----------------

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
  (let [pubs (map ns-publics nss)
        only-fns (map vals pubs)]
    (println (str "pubs" pubs))
    (println (str "only-fns" only-fns))
    (reduce concat only-fns)))

(defn- get-fns-by-name
  "Retrieve a list of functions, all with `namestr` somewhere in their names,
  from the visible namespaces."
  [namestr]
  (let [plugin-nss (get-plugin-nss ".")
        import-nss (require-nss plugin-nss) ;; only for side-effects
        fns (extract-all-fns plugin-nss)
        patt (re-pattern namestr)]
    (println plugin-nss)
    (println patt)
    (println "all functions: ")
    (println fns)
    (filter #(re-find patt (str %)) fns)))

(defn- make-thread-fnc
  "Returns a function which threads `foo` through the list `fns`."
  [fns]
  (fn [foo] 
    (if (empty? fns)
      foo ; nothing to do
      (loop [fnlist (rest fns)
             result ((first fns) foo)]
        (if (= fnlist ())
          result
          (recur (rest fnlist) ((first fnlist) result)))))))

(defn- accum-str-results
  "Given a list of functions, all of which should have arity 0 and
  return strings, accumulate their results into one string."
  [fns]
  (reduce str (map #(%) fns)))

;;; public functions
;;; ----------------

(defn modify-posts
  "Compose all post modification plugin fns and apply to all posts. Each
  function named `*post-mod-name*` defined in plugin files must take one
  argument."
  [posts]
  (let [all-mod-fns (get-fns-by-name *post-mod-name*)
        thread-fnc (make-thread-fnc all-mod-fns)]
    (map thread-fnc posts)))

(defn append-to-head
  "Add to each page's <head>. Functions of `*head-add-name*` defined
  in plugin files must not take any arguments."
  []
  (let [all-add-fns (get-fns-by-name *head-add-name*)]
    (println all-add-fns)
    (accum-str-results all-add-fns)))

(defn append-to-body-end
  []
  (let [all-add-fns (get-fns-by-name *body-end-add-name*)]
    (accum-str-results all-add-fns)))


(defn wrap-post-body-HTML
  "Wraps each post's body HTML in more HTML. Functions defined in plugin
  files named `*wrap-post-name*` must (i) have arity 1 and (ii) use
  hiccup HTML syntax."
  [post-HTML]
  (let [all-wrap-fns (get-fns-by-name *wrap-post-body-name*)
        thread-fnc (make-thread-fnc all-wrap-fns)]
    (thread-fnc post-HTML)))

(defn wrap-post-HTML
  "Wraps each post's HTML in more HTML. Functions defined in plugin
  files named `*wrap-post-name*` must (i) have arity 1 and (ii) use
  hiccup HTML syntax."
  [post-HTML]
  (let [all-wrap-fns (get-fns-by-name *wrap-post-name*)
        thread-fnc (make-thread-fnc all-wrap-fns)]
    (thread-fnc post-HTML)))
 

