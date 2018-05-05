(ns lum-app.routes.home
  (:require [lum-app.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.response :refer [response]]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.tools.logging :as log]
            [lum-app.serverService :as dbService]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [buddy.auth.accessrules :refer [restrict success error]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            [buddy.auth.backends.httpbasic :refer [http-basic-backend]]))

; (defn home-page []
;   (layout/render
;     "home.html" {:testPara (-> "docs/para.txt" io/resource slurp)}))

(defn on-error
  [request value]
  (layout/error-page {:status 403 :title "Authentication Error"}))

(defn home-page
  ([] (layout/render
    "home.html" {:testPara (dbService/getPara "Clojure")}))
    ;"home.html" {:testPara (dbService/getAllParas)}))
  ([id] (layout/render
    "home.html" {:testPara (dbService/getParaById id)})))


(defn about-page 
  [req]
    (layout/render "about.html"))

(defn add-para-page 
  ([req] (add-para-page "" req))
  ([result req] 
    (if-not (authenticated? req)
      (throw-unauthorized {:message "Not authorized"})
      (layout/render "addPara.html" {:message (:message result)}))))

(defn all-paras-page 
  [req]
    (if-not (authenticated? req)
      (throw-unauthorized)
      (layout/render "allParas.html" {:paras (dbService/getAllParas)})))

(defn addPara [req]
    (let [title (get-in req [:params :title])
            text (get-in req [:params :text])]
        (dbService/insertPara title text)
        ;(dbService/getAllParas)
        {:message "Paragraph Added!"}
        ))

(defn addNewPara [req]
    (if-not (authenticated? req)
      (throw-unauthorized {:message "Not authorized"})
      ;(layout/render "addPara.html" {:message req})
      #(add-para-page "Hello" %)))

(defn test-page 
  [req]
    (layout/render "test.html"))


(defn getWordScore [tes myw]
  (if (= tes myw)
    10
    -5))

(defn calculateScore [testPara inputPara] 
  (let [testWords (str/split testPara #" ")
        myWords (str/split inputPara #" ")]
       (reduce + (mapv getWordScore myWords testWords))))

(defn getScore [req]
  (let [testPara (get-in req [:params :testPara])
        inputPara (get-in req [:params :inputPara])]
    (calculateScore testPara inputPara)))


(defn greet [req] 
  (let [name (get-in req [:params :name])]
    (str "Hello " name)))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/home" [] (home-page))
  (GET "/home/:id" [id] (home-page id))
  (GET "/about" [] about-page)
  (GET "/test" [] test-page)
  (POST "/getScore" [] (fn [req] (str "Score: " (getScore req) )))
  )

(defroutes write-routes
  (GET "/addPara" [] add-para-page)
  (POST "/addNewPara" [] 
    (fn [req] #(add-para-page (addPara req) %))))

(defroutes read-routes
  (GET "/allParas" [] all-paras-page))


