;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.config.config_restapi
    (:require [reagent.core :as reagent]
              [reagent.core :as r]
              [re-frame.core :as re-frame]
              [gui.events :as events]
              [gui.subs :as subs]
              [utils.logs :as logs]
              [gui.globals :as VARS]
              [restapi.ping :as ping]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; f-ping
(defn- f-ping "Ping to testbed"
  [status res]
  (logs/debug "ping to alde... [status=" status "]")
  (if (nil? res)
    (logs/error "res is NIL!")
    (if (= (compare status 200) 0)
      (re-frame/dispatch [::events/is-rest-api-online? true])
      (re-frame/dispatch [::events/is-rest-api-online? false]))))


;; https://reagent-project.github.io/
(defn atom-input-text [value]
  [:input.form-control.input-sm.text-left
    {:type "text" :placeholder "URL / Endpoint"  :style {:margin-top "10px"}
     :value @value
     :on-change #(reset! value (-> % .-target .-value))}])


;; FUNCTION: panel
(defn panel "Configuration panel"
  []
  (let [val-url (r/atom @VARS/REST_API_URL)
        is-rest-api-online? (re-frame/subscribe [::subs/is-rest-api-online?])]
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
                [:label.col-sm-2.control-label.text-right {:style {:margin-top "15px"}} [:b "Endpoint:"]]
                [:div.col-sm-6
                  [atom-input-text val-url]]
                [:div.col-sm-1
                  (if @is-rest-api-online?
                    [:i.fa.fa-flag {:style {:color "green" :margin-top "15px"}}]
                    [:i.fa.fa-flag {:style {:color "red" :margin-top "15px"}}])
                ]]

              ;; footer
              [:div.row
                [:div.col-sm-9 " "]
                [:div.col-sm-3
                  [:h5 {:style {:margin-top "5px" :text-align "left"}}
                    ;; save node
                    [:button.badge.badge-pill.btn-sm.btn-success {:style {:margin-right "5px" :text-align "right"}
                      :data-toggle "tooltip" :data-placement "right" :title "Submit"
                      :on-click #(do (reset! VARS/REST_API_URL @val-url)
                                     (VARS/set-item! "REST_API_URL" @val-url)
                                     (ping/ping-alde f-ping))} "Submit"]
                    ;; cancel
                    [:button.badge.badge-pill.btn-sm.btn-danger {:title "Cancel operation and close panel"
                      :data-toggle "collapse" :data-target "#collapseEndpoint" :aria-expanded "false" :aria-controls "collapseEndpoint"
                      :on-click #(reset! val-url @VARS/REST_API_URL)} "cancel"]]]]]]]
])))
