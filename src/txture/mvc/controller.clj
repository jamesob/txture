(ns txture.mvc.controller
  (:use txture.config)
  (:require 
     [txture.mvc.view :as view]
     [txture.mvc.model :as model]))

(defn show-post [id]
  (println (str "showing " id))
  (let [post (first (models/get-posts-by-id id))]
    (views/show-post post)))

(defn show-n-posts [n]
  (let [posts (models/get-recent-posts n)]
    (views/show-posts posts)))

