;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.home.vcontent
  (:require [restapi.testbeds :as restapi]
            [re-frame.core :as re-frame]
            [gui.events :as events]
            [gui.globals :as VARS]
            [gui.subs :as subs]
            [utils.logs :as logs]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; f-testbeds
(defn- f-testbeds "Parse testbeds result"
  [res]
  ;(logs/info "-------------------------")
  ;(logs/info res)
  ;(logs/info (type res))
  ;(logs/info (res "objects"))
  ;(logs/info (res :objects))
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
  (let [l-testbeds  (re-frame/subscribe [::subs/testbeds])]
    [:div.container.col-md-12 {:style {:margin-bottom "15px"}}
        [:div.row.col-md-12.text-center
          [:div.col-sm-4
            [:div.card.text-center
              [:img.card-img-top {:src "images/network_mini.png" :alt "Testbeds managed by ALDE"}]
              [:div.card-body
                [:h4.card-title "Testbeds " "[" [:b (str (VARS/get-total-testbeds))] "]"]]
              [:div.card-footer
                [:a.btn.btn-primary {:style {:width "100%"} :href "#/testbeds"} "View testbeds"]]]]

          [:div.col-sm-4
            [:div.card.text-center.text-white.bg-warning.mb-3
              [:img.card-img-top {:src "images/node3_mini.png" :alt "Nodes managed by ALDE"}]
              [:div.card-body
                [:h4.card-title "Nodes " "[" [:b (str (VARS/get-total-nodes))] "]"]]
              [:div.card-footer
                [:a.btn.btn-primary {:style {:width "100%"} :href "#/testbeds"} "View nodes"]]]]

          [:div.col-sm-4
            [:div.card.text-white.bg-primary.mb-3
              [:img.card-img-top {:src "images/apps_mini.png" :alt "Applications managed by ALDE"}]
              [:div.card-body
                [:h4.card-title "Applications " "[" [:b (str (VARS/get-total-apps))] "]"]]
              [:div.card-footer
                [:a.btn.btn-info {:style {:width "100%"} :href "#/apps"} "View applications"]]]]]

      [:hr.col-md-4]

      [:div {:style {:font-size "1.125em" :margin-bottom "15px" :text-align "center"}}
        [:a {:title "Show all testbeds" :href "#/home" :on-click #(restapi/get-testbeds f-testbeds) :style {:margin-right "10px"}
             :data-toggle "collapse" :data-target "#collapseListTestbeds" :aria-expanded "false" :aria-controls "collapseListTestbeds"}
          [:span.badge.badge-pill.badge-primary {:style {:font-weight "bold"}} "all testbeds"]]
        [:a {:title "Show online testbeds" :href "#/home" :on-click #(restapi/get-testbeds f-testbeds) :style {:margin-right "10px"}
             :data-toggle "collapse" :data-target "#collapseListTestbeds" :aria-expanded "false" :aria-controls "collapseListTestbeds"}
          [:span.badge.badge-pill.badge-success {:style {:font-weight "bold"}} "online testbeds"]]
        [:a {:title "Show offline testbeds" :href "#/home" :on-click #(restapi/get-testbeds f-testbeds) :style {:margin-right "10px"}
             :data-toggle "collapse" :data-target "#collapseListTestbeds" :aria-expanded "false" :aria-controls "collapseListTestbeds"}
          [:span.badge.badge-pill.badge-danger {:style {:font-weight "bold"}} "offline testbeds"]]]

      [:div.collapse {:id "collapseListTestbeds"}
        [:div.card.card-body
          [:div.col-sm-12 {:style {:padding "16px" :border-radius "6px" :text-align "left"}}
            [:ul.list-group
              @l-testbeds]]]]
    ]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; INIT
(do
  (logs/info "- INIT")
  (logs/info " Getting testbeds from ALDE ...")
  (restapi/get-testbeds VARS/update-testbeds)
  (restapi/get-nodes VARS/update-nodes)
  (restapi/get-apps VARS/update-apps)
  (restapi/execute-periodic-task #(restapi/get-testbeds VARS/update-testbeds) 90)
  (restapi/execute-periodic-task #(restapi/get-nodes VARS/update-nodes) 95)
  (restapi/execute-periodic-task #(restapi/get-apps VARS/update-apps) 30))
