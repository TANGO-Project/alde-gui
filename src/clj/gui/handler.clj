;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; HANDLER.clj
;;  REST API handler: main point entry for the REST API services.
;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.handler
  (:use [compojure.core]
        [ring.util.response])
  (:require [compojure.route :refer [resources]]
            [compojure.handler :refer [api]]
            [clojure.java.io :as io]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.util.response :refer [resource-response]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.reload :refer [wrap-reload]]
            [clojure.data.json :as json]
            [utils.logs :as logs]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; REST-API ROUTES
(defroutes acf-routes
  ;; GUI
  (GET "/" [] (resource-response "index.html" {:root "public"}))

  ;; mock-data: nodes, testbeds, applications
  (GET  "/mock-data/testbeds"     []  (response (json/read-str (slurp (.getPath (clojure.java.io/resource "data/mock_up_testbeds.json"))))))
  (GET  "/mock-data/nodes"        []  (response (json/read-str (slurp (.getPath (clojure.java.io/resource "data/mock_up_nodes.json"))))))
  (GET  "/mock-data/applications" []  (response (json/read-str (slurp (.getPath (clojure.java.io/resource "data/mock_up_apps.json"))))))
  (GET  "/mock-data/deployments"  []  (response (json/read-str (slurp (.getPath (clojure.java.io/resource "data/mock_up_deployments.json"))))))
  (GET  "/mock-data/executables"  []  (response (json/read-str (slurp (.getPath (clojure.java.io/resource "data/mock_up_executables.json"))))))
  (GET  "/mock-data/execution_configurations" []  (response (json/read-str (slurp (.getPath (clojure.java.io/resource "data/mock_up_execution_configurations.json"))))))
  (GET  "/mock-data/executions"   []  (response (json/read-str (slurp (.getPath (clojure.java.io/resource "data/mock_up_executions.json"))))))

  (resources "/"))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; APP - RUN SERVER
;; NOTE: wrap reload isn't needed when the clj sources are watched by figwheel but it's very good to know about
;;  - wrap-cors: https://github.com/r0man/ring-cors
(def handler
  (do
    (-> #'acf-routes wrap-reload)
    (-> (api (wrap-cors acf-routes
                        :access-control-allow-origin [#".*"]
                        :access-control-allow-methods [:get :post :put]))
        (wrap-json-response) ; {:keywords? true})
        (wrap-json-body))))
