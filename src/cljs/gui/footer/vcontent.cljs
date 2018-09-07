;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.footer.vcontent
  (:require [gui.globals :as VARS]
            [utils.logs :as logs]
            [restapi.ping :as ping]
            [re-frame.core :as re-frame]
            [gui.events :as events]
            [gui.subs :as subs]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; f-ping
(defn- f-ping "Ping to testbed"
  [res]
  (logs/debug "ping to alde...")
  (if (nil? res)
    (logs/error "res is NIL!")
    (if (= (compare res 200) 0)
      (re-frame/dispatch [::events/is-rest-api-online? true])
      (re-frame/dispatch [::events/is-rest-api-online? false]))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; WEB CONTENT
;; FUNCTION: menu
(defn menu "Show / generate footer"
  []
  (let [is-rest-api-online? (re-frame/subscribe [::subs/is-rest-api-online?])]
    [:div
      [:a {:href "http://www.tango-project.eu/" :title "TANGO Project" :target "_blank"}
        [:img {:src "images/tango_mini.png" :title "TANGO Project"
               :style {:width "100px" :height "35px" :margin-left "25px"}}]]

      [:small.text-left {:style {:margin-left "75px"}}
        [:i.fa.fa-copyright]
        [:b " ATOS 2018 "] [:i " All right reserved"]]
      [:small.text-right {:style {:margin-left "75px"}}
        (if @is-rest-api-online?
          [:a {:href @VARS/REST_API_URL :title "online"
               :target "_blank"}
              [:i.fa.fa-flag {:style {:color "green"}}]]
          [:a {:href @VARS/REST_API_URL :title "offline"
               :target "_blank"}
              [:i.fa.fa-flag {:style {:color "red"}}]])
        [:span.label.label-muted {:style {:margin-left "10px"}} " ALDE REST-API "]]]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; INIT
(ping/execute-periodic-task #(ping/ping-alde f-ping) 30)
