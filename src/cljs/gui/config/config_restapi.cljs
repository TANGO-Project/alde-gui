;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.config.config_restapi
    (:require [reagent.core :as reagent]
              [reagent.core :as r]
              [gui.globals :as VARS]))

;; FUNCTION: change-url
(defn- change-url ""
  [url event]
  (do
    (reset! url (-> event .-target .-value))
    (swap! VARS/REST_API_URL assoc-in [:rest-api :url] (-> event .-target .-value))))


;; https://reagent-project.github.io/
(defn atom-input-text [value]
  [:input.form-control.input-sm.text-left
    {:type "text" :placeholder "URL / Endpoint"
     :value @value
     :on-change #(reset! value (-> % .-target .-value))}])

; [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "URL / Endpoint"
;    :value val-url}]


;; FUNCTION: panel
(defn panel "Configuration panel"
  []
  (let [val-url (r/atom @VARS/REST_API_URL)]
    (fn []
      [:div.col-md-12.text-left {:style {:font-size "1.020em" :font-weight "bold" :margin-bottom "15px"}}

        [:h5
          ;; add new node TODO
          [:button.badge.badge-pill.btn-sm.btn-success {:title "ALDE REST API Endpoint"
            :data-toggle "collapse" :data-target "#collapseEndpoint" :aria-expanded "false" :aria-controls "collapseEndpoint"} "ALDE REST API"]]

        [:div.collapse {:id "collapseEndpoint"}
          [:div.card.card-body
            [:div.col-sm-12 {:style {:padding "16px" :border-radius "6px" :text-align "center"}}
              ;; header
              [:div.row
                [:div.col-sm-12
                  [:h5 {:style {:margin-top "-25px" :text-align "left"}}
                    [:span.badge.badge-pill.badge-success "Endpoint configuration"]
                    [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} @VARS/REST_API_URL]]]]
              ;; content
              [:div.row {:style {:margin-top "5px"}}
                [:label.col-sm-2.control-label.text-right [:b "Endpoint:"]]
                [:div.col-sm-8
                  [atom-input-text val-url]]]

              ;; footer
              [:div.row
                [:div.col-sm-9 " "]
                [:div.col-sm-3
                  [:h5 {:style {:margin-top "5px" :text-align "left"}}
                    ;; save node TODO
                    [:button.badge.badge-pill.btn-sm.btn-success {:style {:margin-right "5px" :text-align "right"}
                      :data-toggle "tooltip" :data-placement "right" :title "Submit"
                      :on-click #(reset! VARS/REST_API_URL @val-url)} "Submit"]
                    ;; cancel
                    [:button.badge.badge-pill.btn-sm.btn-danger {:title "Cancel operation and close panel"
                      :data-toggle "collapse" :data-target "#collapseEndpoint" :aria-expanded "false" :aria-controls "collapseEndpoint"
                      :on-click #(reset! val-url @VARS/REST_API_URL)} "cancel"]]]]]]]
])))
