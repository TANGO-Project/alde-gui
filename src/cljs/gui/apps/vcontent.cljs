;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.apps.vcontent
  (:require [gui.apps.vapps :as apps]
            [re-frame.core :as re-frame]
            [gui.subs :as subs]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; WEB CONTENT
;; FUNCTION: panel js/myFunction
(defn panel []
  (let [t-apps      (re-frame/subscribe [::subs/total-apps])]
    [:div.col-12.text-left
      (if  (> @t-apps 0)
        [:div.col-sm-12 {:id "apps-content" :style {:margin-bottom "40px"}}
          [apps/panel]]
        [:div.col-12.text-left [:img {:src "images/loader.gif" :width "24px" :height "24px"}]
                               [:b "Getting information from applications and executions..."]])]))
