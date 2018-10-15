;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.common.modal
  (:require [re-frame.core :refer [dispatch subscribe]]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; MODAL:
;; COMPONENT: modal-panel
(defn modal-panel
  [{:keys [child size show?]}]
  [:div.modal-wrapper
    [:div.modal-backdrop
      {:on-click (fn [event]
                  (do
                    (dispatch [:modal {:show? (not show?)
                                       :child nil
                                       :size :default}])
                    (.preventDefault event)
                    (.stopPropagation event)))}]
    [:div.modal-child
      {:style {:width
                (case size
                   :extra-small "15%"
                   :small "30%"
                   :large "70%"
                   :extra-large "85%"
                   "50%")}} child]])


(defn modal []
  (let [modal (subscribe [:modal])]
    (fn []
      [:div
       (if (:show? @modal)
         [modal-panel @modal])])))


(defn close-modal []
  (dispatch [:modal {:show? false :child nil}]))
