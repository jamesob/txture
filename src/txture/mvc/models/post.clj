(ns txture.mvc.models.post
  (:require 
     [clojure.contrib.str-utils2 :as str-utils])
  (:use 
     clojure.contrib.duck-streams
     txture.config)
  (:import 
     [java.io File]
     [java.util Date]
     [java.text DateFormat]))

(defstruct post
  :title
  :subtitle
  :body
  :labels
  :date
  :last-modified
  :raw-lines
  :permalink
  :short-name)

(def *valid-kws* ["title" "subtitle" "labels"])

(defn- file->post
  "Establish a `post` struct-map, get all readily available information from
  `file`, then return the post."
  [file]
  (let [lm (.lastModified file)
        lines (read-lines (.getPath file))]
    (struct-map post :raw-lines lines :last-modified lm)))

(defn- body
  "Return a post's body lines."
  [post]
  (let [rlines (post :raw-lines)]
    (drop 1 (drop-while #(not (str-utils/blank? %)) rlines))))

(defn- post-header-data
  "Isolate all post header lines into strings and return them in a map."
  [post]
  
  (defn- get-tag+val 
    "Return a list of a line's tag and value. May return an empty list."
    [line]
    (drop 1 (re-find #"\s*:(\w+):\s+(.*)" line)))

  (defn- line->map 
    "Take in one line, presumably of the form ':tag value' and return
    {:tag 'val'} if the tag is in `kws`."
    [line kws]
    (let [t-v (get-tag+val line)
          tag (first t-v)
          valu (last t-v)
          tag-kw (keyword tag)]
      (if (some #(= % tag) kws) 
        {tag-kw valu} 
        nil)))
  (let [hlines (take-while #(not (str-utils/blank? %)) (post :raw-lines))
        line->map (fn [line] (line->map line *valid-kws*))
        maps (map line->map hlines)
        recognized (filter #(not (nil? %)) maps)]
    (reduce merge recognized)))

(defn return-post
  [file]
  "Given a file, return a filled-out post struct. Called in `mvc.model`."
  (let [apost (file->post file)
        body (post-body apost)
        h-map (post-header-data apost)
        title (h-map :title)
        subtitle (h-map :subtitle)
        labels (h-map :labels)

                                    
