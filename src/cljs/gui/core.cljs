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
            [utils.logs :as logs]))


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
