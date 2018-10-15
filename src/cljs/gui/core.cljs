;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
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

  (js/setTimeout #(do (logs/info "DELAYED TASK (1): ") (restapi/get-testbeds VARS/update-testbeds)) 1000)
  (js/setTimeout #(do (logs/info "DELAYED TASK (2): ") (restapi/get-nodes VARS/update-nodes)) 1000)
  (js/setTimeout #(do (logs/info "DELAYED TASK (3): ") (restapi/get-apps VARS/update-apps)) 1000)

  (restapi/execute-periodic-task #(do (logs/info "PERIODIC TASK (1): ") (restapi/get-testbeds VARS/update-testbeds)) 90)
  (restapi/execute-periodic-task #(do (logs/info "PERIODIC TASK (2): ") (restapi/get-nodes VARS/update-nodes)) 95)
  (restapi/execute-periodic-task #(do (logs/info "PERIODIC TASK (3): ") (restapi/get-apps VARS/update-apps)) 30)

)
