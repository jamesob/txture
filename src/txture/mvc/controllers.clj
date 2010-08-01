(ns txture.mvc.controllers
  (:use txture.config)
  (:require [txture.mvc.views :as views])
  (:require [txture.mvc.models :as models]))

(defn show-post [id]
  (println (str "showing " id))
  (let [post (first (models/get-posts-by-id id))]
    (views/show-post post)))

(defn show-n-posts [n]
  (let [posts (models/get-recent-posts n)]
    (views/show-posts posts)))

