;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; EVENTS SUBSCRIBERS

(re-frame/reg-sub
  ::name
  (fn [db]
    (:name db)))

;; ACTIVE PANEL
;; Return value of 'active-panel'
;;  ==> (let [active-panel (re-frame/subscribe [::subs/active-panel])]
(re-frame/reg-sub
  ::active-panel
  (fn [db _]
    (:active-panel db)))

;; REST API
;; Return value of 'is-rest-api-online?'
;;  ==> (let [is-rest-api-online? (re-frame/subscribe [::subs/is-rest-api-online?])]
(re-frame/reg-sub
  ::is-rest-api-online?
  (fn [db _]
    (:is-rest-api-online? db)))

;; CLUSTERS PANEL
;; Return value of 'selected-option-panel-clusters' -clusters panel menu options-
(re-frame/reg-sub
  ::selected-option-panel-clusters
  (fn [db _]
    (:selected-option-panel-clusters db)))

;; SELECTED CLUSTER
(re-frame/reg-sub
 ::selected-cluster
 (fn [db]
   (:selected-cluster db)))

;; SELECTED CLUSTER NODE
(re-frame/reg-sub
 ::selected-cluster-node
 (fn [db]
   (:selected-cluster-node db)))

;; SELECTED NODE -NAME-
(re-frame/reg-sub
 ::selected-node-name
 (fn [db]
   (:selected-node-name db)))

(re-frame/reg-sub
 ::selected-cluster-name
 (fn [db]
   (:selected-cluster-name db)))

;; MODAL
(re-frame/reg-sub-raw
 :modal
 (fn [db _] (reaction (:modal @db))))


(re-frame/reg-sub
 ::selected-app-id
 (fn [db]
   (:selected-app-id db)))

(re-frame/reg-sub
 ::selected-app
 (fn [db]
   (:selected-app db)))


(re-frame/reg-sub
 ::selected-app-type
 (fn [db]
   (:selected-app-type db)))


;; TANGO
;; :testbeds
(re-frame/reg-sub
  ::testbeds
  (fn [db _]
    (:testbeds db)))

;; :total-testbeds
(re-frame/reg-sub
  ::total-testbeds
  (fn [db _]
    (:total-testbeds db)))

;; :total-nodes
(re-frame/reg-sub
  ::total-nodes
  (fn [db _]
    (:total-nodes db)))

;; :total-apps
(re-frame/reg-sub
  ::total-apps
  (fn [db _]
    (:total-apps db)))

;; :executions
(re-frame/reg-sub
  ::executions
  (fn [db _]
    (:executions db)))

;; :loading
(re-frame/reg-sub
  ::loading
  (fn [db _]
    (:loading db)))
