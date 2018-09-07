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

;; ROUTES
;;    from  https://github.com/TANGO-Project/alde/blob/master/src/main/python/models.py
;;          https://github.com/TANGO-Project/alde/blob/master/src/main/python/alde.py
;;
;;    /api/v1/applications
;;    /api/v1/testbeds
;;    /api/v1/deployments
;;    /api/v1/executables
;;    /api/v1/executions
;;    /api/v1/execution_configurations
;;    /api/v1/memories
;;    /api/v1/cpus
;;    /api/v1/gpus
;;    /api/v1/nodes
;;    /api/v1/upload ??????????
;;
;;
;;
;;
;;
;;
;;
;;
;;
;;
;;
;;







;; FUNCTION: execute-periodic-task
(defn execute-periodic-task "Excecute a periodic task 'f' every 't' seconds. Returns the task's id"
  [f t]
  (js/setInterval f (* t 1000)))


(def port 3001) ; 8080

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; TESTBEDS
;; FUNCTION: get-testbeds
(defn get-testbeds "Gets all the testbeds from ALDE"
  [f]
  (http/GET "http://localhost:8080/mock-data/testbeds" {:with-credentials? false} f))
  ;(http/GET (str @VARS/REST_API_URL "testbeds") {:with-credentials? false} f))


;; FUNCTION: get-nodes-from-testbed
(defn get-nodes-from-testbed "Gets the nodes from a testbed"
  [f testbed-id]
  (http/GET "http://localhost:8080/mock-data/testbeds" {:with-credentials? false} f))
  ;(http/GET (str @VARS/REST_API_URL "testbeds/" testbed-id "/nodes") {:with-credentials? false} f))


;; FUNCTION: get-nodes-from-testbed-node
(defn get-nodes-from-testbed-node "Gets a testbeds' node info from ALDE"
  [f testbed-id node-id]
  (http/GET "http://localhost:8080/mock-data/testbeds" {:with-credentials? false} f))
  ;(http/GET (str @VARS/REST_API_URL "testbeds/" testbed-id "/nodes/" node-id) {:with-credentials? false} f))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; NODES
;; FUNCTION: get-nodes
(defn get-nodes "Gets all the nodes from ALDE"
  [f]
  (http/GET "http://localhost:8080/mock-data/nodes" {:with-credentials? false} f))
  ;(http/GET (str @VARS/REST_API_URL "nodes") {:with-credentials? false} f))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; EXECUTIONS
;; FUNCTION: get-execs
(defn get-execs "Gets all the executions from ALDE"
  [f]
  (http/GET "http://localhost:8080/mock-data/executions" {:with-credentials? false} f))
  ;(http/GET (str @VARS/REST_API_URL "applications") {:with-credentials? false} f))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; add-testbed
;; TODO
(defn add-testbed "Adds a testbed"
  [func json-params]
  ; endpoint func m json-params
  (http/POST (str @VARS/REST_API_URL "nodes?") func {:with-credentials? false} json-params))


;; add-node-to-testbed
;; TODO
(defn add-node-to-testbed "Adds a node to testbed"
  [func json-params]
  ; endpoint func m json-params
  (http/POST (str @VARS/REST_API_URL "nodes?") func {:with-credentials? false} json-params))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; APPLICATIONS:

;; FUNCTION: get-apps
(defn get-apps "Gets all the apps from ALDE"
  [f]
  (http/GET "http://localhost:8080/mock-data/applications" {:with-credentials? false} f))
  ;(http/GET (str @VARS/REST_API_URL "applications") {:with-credentials? false} f))


;; FUNCTION: new-application
(defn new-application "Creates a new application"
  [func json-params]
  ; endpoint func m json-params
  (http/POST (str @VARS/REST_API_URL "applications") func {:with-credentials? false} json-params))


;; FUNCTION: update-application
(defn update-application "Updates application"
  [id func json-params]
  ; endpoint func m json-params
  (http/POST (str @VARS/REST_API_URL "upload/" id) func {:with-credentials? false} json-params))
