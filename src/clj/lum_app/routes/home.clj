(ns lum-app.routes.home
  (:require [lum-app.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [lum-app.serverService :as dbService]))

; (defn home-page []
;   (layout/render
;     "home.html" {:testPara (-> "docs/para.txt" io/resource slurp)}))

(defn home-page
  ([] (layout/render
    "home.html" {:testPara (dbService/getPara "Clojure")}))
    ;"home.html" {:testPara (dbService/getAllParas)}))
  ([id] (layout/render
    "home.html" {:testPara (dbService/getParaById id)})))

(defn about-page []
  (layout/render "about.html"))

(defn add-para-page 
  ([] (layout/render "addPara.html"))
  ([message] (layout/render "addPara.html" {:message message})))

(defn all-paras-page 
  ([] (layout/render "allParas.html" {:paras (dbService/getAllParas)})))

(defn addPara [req]
  (let [title (get-in req [:params :title])
          text (get-in req [:params :text])]
      (dbService/insertPara title text)
      ;(dbService/getAllParas)
      (str "Paragraph Added!")
      ))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/home" [] (home-page))
  (GET "/home/:id" [id] (home-page id))
  (GET "/about" [] (about-page))
  (GET "/addPara" [] (add-para-page))
  (POST "/addNewPara" [] 
    (fn [req] (add-para-page (addPara req))))
  (GET "/allParas" [] (all-paras-page)))





