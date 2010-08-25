(ns txture.hooks
  "Provide hooks for plugins."
  (:use
     clojure.contrib.find-namespaces)
  (:import
     [java.io File]))

(def *plugin-nss* 
  (let [txture-nss (find-namespaces-in-dir (new File "."))]
    (filter #(re-find #"txture.plugins.*" (str %)) txture-nss)))

(defn- modify-posts
