(ns txture.config)

(def *author* "p. marlowe")
(def *title* "title")
(def *subtitle* "")
(def *text-title* "snappy title by philip marlowe")
(def *description*
  "This is a web log, etc. Hire me.")

(def *keywords* ; site-wide keywords
  "detective, philip marlowe, philip, marlowe")

;; this is a relative path based rooted on the top-level of txture, i.e. if you
;; leave *posts-dir* defined as "posts/", the posts folder will be alongside
;; "src/" and "static/".
(def *posts-dir* "posts/")

(def *posts-ext* ".txt")

;; If you haven't screwed with `src/txture/core.clj`, then any absolute path
;; referred to within a reference in the HTML originates in `static/`.
(def *css-loc* "/css/log.css")

(def *num-posts-shown* 6)


