;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns restapi.testbeds
  (:require [gui.globals :as VARS]
            [utils.logs :as logs]
            [utils.http_cljs :as http]
            [gui.apps.graphs2 :as graphs2-executions]
            [re-frame.core :as re-frame]
            [gui.subs :as subs]))


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
(defn rem-application "Deletes application from ALDE"
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
  (let [selected-exec-app     (re-frame/subscribe [::subs/selected-exec-app])]
    (logs/info "Call to: POST " (str @VARS/REST_API_URL "execution_configurations : " (@selected-exec-app :id)))
    (http/POST-EXECUTIONS
      (str @VARS/REST_API_URL "execution_configurations")
      ;#(do (get-apps VARS/update-apps) (graphs2-executions/reload-app (@selected-exec-app :id)))
      #(graphs2-executions/reload-app (@selected-exec-app :id))
      json-params)))


;; FUNCTION: launch-exec
;; curl -X PATCH -H'Content-type: application/json' http://127.0.0.1:5000/api/v1/execution_configurations/1 -d'{"launch_execution": true}'
(defn launch-exec "Launch new execution"
  [id]
  (reset! VARS/TAB_EXECUTIONS_LOADING true)
  ;(logs/info "Call to: PATCH " (str @VARS/REST_API_URL "execution_configurations/" id))
  (http/PATCH-EXECUTIONS
    (str @VARS/REST_API_URL "execution_configurations/" id)
    ;#(do (get-apps VARS/update-apps) (graphs2-executions/reload-executions (str "conf_" id)))
    #(graphs2-executions/reload-executions (str "conf_" id))
    "{\"launch_execution\": true}"))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; EXECUTIONS:


(defn get-executions-completed ""
  [f]
  ;(http/GET "http://localhost:8081/mock-data/executions_completed" {:with-credentials? false} f))
  (http/GET
    (str @VARS/REST_API_URL "executions?q={\"filters\":[{\"val\":\"COMPLETED\",\"op\":\"like\",\"name\":\"status\"}]}")
    {:with-credentials? false}
    f))


(defn get-executions-running ""
  [f]
  ;(http/GET "http://localhost:8081/mock-data/executions_running" {:with-credentials? false} f))
  (http/GET
    (str @VARS/REST_API_URL "executions?q={\"filters\":[{\"val\":\"RUNNING\",\"op\":\"like\",\"name\":\"status\"}]}")
    {:with-credentials? false}
    f))


(defn get-executions-failed ""
  [f]
  ;(http/GET "http://localhost:8081/mock-data/executions_failed" {:with-credentials? false} f))
  (http/GET
    (str @VARS/REST_API_URL "executions?q={\"filters\":[{\"val\":\"FAILED\",\"op\":\"like\",\"name\":\"status\"}]}")
    {:with-credentials? false}
    f))


;; FUNCTION: get-apps
(defn get-execs-from-conf "Gets all executions from exec_conf"
  [conf-id f]
  ;(logs/info "Call to: GET " (str @VARS/REST_API_URL "execution_configurations/" conf-id))
  ;(http/GET "http://localhost:8081/mock-data/execution_configurations/x" {:with-credentials? false} f)) ; no-connection / dev tests
  (http/GET (str @VARS/REST_API_URL "execution_configurations/" conf-id) {:with-credentials? false} f))


;; stop-execution
(defn stop-execution ""
  [id]
  (reset! VARS/TAB_EXECUTIONS_LOADING true)
  (let [selected-exec-conf-id (re-frame/subscribe [::subs/selected-exec-conf-id])]
    (http/PATCH-EXECUTIONS
      (str @VARS/REST_API_URL "executions/" id)
      #(graphs2-executions/reload-executions @selected-exec-conf-id)
      "{\"status\": \"STOP\"}")))

;; cancel-execution
(defn cancel-execution ""
  [id]
  (reset! VARS/TAB_EXECUTIONS_LOADING true)
  (let [selected-exec-conf-id (re-frame/subscribe [::subs/selected-exec-conf-id])]
    (http/PATCH-EXECUTIONS
      (str @VARS/REST_API_URL "executions/" id)
      #(graphs2-executions/reload-executions @selected-exec-conf-id)
      "{\"status\": \"CANCEL\"}")))

;; restart-execution
(defn restart-execution ""
  [id]
  (reset! VARS/TAB_EXECUTIONS_LOADING true)
  (let [selected-exec-conf-id (re-frame/subscribe [::subs/selected-exec-conf-id])]
    (http/PATCH-EXECUTIONS
      (str @VARS/REST_API_URL "executions/" id)
      #(graphs2-executions/reload-executions @selected-exec-conf-id)
      "{\"status\": \"RESTART\"}")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; FUNCTION: not-implemented-func
(defn not-implemented-func ""
  [param1 param2 param3]
  {:message "not-implemented"})
