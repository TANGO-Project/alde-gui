;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license. Please, refer to the
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
  [status res]
  (logs/debug "ping to alde... [status=" status "]")
  (if (nil? res)
    (logs/error "res is NIL!")
    (if (= (compare status 200) 0)
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
          [:a {:href (str @VARS/REST_API_URL "testbeds") :title "online"
               :target "_blank"}
              [:i.fa.fa-flag {:style {:color "green"}}]]
          [:a {:href (str @VARS/REST_API_URL "testbeds") :title "offline"
               :target "_blank"}
              [:i.fa.fa-flag {:style {:color "red"}}]])
        [:span.label.label-muted {:style {:margin-left "10px"}}
          " ALDE REST-API "  [:i {:style {:color "blue" :margin-left "25px"}} @VARS/REST_API_URL]]]]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; INIT
(ping/ping-alde f-ping)
(ping/execute-periodic-task #(ping/ping-alde f-ping) 300)
