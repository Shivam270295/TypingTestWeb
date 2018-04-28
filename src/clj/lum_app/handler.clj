(ns lum-app.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [lum-app.layout :refer [error-page]]
            [lum-app.routes.home :refer [home-routes]]
            [compojure.route :as route]
            [lum-app.env :refer [defaults]]
            [mount.core :as mount]
            [lum-app.middleware :as middleware]))

(mount/defstate init-app
  :start ((or (:init defaults) identity))
  :stop  ((or (:stop defaults) identity)))

(mount/defstate app
  :start
  (middleware/wrap-base
    (routes
      (-> #'home-routes
          (wrap-routes middleware/wrap-csrf)
          (wrap-routes middleware/wrap-formats))
      (route/not-found
        (:body
          (error-page {:status 404
                       :title "page not found"}))))))
