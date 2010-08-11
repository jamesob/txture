(ns txture.mvc.model
  (:require 
     [clojure.contrib.str-utils2 :as str-utils]
     [txture.dates :as dates]
     [txture.mvc.models.post :as post])
  (:use 
     clojure.contrib.duck-streams
     txture.config)
  (:import 
     [java.io File]
     [java.util.regex Pattern]
     [java.util Date]
     [java.text DateFormat]))


;; fns for file retrieval
;; ----------------------

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

(defn files->posts
  "Convert a seq of files to a seq of post structs; involves doing some date
  wrangling along the way."
  [fileseq]
  (let [posts (map #(post/file->post %) fileseq)]
    (if (not (dates/secret-dates-exists?)) (dates/init-secret-dates posts))
    (dates/update-secret-dates posts)
    (dates/adjust-post-dates posts)))

;; fns used in `mvc.controller`
;; -----------------------------

(defn get-posts-by-id
  "Retrieve posts from disk, fill out a post structs, return them in a seq."
  [& post-ids]
  (let [post-files (map #(get-file-by-id %) post-ids)]
    (files->posts post-files)))

(defn get-recent-posts
  "Retrieve the `n` most recent posts."
  [n]
  (let [postfiles (take n (sort-by-time (get-all-posts-seq)))]
    (files->posts postfiles)))


