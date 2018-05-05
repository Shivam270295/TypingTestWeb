(ns lum-app.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [lum-app.layout :refer [error-page]]
            [lum-app.routes.home :refer [home-routes write-routes read-routes]]
            [compojure.route :as route]
            [lum-app.env :refer [defaults]]
            [mount.core :as mount]
            [clojure.tools.logging :as log]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            [ring.util.response :refer [response]]
            [buddy.auth.backends.httpbasic :refer [http-basic-backend]]
            [buddy.auth.accessrules :refer [success error]]
            [buddy.auth.accessrules :refer [wrap-access-rules]]
            [lum-app.middleware :as middleware]))


(def authdata
  {:admin "secret"
   :test "secret"})

(def authWrite
  {:admin "secret"})

(def authRead
  {:test "secret"})

(defn my-authfn
  [req {:keys [username password]}]
  (when-let [user-password (get authdata (keyword username))]
    (when (= password user-password)
      (keyword username))))

(defn my-unauthorized-handler
  [request metadata]
  (-> (response "Unauthorized request")
      (assoc :status 403)))

(defn authenticated-user
  [request]
  (println "authenticated-user")
  (if (:identity request)
    ;true
    (let [user (:identity request)]
      (log/error (str "user: " user))
      (if (= user :admin)
      true
      (error "Only authenticated users allowed")))
    (do
      true
      )))

(defn on-error
  [request value]
  (error-page {:status 403 :title "Authentication Error"})
  )

(def rules [{:pattern #"^/.*"
             :handler authenticated-user}])

(def auth-backend
  (http-basic-backend {:realm "MyRealm"
                       :authfn my-authfn}))


(mount/defstate init-app
  :start ((or (:init defaults) identity))
  :stop  ((or (:stop defaults) identity)))

(mount/defstate app
  :start
  ;(do
   (let [options {:rules rules :on-error on-error}] 
  (middleware/wrap-base
    (routes
      (-> #'home-routes
          ;(wrap-routes middleware/wrap-csrf)
          (wrap-routes middleware/wrap-formats)
          )
      (-> #'read-routes
          (wrap-routes middleware/wrap-csrf)
          (wrap-routes middleware/wrap-formats)
          (wrap-routes wrap-access-rules options)
          (wrap-routes wrap-authorization auth-backend)
          (wrap-routes wrap-authentication auth-backend)
          )
      (-> #'write-routes
          (wrap-routes middleware/wrap-csrf)
          (wrap-routes middleware/wrap-formats)
          (wrap-routes wrap-access-rules options)
          (wrap-routes wrap-authorization auth-backend)
          (wrap-routes wrap-authentication auth-backend)
          ;(wrap-access-rules options)
          )
      (route/not-found
        (:body
          (error-page {:status 404
                       :title "page not found"})))))))
(defn init [] ())

(defn destroy [] ())
