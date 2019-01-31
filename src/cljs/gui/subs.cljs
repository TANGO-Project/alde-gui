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

;; :exec-times
(re-frame/reg-sub
  ::exec-times
  (fn [db _]
    (:exec-times db)))

;; :exec-running
(re-frame/reg-sub
  ::exec-running
  (fn [db _]
    (:exec-running db)))

;; exec-tr
(re-frame/reg-sub
  ::exec-tr
  (fn [db _]
    (:exec-tr db)))

;; :total-execs
(re-frame/reg-sub
  ::total-execs
  (fn [db _]
    (:total-execs db)))

;; :total-execs-completed
(re-frame/reg-sub
  ::total-execs-completed
  (fn [db _]
    (:total-execs-completed db)))

;; :total-execs-running
(re-frame/reg-sub
  ::total-execs-running
  (fn [db _]
    (:total-execs-running db)))

;; :total-execs-failed
(re-frame/reg-sub
  ::total-execs-failed
  (fn [db _]
    (:total-execs-failed db)))

;; :loading
(re-frame/reg-sub
  ::loading
  (fn [db _]
    (:loading db)))


;; Execs graph

(re-frame/reg-sub
 ::selected-exec-app-id
 (fn [db]
   (:selected-exec-app-id db)))

(re-frame/reg-sub
 ::selected-exec-app
 (fn [db]
   (:selected-exec-app db)))

(re-frame/reg-sub
 ::selected-execution-id
 (fn [db]
   (:selected-execution-id db)))

(re-frame/reg-sub
 ::selected-exec-exec-id
 (fn [db]
   (:selected-exec-exec-id db)))

(re-frame/reg-sub
 ::selected-exec-conf-id
 (fn [db]
   (:selected-exec-conf-id db)))

(re-frame/reg-sub
 ::selected-execution
 (fn [db]
   (:selected-execution db)))

(re-frame/reg-sub
 ::selected-exec-app-type
 (fn [db]
   (:selected-exec-app-type db)))


;; CFG EXECS RESULTS:
(re-frame/reg-sub
 ::exec_cfg_execs_total
 (fn [db]
   (:exec_cfg_execs_total db)))

(re-frame/reg-sub
 ::exec_cfg_execs_completed
 (fn [db]
   (:exec_cfg_execs_completed db)))

(re-frame/reg-sub
 ::exec_cfg_execs_failed
 (fn [db]
   (:exec_cfg_execs_failed db)))

(re-frame/reg-sub
 ::exec_cfg_execs_running
 (fn [db]
   (:exec_cfg_execs_running db)))

(re-frame/reg-sub
 ::exec_cfg_execs_cancelled
 (fn [db]
   (:exec_cfg_execs_cancelled db)))

(re-frame/reg-sub
 ::exec_cfg_execs_restart
 (fn [db]
   (:exec_cfg_execs_restart db)))

(re-frame/reg-sub
 ::exec_cfg_execs_timeout
 (fn [db]
   (:exec_cfg_execs_timeout db)))

(re-frame/reg-sub
 ::exec_cfg_execs_unknown
 (fn [db]
   (:exec_cfg_execs_unknown db)))

;; Responses / logs
(re-frame/reg-sub
 ::resp-executions
 (fn [db]
   (:resp-executions db)))

(re-frame/reg-sub
 ::requ-executions
 (fn [db]
   (:requ-executions db)))
