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



;; :executions
(re-frame/reg-event-db
  ::set-executions
  (fn [db [_ executions]]
    (assoc db :executions executions)))

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
  ::set-selected-app-type
  (fn [db [_ selected-app-type]]
    (assoc db :selected-app-type selected-app-type)))
