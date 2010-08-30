(ns txture.config
  "Things you should absolutely modify.

  All absolute paths are rooted in the `static` directory unless otherwise
  specified in `txture.core`.

  All relative paths are rooted in the directory from which you've run 
  `txture.core`, which (assuming you've followed the instructions) is the 
  top-level.")

;; blog characteristics
;; --------------------

(def *author* 
  "p. marlowe")
(def *title* 
  "title")
(def *subtitle* ; currently does nothing
  "")

(def #^{:doc 
  "<title> of main page's <head>."}
  *text-title* 
  "snappy title by philip marlowe")

(def #^{:doc 
  "Text displayed in side-panel, by default."}
  *description*
  "This is a web log, etc. Hire me.")

(def #^{:doc 
     "these keywords will generate the meta tags used for the entire site."}
  *keywords* 
  "detective, philip marlowe, philip, marlowe")

(def #^{:doc
     "The directory where txture looks for post files.
     
     this is a relative path rooted in wherever you're running `txture.core`
     from; i.e. if you run the webserver as detailed in the instructions and
     leave *posts-dir* defined as 'posts/', the posts folder will be alongside
     'src/' and 'static/'"}
  *posts-dir* 
  "posts/")

(def #^{:doc
     "only look for posts, within *posts-dir*, with this file extension. To pick up
     any file extension, use '.*'."}
  *posts-ext* 
  ".txt")

(def #^{:doc 
     "The location of the stylesheet used for every page.
     
     If you haven't screwed with `src/txture/core.clj`, then any absolute path
     referred to within a reference in the HTML originates in `static/`."}
  *css-loc* 
  "/stylesheets/main.css")

(def 
  #^{:doc "the number of posts shown on the main page"}
  *num-posts-shown* 
  6)

;; post display functions
;; ----------------------
;;
;;   Note: a `post` struct, as defined in `txture.mvc.models.post`, is 
;;   as follows:
;;
;;   (defstruct post
;;      :title
;;      :subtitle
;;      :body-list
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
  "Content that precedes a txture.mvc.models.post."
  [post]
  [:div.post-heading
   [:h2.post-title 
    [:a {:href (post :permalink)} (post :title)]]
   [:h3.post-date (post :date)]])

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
   
(defn *gen-footer* 
  "Returns footer content."
  []
  [:div 
   [:span.important *title*] 
   " by " 
   [:span.important *author*]])

