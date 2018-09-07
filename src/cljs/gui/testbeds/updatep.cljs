;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.testbeds.updatep
  (:require [gui.testbeds.graphs :as graphs]
            [restapi.testbeds :as restapi]
            [re-frame.core :as re-frame]
            [gui.subs :as subs]
            [reagent.core :as reagent]
            [gui.globals :as VARS]
            [gui.testbeds.modal :as modal]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; WEB CONTENT
;; FUNCTION: panel js/myFunction
(defn panel []
  [:div.collapse {:id "collapseUpdate2"}
    [:div.card.card-body
      [:div.col-sm-12 {:style {:padding "16px" :border-radius "6px" :text-align "center"}}
        ;; header
        [:div.row
          [:div.col-sm-12
            [:h5 {:style {:margin-top "-25px" :text-align "left"}}
              [:span.badge.badge-pill.badge-warning "Update node"]]]]
        ;; content
        [:div.row {:style {:margin-top "5px"}}
          [:label.col-sm-2.control-label.text-right [:b "Name:"]]
          [:div.col-sm-8
            [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "Name"
              :defaultValue "-"}]]]
        [:div.row {:style {:margin-top "5px"}}
          [:label.col-sm-2.control-label.text-right [:b "Id:"]]
          [:div.col-sm-4
            [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "Id"
              :defaultValue "-"}]]]
        [:div.row {:style {:margin-top "5px"}}
          [:label.col-sm-2.control-label.text-right [:b "Information retrieved?"]]
          [:div.col-sm-4
            [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "Information retrieved?"
              :defaultValue "-"}]]]
        [:div.row {:style {:margin-top "5px"}}
          [:label.col-sm-2.control-label.text-right [:b "Testbed Id:"]]
          [:div.col-sm-4
            [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "Testbed Id"
              :defaultValue "-"}]]]
        [:div.row {:style {:margin-top "5px"}}
          [:label.col-sm-2.control-label.text-right [:b "cpus:"]]
          [:div.col-sm-8
            [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"}
              :defaultValue "-"}]]]
        [:div.row {:style {:margin-top "5px"}}
          [:label.col-sm-2.control-label.text-right [:b "gpus:"]]
          [:div.col-sm-8
            [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"}
              :defaultValue "-"}]]]
        ;; footer
        [:div.row
          [:div.col-sm-9 " "]
          [:div.col-sm-3
            [:h5 {:style {:margin-top "5px" :text-align "left"}}
              ;; save node TODO
              [:button.badge.badge-pill.btn-sm.btn-success {:style {:margin-right "5px" :text-align "right"}
                :data-toggle "tooltip" :data-placement "right" :title "Submit"
                } "Submit"]
              ;; cancel
              [:button.badge.badge-pill.btn-sm.btn-danger {:title "Cancel operation and close panel"
                :data-toggle "collapse" :data-target "#collapseUpdate2" :aria-expanded "false" :aria-controls "collapseUpdate2"} "cancel"]]]]]]])
