;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.monitoring.vcontent
  (:require [restapi.testbeds :as restapi]
            [re-frame.core :as re-frame]
            [gui.events :as events]
            [gui.globals :as VARS]
            [gui.subs :as subs]
            [utils.logs :as logs]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TEXTs
(def TXT_MAIN
  "Transparent heterogeneous hardware Architecture deployment for eNergy Gain in Operation")
(def TXT_ALDE
  "ALDE is responsible for the workload scheduling and the management of the application life-cycle while it is executed. ALDE will take the application source code, packetize for different heterogeneous architectures configurations and, if possible, deploy it via a TANGO Device Supervisor and manage the application execution.")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; WEB CONTENT

;; PANEL / CONTENT
(defn panel []
  (let [l-testbeds  (re-frame/subscribe [::subs/testbeds])]
    [:div.container.col-md-12 {:style {:margin-bottom "15px"}}
      [:div.col-md-12.text-center
        [:header.jumbotron
          [:h5.display-4 {:style {:color "#AFAB69" :margin-bottom "25px"}} [:b "Application Lifecycle Deployment Engine (ALDE)"]]
          [:p.lead {:style {:color "#AFAB69" :margin-bottom "25px"}} TXT_ALDE]]]

      [:hr.col-md-4]

      [:div {:style {:font-size "1.125em" :margin-bottom "15px"}}
        [:a {:title "get list of testbeds" :href "#/home" :on-click #(+ 1 1)
             :data-toggle "tooltip" :data-placement "right"}
          [:span.badge.badge-primary {:style {:font-weight "bold"}} " Get testbeds"]]]

      [:ul.list-group
        @l-testbeds]
    ]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; INIT
