;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
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
    (logs/debug "DB: EVENTS: call to initialize-db ")
    db/default-db))

;; ACTIVE PANEL
(re-frame/reg-event-db
  ::set-active-panel
  (fn [db [_ active-panel]]
    (logs/debug "DB: EVENTS: call to set-active-panel " active-panel)
    (assoc db :active-panel active-panel)))

;; REST API
(re-frame/reg-event-db
  ::is-rest-api-online?
  (fn [db [_ is-rest-api-online?]]
    (logs/debug "DB: EVENTS: call to is-rest-api-online? " is-rest-api-online?)
    (assoc db :is-rest-api-online? is-rest-api-online?)))

;; EXTERNAL_APIS_CONF
(re-frame/reg-event-db
  ::set-external-apis
  (fn [db [_ external-apis]]
    (logs/debug "DB: EVENTS: call to set-external-apis " external-apis)
    (assoc db :external-apis external-apis)))

;; CLUSTERS
(re-frame/reg-event-db
  ::set-clusters
  (fn [db [_ clusters]]
    (logs/debug "DB: EVENTS: call to set-clusters " clusters)
    (assoc db :clusters clusters)))

;; CLUSTERS PANEL
(re-frame/reg-event-db
  ::set-selected-option-panel-clusters
  (fn [db [_ clusters]]
    (logs/debug "DB: EVENTS: call to set-selected-option-panel-clusters " clusters)
    (assoc db :selected-option-panel-clusters clusters)))

;; selected cluster
(re-frame/reg-event-db            ;; register an event handler
  ::set-selected-cluster          ;; for events with this name
  (fn [db [_ selected-cluster]]   ;; get the co-effects and destructure the event
    (logs/debug "DB: EVENTS: call to set-selected-cluster " selected-cluster)
    (assoc db :selected-cluster selected-cluster)))

(re-frame/reg-event-db
  ::set-selected-cluster-node
  (fn [db [_ selected-cluster-node]]
    ;(logs/debug "DB: EVENTS: call to set-selected-cluster-node... " selected-cluster-node)
    (assoc db :selected-cluster-node selected-cluster-node)))

(re-frame/reg-event-db
  ::set-selected-cluster-name
  (fn [db [_ selected-cluster-name]]
    (logs/debug "DB: EVENTS: call to set-selected-cluster-name... " selected-cluster-name)
    (assoc db :selected-cluster-name selected-cluster-name)))

(re-frame/reg-event-db
 :modal
 (fn [db [_ data]]
   (logs/debug "DB: EVENTS: call to modal " data)
   (assoc-in db [:modal] data)))

;; :testbeds
(re-frame/reg-event-db
  ::set-testbeds
  (fn [db [_ testbeds]]
    ;(logs/debug "DB: EVENTS: call to set-testbeds " testbeds)
    (assoc db :testbeds testbeds)))

;; :executions
(re-frame/reg-event-db
  ::set-executions
  (fn [db [_ executions]]
    ;(logs/debug "DB: EVENTS: call to set-executions " executions)
    (assoc db :executions executions)))

;; :loading
(re-frame/reg-event-db
  ::set-loading
  (fn [db [_ loading]]
    ;(logs/debug "DB: EVENTS: call to set-loading " loading)
    (assoc db :loading loading)))


(re-frame/reg-event-db
  ::set-selected-app-id
  (fn [db [_ selected-app-id]]
    (logs/debug "DB: EVENTS: call to set-selected-app-id... " selected-app-id)
    (assoc db :selected-app-id selected-app-id)))

(re-frame/reg-event-db
  ::set-selected-app
  (fn [db [_ selected-app]]
    (logs/debug "DB: EVENTS: call to set-selected-app... " selected-app)
    (assoc db :selected-app selected-app)))

(re-frame/reg-event-db
  ::set-selected-app-type
  (fn [db [_ selected-app-type]]
    (logs/debug "DB: EVENTS: call to set-selected-app-type... " selected-app-type)
    (assoc db :selected-app-type selected-app-type)))
