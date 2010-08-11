(ns txture.config)

;;; THINGS YOU SHOULD ABSOLUTELY MODIFY
;;; -----------------------------------

;; blog characteristics
;; --------------------

(def *author* "p. marlowe")
(def *title* "title")
(def *subtitle* "")
(def *text-title* "snappy title by philip marlowe")
(def *description*
  "This is a web log, etc. Hire me.")
;;
;; these keywords will be generate the meta tags used for the entire site.
(def *keywords* 
  "detective, philip marlowe, philip, marlowe")

;; this is a relative path rooted on the top-level of txture, i.e. if you
;; leave *posts-dir* defined as "posts/", the posts folder will be alongside
;; "src/" and "static/".
(def *posts-dir* "posts/")

;; only look for posts with this file extension. To pick up any file extension,
;; use ".*".
(def *posts-ext* ".txt")

;; If you haven't screwed with `src/txture/core.clj`, then any absolute path
;; referred to within a reference in the HTML originates in `static/`.
(def *css-loc* "/stylesheets/main.css")

;; the number of posts shown on the main page
(def *num-posts-shown* 6)

;; post display functions
;; ----------------------
;;
;;   Note: a `post` struct, as defined in `txture.mvc.models.post`, is 
;;   as follows:
;;
;;   (defstruct post
;;      :title
;;      :subtitle
;;      :body
;;      :labels-str
;;      :labels-list
;;      :date
;;      :last-modified
;;      :raw-lines
;;      :permalink
;;      :short-name)
;; 
;;   You may use the `post` attributes as they are listed in modifying the
;;   functions that follow.
;;
(defn *before-post*
  "Content that precedes a post."
  [post]
  [:div.post-heading
   [:h2.post-title 
    [:a {:href (post :permalink)} (post :title)]]
   [:h3.post-date (post :date)]])

(defn *post-body*
  "How the body of a post is displayed."
  [post]
  [:div.post-body
   (for [line (post :body)]
     (str line \newline))])

(defn *after-post*
  "What is displayed after each post."
  [post]
  [:div.post-footer
   [:div.permalink 
    "[" [:a {:href (post :permalink)} "permalink"] "]"]
   [:div.labels 
    [:span.inline-code "filed under: "]
    (post :labels-str)]])

;; sidebar display functions
;; -------------------------

(defn *gen-sidebar*
  "Returns sidebar content."
  []
  [:div#side-desc 
   [:h2 "about this"] *description*])

;; footer display functions
;; ------------------------
   
(defn *gen-footer* []
  "Returns footer content."
  [:div 
   [:span.important *title*] 
   " by " 
   [:span.important *author*]])

