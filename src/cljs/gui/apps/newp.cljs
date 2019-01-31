;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.apps.newp
  (:require [dommy.core :refer-macros [sel sel1] :as dom]
            [restapi.testbeds :as restapi]
            [reagent.core :as r]
            [gui.globals :as VARS]
            [utils.logs :as logs]
            [gui.common.modal :as modal]))


;; textarea RESPONSE
(def last-resp (r/atom ""))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; DYNAMIC HTML CONTENT: atom-input-text
;; https://reagent-project.github.io/
(defn- atom-input-text [value]
  [:input.form-control.input-sm.text-left
    {:type "text" :placeholder "Application Name"
     :value @value
     :on-change #(reset! value (-> % .-target .-value))}])


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; WEB CONTENT
;; FUNCTION: panel
(defn panel []
  (reset! last-resp "")
  [:div.modal_exec {:style {:padding "16px" :border-radius "6px" :text-align "center"}}
  (let [val  (r/atom "")]
    [:div.card.card-body
      [:div.col-sm-12
        ;; header
        [:div.row
          [:div.col-sm-12
            [:h5 {:style {:margin-top "5px" :text-align "left"}}
              [:span.badge.badge-pill.badge-success "Create new application"]]]]
        ;; content
        [:div.row {:style {:margin-top "5px"}}
          [:label.col-sm-2.control-label.text-right [:b "Name:"]]
          [:div.col-sm-10 [atom-input-text val]]]
        ;; footer buttons
        [:div.row
          [:div.col-sm-7 " "]
          [:div.col-sm-5
            [:h5 {:style {:margin-top "5px" :text-align "left"}}
              ;; save node
              [:button.badge.badge-pill.btn-sm.btn-success {:style {:margin-right "5px" :text-align "right"}
                :data-toggle "tooltip" :data-placement "right" :title "Submit"
                :on-click #(do (restapi/new-application (str "{\"name\": \"" @val "\"}")) (modal/close-modal))} "Submit"]
              ;; reset
              [:button.badge.badge-pill.btn-sm.btn-secondary {:style {:margin-right "5px" :text-align "right"}
                :title "Reset fields" :on-click #(reset! val "")} "Reset"]
              ;; cancel
              [:button.badge.badge-pill.btn-sm.btn-danger {:title "Cancel operation and close panel"
                :on-click #(do (reset! val "") (reset! last-resp "") (modal/close-modal))} "Cancel / Close"]]]]]])])
