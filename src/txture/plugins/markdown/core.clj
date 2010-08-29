(ns txture.plugins.markdown.core
  "Plugin for txture. Uses Showdown to render Markdown syntax into HTML 
  with JavaScript."
  (:require 
     [clojure.contrib.str-utils2 :as su2]))

(def *head* (str "
<script src='/js/showdown.js' type='text/javascript'></script>
<script src='/js/render-showdown.js' type='text/javascript'></script>"))

(defn add-to-head
  "Add javascript imports to head."
  []
  *head*)

(defn wrap-post-body-HTML
  "Wrap post bodies in a div called `render-markdown` so that this plugin works
  no matter what you call your post content div class."
  [existing-HTML]
  [:div.render-markdown existing-HTML])
