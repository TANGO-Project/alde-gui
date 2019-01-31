;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [gui.events :as events]
            [gui.routes :as routes]
            [gui.views :as views]
            [gui.globals :as VARS]
            [utils.logs :as logs]
            [restapi.testbeds :as restapi]))


;; DEBUG?
(def debug? ^boolean goog.DEBUG)


(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (logs/debug "dev mode running ...")))


(defn mount-root []
  (logs/debug "mount-root ...")
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/bar-menu-panel]
                  (.getElementById js/document "bar-menu"))
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app"))
  (reagent/render [views/footer-panel]
                  (.getElementById js/document "footer-app"))
  (reagent/render [views/breadcrumb]
                  (.getElementById js/document "div-breadcrumb"))
  (logs/info "mount-root ... ok"))


(defn ^:export init []
  (logs/debug "gui.core ...")
  (routes/app-routes)
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root)
  (logs/info "gui.core ... ok"))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; INIT
(do
  (logs/info "- INIT")
  (logs/info " Getting testbeds from ALDE ...")

  (js/setTimeout #(do ;(logs/info "INI: DELAYED TASK (1 - testbeds)...")
                      (restapi/get-testbeds VARS/update-testbeds)) 800)
  (js/setTimeout #(do ;(logs/info "INI: DELAYED TASK (2 - nodes)...")
                      (restapi/get-nodes VARS/update-nodes)) 900)
  (js/setTimeout #(do ;(logs/info "INI: DELAYED TASK (3 - applications)...")
                      (restapi/get-apps VARS/update-apps)) 1000)
  (js/setTimeout #(do ;(logs/info "INI: DELAYED TASK (4 - executions)...")
                      (restapi/get-execs VARS/update-execs)) 1400)

  (js/setTimeout #(do ;(logs/info "INI: DELAYED TASK (5 - executions completed)...")
                      (restapi/get-executions-completed VARS/update-execs-completed)) 1100)
  (js/setTimeout #(do ;(logs/info "INI: DELAYED TASK (6 - executions running)...")
                      (restapi/get-executions-running VARS/update-execs-running)) 1200)
  (js/setTimeout #(do ;(logs/info "INI: DELAYED TASK (7 - executions failed)...")
                      (restapi/get-executions-failed VARS/update-execs-failed)) 1300)

  (restapi/execute-periodic-task #(do ;(logs/info "PERIODIC TASK (1 - testbeds)...")
                                      (restapi/get-testbeds VARS/update-testbeds)) 90)
  (restapi/execute-periodic-task #(do ;(logs/info "PERIODIC TASK (2 - nodes)...")
                                      (restapi/get-nodes VARS/update-nodes)) 95)
  (restapi/execute-periodic-task #(do ;(logs/info "PERIODIC TASK (3 - applications)...")
                                      (restapi/get-apps VARS/update-apps)) 30)
  (restapi/execute-periodic-task #(do ;(logs/info "PERIODIC TASK (4 - executions)...")
                                      (restapi/get-execs VARS/update-execs)) 60)

  (restapi/execute-periodic-task #(do ;(logs/info "PERIODIC TASK (5 - executions completed)...")
                                      (restapi/get-executions-completed VARS/update-execs-completed)) 60)
  (restapi/execute-periodic-task #(do ;(logs/info "PERIODIC TASK (6 - executions running)...")
                                      (restapi/get-executions-running VARS/update-execs-running)) 65)
  (restapi/execute-periodic-task #(do ;(logs/info "PERIODIC TASK (6 - executions failed)...")
                                      (restapi/get-executions-failed VARS/update-execs-failed)) 70)
)
