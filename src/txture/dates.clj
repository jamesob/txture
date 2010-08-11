(ns txture.dates
  "Handles all date-wrangling and post date storage."
  (:require 
     [clojure.contrib.str-utils2 :as str-utils])
  (:use
     clojure.contrib.duck-streams
     txture.config)
  (:import 
     [java.io File]
     [java.util Date]
     [java.text DateFormat]))

;; where post date information is written out
(def secret-date-file "metadata/post-dates.data.clj")

;; used globally for date/time formatting
(def date-formatter (. DateFormat getDateTimeInstance
                         DateFormat/MEDIUM DateFormat/SHORT))

;; date conversion fns
;; -------------------

(defn datetime-str->long
  "Given `datetime-str`, return the date's long value."
  [dstr]
  (let [jdate (.parse date-formatter dstr)]
    (.getTime jdate)))

(defn datetime-long->str
  "Given `dlong`, return the date's corresponding string value."
  [dlong]
  (let [jdate (new Date dlong)]
    (.format date-formatter jdate)))
 
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
;;   property of a post's file with our `secret-date-file`'s to see if we
;;   should reload the post data.
;;

(defn secret-dates-exists?
  "Has a secret dates file been established yet?"
  []
  (let [secret (-> secret-date-file File.)]
    (if (= 0 (.lastModified secret)) false true)))

(defn read-secret-dates-map
  "Read in current secret post date information, return as a map.
  Looks as follows: 
    {post's-permalink-str [creation-date last-modified-date], ...}."
  []
  (let [secret-lines (read-lines secret-date-file)]
    (load-string (str-utils/join " " secret-lines))))

(defn write-secret-dates-map
  "Write out `new-date-map` to secret-date-file."
  [new-date-map]
  (spit secret-date-file (str new-date-map)))

(defn new-date-entry
  "Given a post, return a map containing the date storage information used to
  enter it into secret-date-file."
  [post]
  {(post :permalink) [(post :date) (post :last-modified)]})

(defn init-secret-dates
  "Given a list of posts, initialize secret-date-file."
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
  tracked in secret-date-file and if not, add it to the map."
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
  "Given a list of posts, update secret-date-file."
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
 
