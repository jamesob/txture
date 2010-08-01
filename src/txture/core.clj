(ns txture.core
  (:use compojure.core
        ring.adapter.jetty
        txture.config)
  (:require [compojure.route :as route])
  (:require [txture.mvc.controllers :as controllers]))

(defroutes site
  (GET "/post/:id" [id] 
       (controllers/show-post id))
  (GET "/posts" [] 
       (controllers/show-n-posts *num-posts-shown*))
  (GET "/" [] 
       (controllers/show-n-posts *num-posts-shown*))
  (GET "/:n" [n] 
       (controllers/show-n-posts (Integer/valueOf n)))
  (route/files "/" {:root "static"})
  (route/not-found "Uh-oh!"))

(run-jetty site {:port 8080})
