(ns txture.mvc.models.post
  (:require 
     [clojure.contrib.str-utils2 :as str-utils])
  (:use 
     clojure.contrib.duck-streams
     txture.config)
  (:import 
     [java.io File]
     [java.util.regex Pattern]
     [java.util Date]
     [java.text DateFormat]))

(defstruct post
  :title
  :subtitle
  :body
  :labels-list
  :labels-str
  :date
  :last-modified
  :raw-lines
  :permalink
  :short-name)

(def *valid-kws* ["title" "subtitle" "labels"])

;; utility fns
;; -----------

(defn- comma-str->list
  "Convert a string of items delimited by commas to a list."
  [comma-str]
  (str-utils/split comma-str #",\s*"))

(defn- strs->regex
  "Given a number of strings, return a regex pattern."
  [& strs]
  (Pattern/compile (reduce str strs)))

;; fns used to derive post-struct data
;; -----------------------------------

(defn- lines->body
  "Given all the lines in a post, return the body lines."
  [rlines]
  (drop 1 (drop-while #(not (str-utils/blank? %)) rlines)))

(defn- lines->header
  "Given all the lines in a post, return the header lines."
  [rlines]
  (take-while #(not (str-utils/blank? %)) rlines))

(defn- post-header->str-map
  "Given header lines, extract from each the keyword and value and return them
  in a map."
  [hlines]
  (defn- get-tag+val 
    "Return a list of a line's tag and value. May return an empty list."
    [line]
    (drop 1 (re-find #"\s*:(\w+):\s+(.*)" line)))
  (defn- line->map 
    "Take in one line, presumably of the form ':tag: value' and return
    {:tag 'val'} if the tag is in `kws`."
    [line]
    (let [t-v (get-tag+val line)
          tag (first t-v)
          valu (last t-v)
          tag-kw (keyword tag)]
      (if (some #(= % tag) *valid-kws*) 
        {tag-kw valu} 
        nil)))
  (let [maps (map line->map hlines)
        recognized (filter #(not (nil? %)) maps)]
    (reduce merge recognized)))


(defn- file=>shortname-str
  "Get the filename, minus the extension, from a file. Return as string."
  [file]
  (let [path (.getPath file)]
    (last (re-find (strs->regex #".*/([\w-.]+)" *posts-ext*) 
                   path))))

(defn- file=>permalink-str
  "Derive a permalink string from a file."
  [file]
  (let [shortname (file=>shortname-str file)]
    (str "/post/" shortname)))   

;; fns to be called externally, returning a post
;; ---------------------------------------------

(defn file->post
  "Given a file, return a filled-out post struct. Called in `mvc.model`."
  [file]
  (let [last-mod (.lastModified file)
        rlines (read-lines (.getPath file))
        body-lines (lines->body rlines)
        header-lines (lines->header rlines)
        h-map (post-header->str-map header-lines)
        title (h-map :title)
        subtitle (h-map :subtitle)
        labels-str (h-map :labels)
        labels-list (comma-str->list (h-map :labels))
        plink (file=>permalink-str file)
        shortname (file=>shortname-str file)
        date-str (*datetime-long->str* last-mod)]
    (struct-map post
                :title title
                :subtitle subtitle
                :body body-lines
                :labels-str labels-str
                :labels-list labels-list
                :date date-str
                :last-modified last-mod
                :raw-lines rlines
                :permalink plink
                :short-name shortname)))
  

                                    
