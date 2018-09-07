;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.apps.updatep
  (:require [gui.apps.graphs :as graphs]
            [restapi.testbeds :as restapi]
            [re-frame.core :as re-frame]
            [gui.subs :as subs]
            [reagent.core :as reagent]
            [gui.globals :as VARS]
            [gui.apps.modal :as modal]))


;; FUNCTION: f-update
(defn f-update ""
  [res]
  (js/alert res))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; WEB CONTENT
;; FUNCTION: panel js/myFunction
(defn panel []
  (let [selected-app      (re-frame/subscribe [::subs/selected-app])
        selected-app-id   (re-frame/subscribe [::subs/selected-app-id])]
    (fn []
      [:div.collapse {:id "collapseUpdate"}
        [:div.card.card-body
          [:div.col-sm-12 {:style {:padding "16px" :border-radius "6px" :text-align "center"}}
            ;; header
            [:div.row
              [:div.col-sm-12
                [:h5 {:style {:margin-top "-25px" :text-align "left"}}
                  [:span.badge.badge-pill.badge-warning "Update application / Upload zip file with the source code of the application"]]]]
            ;; content
            [:div.row {:style {:margin-top "5px"}}
              [:label.col-sm-2.control-label.text-right [:b "Name:"]]
              [:div.col-sm-8
                [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFCC"}
                  :value (@selected-app :name)}]]]
            [:div.row {:style {:margin-top "5px"}}
              [:label.col-sm-2.control-label.text-right [:b "Id:"]]
              [:div.col-sm-2
                [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFCC"}
                  :value (@selected-app :id)}]]]
            [:div.row {:style {:margin-top "5px"}}
              [:label.col-sm-2.control-label.text-right [:b "Compilation type"]]
              [:div.col-sm-8
                [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "SINGULARITY:PM  or  SLURM:SBATCH"
                  }]]]
            [:div.row {:style {:margin-top "5px"}}
              [:label.col-sm-2.control-label.text-right [:b "Compilation script:"]]
              [:div.col-sm-8
                [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "compss_build_app Matmul"
                  }]]]
            [:div.row {:style {:margin-top "5px"}}
              [:label.col-sm-2.control-label.text-right [:b "File:"]]
              [:div.col-sm-8
                [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"}
                  }]]]
            ;; footer
            [:div.row
              [:div.col-sm-9 " "]
              [:div.col-sm-3
                [:h5 {:style {:margin-top "5px" :text-align "left"}}
                  ;; save node TODO
                  [:button.badge.badge-pill.btn-sm.btn-success {:style {:margin-right "5px" :text-align "right"}
                    :data-toggle "tooltip" :data-placement "right" :title "Submit"
                    :on-click #(restapi/update-application @selected-app-id f-update {:name ""})} "Submit"]

                  ;; cancel
                  [:button.badge.badge-pill.btn-sm.btn-danger {:title "Cancel operation and close panel"
                    :data-toggle "collapse" :data-target "#collapseUpdate" :aria-expanded "false" :aria-controls "collapseUpdate"} "cancel"]]]]]]])))
