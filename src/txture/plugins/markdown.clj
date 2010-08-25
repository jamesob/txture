(ns txture.plugins.markdown)

(defn modify-post
  "Modifies a post."
  [post]
  (let [body (post :body-list)
        newbody (concat body (list "this is a plugin!"))]
    (println body)
    (assoc post :body-list newbody)))
