(ns txture.config)

;; blog characteristics
;; --------------------

(def *author* "p. marlowe")
(def *title* "title")
(def *subtitle* "")
(def *text-title* "snappy title by philip marlowe")
(def *description*
  "This is a web log, etc. Hire me.")
(def *keywords* ; site-wide keywords
  "detective, philip marlowe, philip, marlowe")

;; this is a relative path rooted on the top-level of txture, i.e. if you
;; leave *posts-dir* defined as "posts/", the posts folder will be alongside
;; "src/" and "static/".
(def *posts-dir* "posts/")

(def *posts-ext* ".txt")

;; If you haven't screwed with `src/txture/core.clj`, then any absolute path
;; referred to within a reference in the HTML originates in `static/`.
(def *css-loc* "/css/log.css")

(def *num-posts-shown* 6)

;; post display functions
;; ----------------------
;;
;;   Note: a `post` struct, as defined in `txture.mvc.models.post`, is defined
;;   as the following:
;;
;;   (defstruct post
;;             :title
;;             :subtitle
;;             :body
;;             :labels
;;             :date
;;             :last-modified
;;             :raw-lines
;;             :permalink
;;             :short-name)
;; 
;;   You may use the `post` attributes as they are listed in modifying the
;;   functions that follow.

(defn *before-post*
  "Content that precedes a post."
  [post]
  [:div.post-heading
   [:h2.post-title 
    [:a {:href (str "/post/" (post :short-name))} (post :title)]]
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

(defn *gen-sidebar*
  "Returns sidebar content."
  []
  [:div#side-desc 
   [:h2 "about this"] *description*])
   
(defn *gen-footer* []
  "Returns footer content."
  [:div 
   [:span.important *title*] 
   " by " 
   [:span.important *author*]])

