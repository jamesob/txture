(ns txture.plugins.markdown.core
  "Plugin for txture. Uses Showdown to render Markdown syntax into HTML 
  with JavaScript."
  (:require [clojure.contrib.str-utils2 :as su2]))

(def *head* (str "
<script src='/js/showdown.js' type='text/javascript'></script>
<script src='/js/render-showdown.js' type='text/javascript'></script>"))

(defn modify-post
  "Modifies a post."
  [post]
  (let [body (post :body-list)
        newbody (concat body (list "this is a plugin!"))]
    (println body)
    (assoc post :body-list newbody)))

(defn add-to-head
  "Appends some text to the head"
  []
  *head*)
