(ns lum-app.test.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [lum-app.handler :refer :all]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [clojure.tools.logging :as log]
            [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]
            [mount.core :as mount]))

(use-fixtures
  :once
  (fn [f]
    (mount/start #'lum-app.config/env
                 #'lum-app.handler/app)
    (f)))

(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= 200 (:status response)))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= 404 (:status response)))))

  (testing "getScore response1"
    (let [response (app (request :post "/getScore" {:testPara "hello" :inputPara "hello"}))]
        (is (= "Score: 10" (:body response)))))

  (testing "getScore response2"
    (let [response (app (request :post "/getScore" {:testPara "hello world" :inputPara "hello"}))]
        (is (= "Score: 10" (:body response)))))

  (testing "getScore response3"
    (let [response (app (request :post "/getScore" {:testPara "hello world" :inputPara "hello earth"}))]
        (is (= "Score: 5" (:body response)))))

  (testing "getScore response4"
    (let [response (app (request :post "/getScore" {:testPara "hello world" :inputPara "hi earth"}))]
        (is (= "Score: -10" (:body response)))))

  )
