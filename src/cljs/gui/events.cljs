;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.events
  (:require [re-frame.core :as re-frame]
            [utils.logs :as logs]
            [gui.db :as db]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; EVENTS HANDLER

(re-frame/reg-event-db
  ::initialize-db
  (fn  [_ _]
    db/default-db))

;; ACTIVE PANEL
(re-frame/reg-event-db
  ::set-active-panel
  (fn [db [_ active-panel]]
    (assoc db :active-panel active-panel)))

;; REST API
(re-frame/reg-event-db
  ::is-rest-api-online?
  (fn [db [_ is-rest-api-online?]]
    (assoc db :is-rest-api-online? is-rest-api-online?)))

;; EXTERNAL_APIS_CONF
(re-frame/reg-event-db
  ::set-external-apis
  (fn [db [_ external-apis]]
    (assoc db :external-apis external-apis)))

;; CLUSTERS
(re-frame/reg-event-db
  ::set-clusters
  (fn [db [_ clusters]]
    (assoc db :clusters clusters)))

;; CLUSTERS PANEL
(re-frame/reg-event-db
  ::set-selected-option-panel-clusters
  (fn [db [_ clusters]]
    (assoc db :selected-option-panel-clusters clusters)))

;; selected cluster
(re-frame/reg-event-db            ;; register an event handler
  ::set-selected-cluster          ;; for events with this name
  (fn [db [_ selected-cluster]]   ;; get the co-effects and destructure the event
    (assoc db :selected-cluster selected-cluster)))

(re-frame/reg-event-db
  ::set-selected-cluster-node
  (fn [db [_ selected-cluster-node]]
    (assoc db :selected-cluster-node selected-cluster-node)))

(re-frame/reg-event-db
  ::set-selected-node-name
  (fn [db [_ selected-node-name]]
    (assoc db :selected-node-name selected-node-name)))

(re-frame/reg-event-db
  ::set-selected-cluster-name
  (fn [db [_ selected-cluster-name]]
    (assoc db :selected-cluster-name selected-cluster-name)))

(re-frame/reg-event-db
 :modal
 (fn [db [_ data]]
   (assoc-in db [:modal] data)))


;; :testbeds
(re-frame/reg-event-db
  ::set-testbeds
  (fn [db [_ testbeds]]
    (assoc db :testbeds testbeds)))

;; :total-testbeds
(re-frame/reg-event-db
  ::set-total-testbeds
  (fn [db [_ total-testbeds]]
    (assoc db :total-testbeds total-testbeds)))

;; :total-nodes
(re-frame/reg-event-db
  ::set-total-nodes
  (fn [db [_ total-nodes]]
    (assoc db :total-nodes total-nodes)))

;; :total-apps
(re-frame/reg-event-db
  ::set-total-apps
  (fn [db [_ total-apps]]
    (assoc db :total-apps total-apps)))

;; :total-execs
(re-frame/reg-event-db
  ::set-total-execs
  (fn [db [_ total-execs]]
    (assoc db :total-execs total-execs)))

;; :total-execs-completed
(re-frame/reg-event-db
  ::set-total-execs-completed
  (fn [db [_ total-execs-completed]]
    (assoc db :total-execs-completed total-execs-completed)))

;; :total-execs-running
(re-frame/reg-event-db
  ::set-total-execs-running
  (fn [db [_ total-execs-running]]
    (assoc db :total-execs-running total-execs-running)))

;; :total-execs-failed
(re-frame/reg-event-db
  ::set-total-execs-failed
  (fn [db [_ total-execs-failed]]
    (assoc db :total-execs-failed total-execs-failed)))

;; :executions
(re-frame/reg-event-db
  ::set-executions
  (fn [db [_ executions]]
    (assoc db :executions executions)))

;; :exec-times
(re-frame/reg-event-db
  ::set-exec-times
  (fn [db [_ exec-times]]
    (assoc db :exec-times exec-times)))

;; :exec-running
(re-frame/reg-event-db
  ::set-exec-running
  (fn [db [_ exec-running]]
    (assoc db :exec-running exec-running)))

;; :exec-tr
(re-frame/reg-event-db
  ::set-exec-tr
  (fn [db [_ exec-tr]]
    (assoc db :exec-tr exec-tr)))

;; :loading
(re-frame/reg-event-db
  ::set-loading
  (fn [db [_ loading]]
    (assoc db :loading loading)))


(re-frame/reg-event-db
  ::set-selected-app-id
  (fn [db [_ selected-app-id]]
    (assoc db :selected-app-id selected-app-id)))

(re-frame/reg-event-db
  ::set-selected-app
  (fn [db [_ selected-app]]
    (assoc db :selected-app selected-app)))

(re-frame/reg-event-db
  ::set-selected-execution
  (fn [db [_ selected-execution]]
    (assoc db :selected-execution selected-execution)))

(re-frame/reg-event-db
  ::set-selected-app-type
  (fn [db [_ selected-app-type]]
    (assoc db :selected-app-type selected-app-type)))


(re-frame/reg-event-db
  ::set-selected-exec-app-id
  (fn [db [_ selected-exec-app-id]]
    (assoc db :selected-exec-app-id selected-exec-app-id)))

(re-frame/reg-event-db
  ::set-selected-exec-app
  (fn [db [_ selected-exec-app]]
    (assoc db :selected-exec-app selected-exec-app)))

(re-frame/reg-event-db
  ::set-selected-exec-exec-id
  (fn [db [_ selected-exec-exec-id]]
    (assoc db :selected-exec-exec-id selected-exec-exec-id)))

(re-frame/reg-event-db
  ::set-selected-execution-id
  (fn [db [_ selected-execution-id]]
    (assoc db :selected-execution-id selected-execution-id)))

(re-frame/reg-event-db
  ::set-selected-exec-conf-id
  (fn [db [_ selected-exec-conf-id]]
    (assoc db :selected-exec-conf-id selected-exec-conf-id)))

(re-frame/reg-event-db
  ::set-selected-exec-app-type
  (fn [db [_ selected-exec-app-type]]
    (assoc db :selected-exec-app-type selected-exec-app-type)))

;; CFG EXECS RESULTS:
(re-frame/reg-event-db
  ::set-exec_cfg_execs_total
  (fn [db [_ exec_cfg_execs_total]]
    (assoc db :exec_cfg_execs_total exec_cfg_execs_total)))

(re-frame/reg-event-db
  ::set-exec_cfg_execs_completed
  (fn [db [_ exec_cfg_execs_completed]]
    (assoc db :exec_cfg_execs_completed exec_cfg_execs_completed)))

(re-frame/reg-event-db
  ::set-exec_cfg_execs_failed
  (fn [db [_ exec_cfg_execs_failed]]
    (assoc db :exec_cfg_execs_failed exec_cfg_execs_failed)))

(re-frame/reg-event-db
  ::set-exec_cfg_execs_running
  (fn [db [_ exec_cfg_execs_running]]
    (assoc db :exec_cfg_execs_running exec_cfg_execs_running)))

(re-frame/reg-event-db
  ::set-exec_cfg_execs_cancelled
  (fn [db [_ exec_cfg_execs_cancelled]]
    (assoc db :exec_cfg_execs_cancelled exec_cfg_execs_cancelled)))

(re-frame/reg-event-db
  ::set-exec_cfg_execs_restart
  (fn [db [_ exec_cfg_execs_restart]]
    (assoc db :exec_cfg_execs_restart exec_cfg_execs_restart)))

(re-frame/reg-event-db
  ::set-exec_cfg_execs_timeout
  (fn [db [_ exec_cfg_execs_timeout]]
    (assoc db :exec_cfg_execs_timeout exec_cfg_execs_timeout)))

(re-frame/reg-event-db
  ::set-exec_cfg_execs_unknown
  (fn [db [_ exec_cfg_execs_unknown]]
    (assoc db :exec_cfg_execs_unknown exec_cfg_execs_unknown)))

;; Responses / logs
(re-frame/reg-event-db
  ::set-resp-executions
  (fn [db [_ resp-executions]]
    (assoc db :resp-executions resp-executions)))

(re-frame/reg-event-db
  ::set-requ-executions
  (fn [db [_ requ-executions]]
    (assoc db :requ-executions requ-executions)))
