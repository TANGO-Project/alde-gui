;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.breadcrumb.vcontent
  (:require [re-frame.core :as re-frame]
            [gui.subs :as subs]))

;; FUNCTION: panel
(defn panel "Show / generate breadcrumb"
  []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [:div {:style {:margin-top "5px"}}
      [:ol.breadcrumb
        [:li.breadcrumb-item
          [:a {:href "http://www.tango-project.eu/" :title "TANGO Project" :target "_blank"}
            [:img {:src "images/tango_mini.png" :title "TANGO Project"
                   :style {:width "70px" :height "25px" :margin-left "5px"}}]]]
        [:li.breadcrumb-item
          [:a {:href "#" :title "ALDE GUI - Home"} "ALDE-GUI"]]
          (case @active-panel
            :home-panel         [:li.breadcrumb-item.active "Home - Dashboard"]
            :testbeds-panel     [:li.breadcrumb-item.active "Testbeds & Nodes"]
            :apps-panel         [:li.breadcrumb-item.active "Applications & Executables"]
            :execs-panel        [:li.breadcrumb-item.active "Executions"]
            :config-panel       [:li.breadcrumb-item.active "Configuration"]
            [:li.breadcrumb-item.active (str "not-defined" @active-panel)])]]))
