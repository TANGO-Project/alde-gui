;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns restapi.testbeds
  (:require [utils.logs :as logs]
            [gui.globals :as VARS]
            [utils.http_cljs :as http]))


;; FUNCTION: execute-periodic-task
(defn execute-periodic-task "Excecute a periodic task 'f' every 't' seconds. Returns the task's id"
  [f t]
  (js/setInterval f (* t 1000)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; TESTBEDS
;; FUNCTION: get-testbeds
(defn get-testbeds "Gets all the testbeds from ALDE"
  [f]
  ;(http/GET "http://localhost:8081/mock-data/testbeds" {:with-credentials? false} f)) ; no-connection / dev tests
  (http/GET
    (str @VARS/REST_API_URL "testbeds")
    {:with-credentials? false}
    f))


;; add-testbed
(defn add-testbed "Adds a new testbed to ALDE"
  [json-params]
  (http/POST
    (str @VARS/REST_API_URL "testbeds")
    #(do (get-testbeds VARS/update-testbeds) (.reload js/location))
    json-params))


;; rem-testbed
(defn rem-testbed "Adds a new testbed to ALDE"
  [id f]
  (http/DELETE
    (str @VARS/REST_API_URL "testbeds/" id)
    f))
    ;#(do (get-testbeds VARS/update-testbeds) (.reload js/location))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; NODES
;; FUNCTION: get-nodes
(defn get-nodes "Gets all the nodes from ALDE"
  [f]
  ;(http/GET "http://localhost:8081/mock-data/nodes" {:with-credentials? false} f)) ; no-connection / dev tests
  (http/GET (str @VARS/REST_API_URL "nodes") {:with-credentials? false} f))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; EXECUTIONS
;; FUNCTION: get-execs
(defn get-execs "Gets all the executions from ALDE"
  [f]
  ;(http/GET "http://localhost:8081/mock-data/executions" {:with-credentials? false} f)) ; no-connection / dev tests
  ; ?page=1&results_per_page=10
  (http/GET (str @VARS/REST_API_URL "executions") {:with-credentials? false} f))


;; FUNCTION: get-execs-page
(defn get-execs-page "Gets all the executions from ALDE"
  [f p]
  ;(http/GET "http://localhost:8080/mock-data/executions" {:with-credentials? false} f)) ; no-connection / dev tests
  ; ?page=1&results_per_page=10
  (http/GET (str @VARS/REST_API_URL "executions?page=" p "&results_per_page=10") {:with-credentials? false} f))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; APPLICATIONS:

;; FUNCTION: get-apps
(defn get-apps "Gets all the apps from ALDE"
  [f]
  ;(http/GET "http://localhost:8081/mock-data/applications" {:with-credentials? false} f)) ; no-connection / dev tests
  (http/GET (str @VARS/REST_API_URL "applications") {:with-credentials? false} f))


;; rem-application
(defn rem-application "Adds a new application to ALDE"
  [id]
  (http/DELETE
    (str @VARS/REST_API_URL "applications/" id)
    #(do (get-apps VARS/update-apps) (.reload js/location))))


;; FUNCTION: new-application
(defn new-application "Creates a new application"
  [json-params]
  (http/POST
    (str @VARS/REST_API_URL "applications")
    #(do (get-apps VARS/update-apps) (.reload js/location))
    json-params))


;; FUNCTION: add-conf
(defn add-conf "Creates a new execution configuration"
  [json-params]
  (http/POST
    (str @VARS/REST_API_URL "execution_configurations")
    #(do (get-apps VARS/update-apps) (.reload js/location))
    json-params))


;; FUNCTION: launch-exec
;; curl -X PATCH -H'Content-type: application/json' http://127.0.0.1:5000/api/v1/execution_configurations/1 -d'{"launch_execution": true}'
(defn launch-exec "Launch the execution"
  [id]
  (http/PATCH
    (str @VARS/REST_API_URL "execution_configurations/" id)
    #(do (get-apps VARS/update-apps) (.reload js/location))
    "{\"launch_execution\": true}"))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; FUNCTION: not-implemented-func
(defn not-implemented-func ""
  [param1 param2 param3]
  {:message "not-implemented"})
