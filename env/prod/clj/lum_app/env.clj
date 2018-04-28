(ns lum-app.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[lum-app started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[lum-app has shut down successfully]=-"))
   :middleware identity})
