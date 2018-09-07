;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.apps.newp
  (:require [restapi.testbeds :as restapi]
            [reagent.core :as r]
            [gui.globals :as VARS]))

;; https://reagent-project.github.io/
(defn- atom-input-text [value]
  [:input.form-control.input-sm.text-left
    {:type "text" :placeholder "Application Name"
     :value @value
     :on-change #(reset! value (-> % .-target .-value))}])

;; FUNCTION: f-new
(defn f-new ""
  [res]
  (js/alert res))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; WEB CONTENT
;; FUNCTION: panel js/myFunction
(defn panel []
  (let [val  (r/atom "")]
    [:div.collapse {:id "collapseNew"}
      [:div.card.card-body
        [:div.col-sm-12 {:style {:padding "16px" :border-radius "6px" :text-align "center"}}
          ;; header
          [:div.row
            [:div.col-sm-12
              [:h5 {:style {:margin-top "-25px" :text-align "left"}}
                [:span.badge.badge-pill.badge-success "Create new application"]]]]
          ;; content
          [:div.row {:style {:margin-top "5px"}}
            [:label.col-sm-2.control-label.text-right [:b "Name:"]]
            [:div.col-sm-8 [atom-input-text val]]]
          ;; footer
          [:div.row
            [:div.col-sm-9 " "]
            [:div.col-sm-3
              [:h5 {:style {:margin-top "5px" :text-align "left"}}
                ;; save node
                [:button.badge.badge-pill.btn-sm.btn-success {:style {:margin-right "5px" :text-align "right"}
                  :data-toggle "tooltip" :data-placement "right" :title "Submit"
                  :on-click #(restapi/new-application f-new {:name @val})} "Submit"]

                ;; reset
                [:button.badge.badge-pill.btn-sm.btn-secondary {:style {:margin-right "5px" :text-align "right"}
                  :title "Reset fields" :on-click #(reset! val "")} "reset"]

                ;; cancel
                [:button.badge.badge-pill.btn-sm.btn-danger {:title "Cancel operation and close panel" :on-click #(reset! val "")
                  :data-toggle "collapse" :data-target "#collapseNew" :aria-expanded "false" :aria-controls "collapseNew"} "cancel"]]]]]]]))
