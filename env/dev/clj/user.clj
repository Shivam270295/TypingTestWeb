(ns user
  (:require [lum-app.config :refer [env]]
            [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [mount.core :as mount]
            [lum-app.core :refer [start-app]]))

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(defn start []
  (mount/start-without #'lum-app.core/repl-server))

(defn stop []
  (mount/stop-except #'lum-app.core/repl-server))

(defn restart []
  (stop)
  (start))


