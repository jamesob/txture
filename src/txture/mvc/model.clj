(ns txture.mvc.model
  (:require 
     [clojure.contrib.str-utils2 :as str-utils]
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

;; post date juggling 
;; ------------------
;;
;;   Since there's no standard indicator of when a file was first created, we have
;;   to do something a little ugly: manually track when txture has first seen a
;;   post.
;;
;;   We do this by keeping a ``secret dates'' file, which tracks, for each post,
;;   when that post entered the blog and when it was last modified.
;;
;;   ``Why track the last modification in a file? We already have that information
;;   from Java.'' We record the last time a file was modified because then we can
;;   implement some stupid caching scheme later that compares the .lastModified
;;   property of a post's file with our `*secret-date-file*`'s to see if we
;;   should reload the post data.
;;

(defn secret-dates-exists?
  "Has a secret dates file been established yet?"
  []
  (let [secret (-> *secret-date-file* File.)]
    (if (= 0 (.lastModified secret)) false true)))

(defn read-secret-dates-map
  "Read in current secret post date information, return as a map.
  Looks as follows: 
    {post's-permalink-str [creation-date last-modified-date], ...}."
  []
  (let [secret-lines (read-lines *secret-date-file*)]
    (load-string (str-utils/join " " secret-lines))))

(defn write-secret-dates-map
  "Write out `new-date-map` to *secret-date-file*."
  [new-date-map]
  (spit *secret-date-file* (str new-date-map)))

(defn new-date-entry
  "Given a post, return a map containing the date storage information used to
  enter it into *secret-date-file*."
  [post]
  {(post :permalink) [(post :date) (post :last-modified)]})

(defn init-secret-dates
  "Given a list of posts, initialize *secret-date-file*."
  [posts]
  (let [maps (map #(new-date-entry %) posts)
        date-map (reduce merge maps)]
    (write-secret-dates-map date-map)))
   
(defn post-in-date-map?
  "Given a post and a date map, check to see if the post is in that map."
  [post date-map]
  (if (date-map (post :permalink)) true false))
 
(defn add-absent-post-dates
  "Given a list of posts and a date-map, check to see that each post is being 
  tracked in *secret-date-file* and if not, add it to the map."
  [posts date-map]

  (defn- map-if-absent
    "If `post` isn't in `date-map`, return a map with date storage info, else 
    return nil."
    [post]
    (if (not (post-in-date-map? post date-map)) 
      (new-date-entry post) nil))

  (let [new-entries (reduce merge (map map-if-absent posts))]
    (merge date-map new-entries)))

(defn update-secret-dates
  "Given a list of posts, update *secret-date-file*."
  [posts]
  (let [sec-date-map (read-secret-dates-map)
        new-map (add-absent-post-dates posts sec-date-map)]
    (write-secret-dates-map new-map)))

(defn adjust-post-dates
  "Given a list of posts, alter each `post` struct to reflect when txture
  first saw that particular post. Return a list of altered posts."
  [posts]

  (defn- alter-date
    "Assoc txture's post date to the post struct."
    [post date-map]
    (if (date-map (post :permalink))
      (assoc post :date (first (date-map (post :permalink))))))

  (let [date-map (read-secret-dates-map)]
    (map #(alter-date % date-map) posts)))

;; fns that convert files to posts
;; -------------------------------

(defn files->posts
  "Convert a seq of files to a seq of post structs."
  [fileseq]
  (let [posts (map #(post/file->post %) fileseq)]
    (if (not (secret-dates-exists?)) (init-secret-dates posts))
    (update-secret-dates posts)
    (adjust-post-dates posts)))

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


