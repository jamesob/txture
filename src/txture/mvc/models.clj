(ns txture.mvc.models
  #^{:doc "We're using flatfile storage."}
  (:require [clojure.contrib.str-utils2 :as str-utils])
  (:use 
     clojure.contrib.duck-streams
     txture.config)
  (:import 
     [java.io File]
     [java.util.regex Pattern]
     [java.util Date]
     [java.text DateFormat]))

(defstruct post
  :body
  :title
  :subtitle
  :date
  :labels)

(defn get-all-posts-seq 
   "Prune the posts directory tree based on file extension, 
   return the flattened seq."
   []
   (let [dirfiles (-> *posts-dir* File. file-seq)
         all-re (Pattern/compile (str ".*" *posts-ext*))]
     (filter #(re-matches all-re (.getPath %)) dirfiles)))

(defn sort-by-time                  
  "Return `fileseq` sorted, decreasing, by last modification time."
  [fileseq]
  (sort #(> (.lastModified %1) (.lastModified %2)) fileseq))

(defn get-post-short-name
  [file]
  (let [p (.getPath file)
        patt (Pattern/compile (str #"([\w-]+)" *posts-ext*))]
    (last (last (re-seq patt p)))))

(defn get-matching-files-seq
  "Return post files whose ID all match a certain pattern as a seq."
  [pattern]
  (filter #(re-matches pattern (.getPath %))
          (-> *posts-dir* File. file-seq)))

(defn get-file-by-id
  "Return a single file based on id."
  [id]
  (let [reg (Pattern/compile (str ".*/" id *posts-ext*))]
    (first (get-matching-files-seq reg))))

;; fns that convert files to posts
;; -------------------------------

(defn postfile->struct
  "Transform a postfile into a post struct for internal use."
  [post-file]

  (defn get-metadata
    "Extract post metadata from lines of file. Expects and returns metadata 
    as a hashmap,"
    [lines]
    (let [metadata-lines (take-while #(not= "" %) lines)
          metadata-str (reduce str metadata-lines)]
      (load-string metadata-str)))

  (defn get-body
    "Extract body from lines of a file."
    [lines]
    (drop 1 (drop-while #(not= % "") lines)))

  (defn get-date
    "Given a file, return its date string."
    [file]
    (let [jdate (new Date (.lastModified file))
          formatter (.. DateFormat getDateInstance)]
      (.format formatter jdate)))

  (let [lines (read-lines (.getPath post-file))
        metadata (get-metadata lines)
        body (get-body lines)
        date-map {:date (get-date post-file)}
        name-map {:short-name (get-post-short-name post-file)}
        curr-post (struct-map post :body body)]
    (println (count body))
    (merge curr-post metadata date-map name-map)))
                                    
(defn files->structs
  "Convert a seq of files to a seq of post structs."
  [fileseq]
  (map #(postfile->struct %) fileseq))

;; fns used in `mvc.controllers`
;; -----------------------------

(defn get-posts-by-id
  "Retrieve posts from disk, fill out a post structs, return them in a seq."
  [& post-ids]
  (let [post-files (map #(get-file-by-id %) post-ids)]
    (files->structs post-files)))

(defn get-recent-posts
  "Retrieve the `n` most recent posts."
  [n]
  (let [postfiles (take n (sort-by-time (get-all-posts-seq)))]
    (files->structs postfiles)))


