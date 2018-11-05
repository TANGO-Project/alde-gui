;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license.
;; Please, refer to the LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.home.vcontent
  (:require [re-frame.core :as re-frame]
            [gui.events :as events]
            [gui.globals :as VARS]
            [gui.subs :as subs]
            [utils.logs :as logs]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; f-testbeds
(defn- f-testbeds "Parse testbeds result"
  [res]
  (if (nil? res)
    (logs/error "res is NIL!")
    (let [res-testbeds (into [] (concat (res :objects) @VARS/ALDE_TESTBEDS))]
      (re-frame/dispatch [::events/set-testbeds (for [x res-testbeds]
                                                  ;; li
                                                  ^{:key (str (x :name) 0)}
                                                  [:li.list-group-item {:id (x :name)}
                                                    ;; li - button 1
                                                    ^{:key (str (x :name) 1)}
                                                    [:span.badge.badge-pill.badge-secondary {:style {:margin-right "15px"}} (x :name)]
                                                    " "
                                                    (if (x :on_line)
                                                      [:a {:href "/#" :title "online"}
                                                          [:i.fa.fa-flag {:style {:color "green"}}]]
                                                      [:a {:href "/#" :title "offline"}
                                                          [:i.fa.fa-flag {:style {:color "red"}}]])
                                                    " "
                                                    ;; TODO delete node with id = 0
                                                    (str (apply dissoc x [:name :id :on_line :edges]))
                                                  ])]))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; WEB CONTENT

;; PANEL / CONTENT
(defn panel []
  (let [l-testbeds  (re-frame/subscribe [::subs/testbeds])
        t-testbeds  (re-frame/subscribe [::subs/total-testbeds])
        t-nodes     (re-frame/subscribe [::subs/total-nodes])
        t-apps      (re-frame/subscribe [::subs/total-apps])]
    [:div.container.col-md-12 {:style {:margin-bottom "15px"}}
        [:div.row.col-md-12.text-center
          [:div.col-sm-4
            [:div.card.text-center
              [:img.card-img-top {:src "images/network_mini.png" :alt "Testbeds managed by ALDE"}]
              [:div.card-body
                [:h4.card-title "Testbeds " "[" [:b (str @t-testbeds)] "]"]]
              [:div.card-footer
                [:a.btn.btn-primary {:style {:width "100%"} :href "#/testbeds"} "View testbeds"]]]]

          [:div.col-sm-4
            [:div.card.text-center.text-white.bg-warning.mb-3
              [:img.card-img-top {:src "images/node3_mini.png" :alt "Nodes managed by ALDE"}]
              [:div.card-body
                [:h4.card-title "Nodes " "[" [:b (str @t-nodes)] "]"]]
              [:div.card-footer
                [:a.btn.btn-primary {:style {:width "100%"} :href "#/testbeds"} "View nodes"]]]]

          [:div.col-sm-4
            [:div.card.text-white.bg-primary.mb-3
              [:img.card-img-top {:src "images/apps_mini.png" :alt "Applications managed by ALDE"}]
              [:div.card-body
                [:h4.card-title "Applications " "[" [:b (str @t-apps)] "]"]]
              [:div.card-footer
                [:a.btn.btn-info {:style {:width "100%"} :href "#/apps"} "View applications"]]]]]]))
