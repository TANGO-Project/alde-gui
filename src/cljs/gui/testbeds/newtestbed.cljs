;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.testbeds.newtestbed
  (:require [restapi.testbeds :as restapi]
            [reagent.core :as r]
            [gui.globals :as VARS]
            [gui.common.modal :as modal]))


;; JSON_NEW_TESTBED
(def JSON_NEW_TESTBED (r/atom "{
  \"name\": \"example-testbed\",
  \"on_line\": true,
  \"category\": \"SLURM\",
  \"protocol\": \"SSH\",
  \"endpoint\": \"ns54\",
  \"extra_config\": {
    \"enqueue_compss_sc_cfg\": \"nova.cfg\",
    \"enqueue_env_file\": \"/home_nfs/home_ejarquej/installations/rc1707/COMPSs/compssenv\"
  },
  \"package_formats\": [\"SINGULARITY\"]
}"))

;; textarea RESPONSE
(def last-resp (r/atom ""))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; FUNCTION: change-val
(defn- change-val ""
  [val event]
  (do
    (reset! val (-> event .-target .-value))
    (reset! JSON_NEW_TESTBED (-> event .-target .-value))))


;; FUNCTION: atom-input-text
(defn atom-input-text [value]
  [:textarea.form-control.input-sm.text-left
    {:type "text" :rows "14" :placeholder "json content" :style {:background-color "#FFFFFC"}
     :value @value
     :on-change #(reset! value (-> % .-target .-value))}])


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; WEB CONTENT
;; FUNCTION: panel js/myFunction
(defn panel []
  (reset! last-resp "")
  [:div {:style {:border-radius "1px" :text-align "center"}}
  (let [val-json (r/atom @JSON_NEW_TESTBED)]
    [:div.modal_new_testbed ; {:style {:background-image "./images/tango.png"}} ;.card-body
      [:div.col-sm-12
        ;; header
        [:div.row
          [:div.col-sm-12
            [:h5 {:style {:margin-top "0px" :text-align "left"}}
              [:span.badge.badge-pill.badge-success "Create new testbed"]]]]
        ;; content
        [:div.row {:style {:margin-top "5px"}}
          [:label.col-sm-2.control-label.text-right [:b "Testbed:"]]
          [:div.col-sm-10
            [atom-input-text val-json]]]
        ;; footer buttons
        [:div.row
          [:div.col-sm-8 " "]
          [:div.col-sm-4
            [:h5 {:style {:margin-top "5px" :text-align "left"}}
              ;; save node TODO
              [:button.badge.badge-pill.btn-sm.btn-success {:style {:margin-right "5px" :text-align "right"}
                :data-toggle "tooltip" :data-placement "right" :title "Submit"
                :on-click #(do (restapi/add-testbed @val-json) (modal/close-modal))} "Submit"]
              ;; cancel
              [:button.badge.badge-pill.btn-sm.btn-danger {:title "Cancel operation and close panel"
                :on-click #(do (reset! last-resp "") (modal/close-modal))} "Cancel / Close"]]]]]])])
