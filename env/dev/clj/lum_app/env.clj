(ns lum-app.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [lum-app.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[lum-app started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[lum-app has shut down successfully]=-"))
   :middleware wrap-dev})
