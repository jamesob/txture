(ns txture.mvc.controller
  (:use txture.config)
  (:require 
     [txture.mvc.view :as view]
     [txture.mvc.model :as model]))

(defn show-post [id]
  (let [post (first (model/get-posts-by-id id))]
    (view/show-post post)))

(defn show-n-posts [n]
  (let [posts (model/get-recent-posts n)]
    (view/show-posts posts)))

