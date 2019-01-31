;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.execs.vcontent
  (:require [gui.execs.vexecs :as vexecs]
            [re-frame.core :as re-frame]
            [gui.subs :as subs]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; WEB CONTENT
;; FUNCTION: panel js/myFunction
(defn panel []
  (let [t-execs      (re-frame/subscribe [::subs/total-execs])]
    [:div.col-12.text-left
      (if  (> @t-execs 0)
        [:div.col-sm-12 {:id "execs-content" :style {:margin-bottom "40px"}}
          [vexecs/panel]]
        [:div.col-12.text-left [:img {:src "images/loader.gif" :width "24px" :height "24px"}]
                               [:b "Getting information from executions..."]])]))
