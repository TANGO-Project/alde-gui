;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.testbeds.vcontent
  (:require [gui.testbeds.vclusters :as clusters]
            [re-frame.core :as re-frame]
            [gui.subs :as subs]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; WEB CONTENT
;; FUNCTION: panel js/myFunction
(defn panel []
  (let [t-testbeds  (re-frame/subscribe [::subs/total-testbeds])]
    [:div.col-12.text-left
      (if  (> @t-testbeds 0)
        [:div.col-sm-12 {:id "clusters-content" :style {:margin-bottom "40px"}}
          [clusters/panel t-testbeds]]
        [:div.col-12.text-left [:img {:src "images/loader.gif" :width "24px" :height "24px"}]
                               [:b "Getting information from testbeds and nodes..."]])]))
