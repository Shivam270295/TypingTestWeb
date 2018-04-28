(ns  lum-app.serverService
	(:require [monger.core :as mg]
            [monger.collection :as mc])
  (:import [com.mongodb MongoOptions ServerAddress])
  (:import org.bson.types.ObjectId)
  (:import [com.mongodb MongoOptions ServerAddress]))

(def conUri "mongodb://shivam:shivam@gettingstarted-shard-00-00-hoqlt.mongodb.net:27017,gettingstarted-shard-00-01-hoqlt.mongodb.net:27017,gettingstarted-shard-00-02-hoqlt.mongodb.net:27017/test?ssl=true&replicaSet=GettingStarted-shard-0&authSource=admin")


; (let [uri               "mongodb://shivam:<shivam>@gettingstarted-shard-00-00-hoqlt.mongodb.net:27017,gettingstarted-shard-00-01-hoqlt.mongodb.net:27017,gettingstarted-shard-00-02-hoqlt.mongodb.net:27017/test?ssl=true&replicaSet=GettingStarted-shard-0&authSource=admin"
;       {:keys [conn db]} (mg/connect-via-uri uri)
;       db   (mg/get-db conn "typingdb")
;       coll "paras"]
;       (mc/find db coll {:title "Clojure"}))

(def connection 
   (mg/connect-via-uri conUri ))


(defn getPara [title]
  (let [{:keys [conn db]} connection
      ; db   (mg/get-db conn "typingdb")
      coll "paras"]
      (get (mc/find-one-as-map db coll {:title title}) :text)
      ; (mc/insert-and-return db coll {:title "Lorem" :text "Some Lorem Ipsum Text?"})
      ;(mg/disconnect conn)
      ))

(defn getParaById [id]
  (let [{:keys [conn db]} connection
      ; db   (mg/get-db conn "typingdb")
      coll "paras"]
      (get (mc/find-one-as-map db coll {:_id (ObjectId. id)}) :text)
      ;(mc/find-one-as-map db coll {:_id (ObjectId. id)})
      ; (mc/insert-and-return db coll {:title "Lorem" :text "Some Lorem Ipsum Text?"})
      ;(mg/disconnect conn)
      ))

(defn getAllParas []
  (let [{:keys [conn db]} connection
      ; db   (mg/get-db conn "typingdb")
      coll "paras"]
      ;(mc/remove db coll { :title "Lorem" })
      (seq (mc/find-maps db coll))
      ; (mc/insert-and-return db coll {:title "Lorem" :text "Some Lorem Ipsum Text?"})
      ;(mg/disconnect conn)
      ))

(defn insertPara [title text]
  (let [{:keys [conn db]} connection
      ; db   (mg/get-db conn "typingdb")
      coll "paras"]
      (mc/insert-and-return db coll {:title title :text text})
      ;(mg/disconnect conn)
      ))

