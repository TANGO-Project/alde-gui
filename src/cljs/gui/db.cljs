;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.db)

;; DB with default values
(def default-db
  {:name                            "re-frame"
   ;:active-panel                    :home-panel
   :is-rest-api-online?             false
   :external-apis                   {}
   :clusters                        {}
   :selected-cluster                {}
   :selected-cluster-node           {}
   :selected-node-name              nil
   :selected-cluster-name           "-"
   :selected-app-id                 "-"
   :selected-app                    {}
   :selected-app-type               "-"
   :selected-option-panel-clusters  :view-clusters
   :loading                         false
   :executions                      [:tr]
   :exec-times                      [:tr]
   :exec-running                    [:tr]
   :exec-tr                         [:tr]
   :testbeds                        [:li.list-group-item "-empty-"]
   :total-testbeds                  0
   :total-nodes                     0
   :total-apps                      0
   :total-execs                     0
   :total-execs-completed           0
   :total-execs-running             0
   :total-execs-failed              0
   :selected-exec-app-id            "-"
   :selected-exec-app               {}
   :selected-exec-exec-id           "-"
   :selected-exec-conf-id           "-"
   :selected-exec-app-type          "-"
   :selected-execution-id           "-"
   :selected-execution              {}
   :exec_cfg_execs_total            0
   :exec_cfg_execs_completed        0
   :exec_cfg_execs_failed           0
   :exec_cfg_execs_running          0
   :exec_cfg_execs_cancelled        0
   :exec_cfg_execs_restart          0
   :exec_cfg_execs_timeout          0
   :exec_cfg_execs_unknown          0
   ;; responses / logs
   :requ-executions                 "-"
   :resp-executions                 "-"})
