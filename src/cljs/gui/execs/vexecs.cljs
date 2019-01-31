;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.execs.vexecs
  (:require [reagent.core :as r]
            [gui.subs :as subs]
            [gui.events :as events]
            [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [dommy.core :refer-macros [sel sel1] :as dom]
            ;; globals
            [gui.globals :as GLOBALS]
            ;; tabs content
            [gui.execs.tabs.executions :as tabs-executions]
            [gui.execs.tabs.exec_times :as tabs-exec-times]
            [gui.execs.tabs.exec_running :as tabs-exec-running]
            [gui.execs.tabs.exec_failed :as tabs-exec-failed]
            [gui.execs.tabs.exec_graphs :as tabs-exec-graphs]))

(def TAB_TIMES (r/atom true))
(def TAB_RUNNING (r/atom false))
(def TAB_FAILED (r/atom false))
(def TAB_EXECUTIONS (r/atom false))
(def TAB_GRAPHS (r/atom false))

;; do-func-times
(defn- do-func-times ""
  []
  (dom/remove-class! (sel1 :#tab-execs) :active)
  (dom/remove-class! (sel1 :#tab-execs) :show)
  (dom/remove-class! (sel1 :#tab-running) :active)
  (dom/remove-class! (sel1 :#tab-running) :show)
  (dom/remove-class! (sel1 :#tab-failed) :active)
  (dom/remove-class! (sel1 :#tab-failed) :show)
  ;(dom/remove-class! (sel1 :#tab-graphs) :active)
  ;(dom/remove-class! (sel1 :#tab-graphs) :show)
  (dom/add-class! (sel1 :#tab-times) :show)
  (dom/add-class! (sel1 :#tab-times) :active)
  (reset! TAB_GRAPHS false)
  (reset! TAB_FAILED false)
  (reset! TAB_RUNNING false)
  (reset! TAB_TIMES true)
  (reset! TAB_EXECUTIONS false))

;; do-func-execs
(defn- do-func-execs ""
  []
  (dom/remove-class! (sel1 :#tab-times) :active)
  (dom/remove-class! (sel1 :#tab-times) :show)
  (dom/remove-class! (sel1 :#tab-running) :active)
  (dom/remove-class! (sel1 :#tab-running) :show)
  (dom/remove-class! (sel1 :#tab-failed) :active)
  (dom/remove-class! (sel1 :#tab-failed) :show)
  ;(dom/remove-class! (sel1 :#tab-graphs) :active)
  ;(dom/remove-class! (sel1 :#tab-graphs) :show)
  (dom/add-class! (sel1 :#tab-execs) :show)
  (dom/add-class! (sel1 :#tab-execs) :active)
  (reset! TAB_GRAPHS false)
  (reset! TAB_FAILED false)
  (reset! TAB_RUNNING false)
  (reset! TAB_TIMES false)
  (reset! TAB_EXECUTIONS true))


;; do-func-running
(defn- do-func-running ""
  []
  (dom/remove-class! (sel1 :#tab-times) :active)
  (dom/remove-class! (sel1 :#tab-times) :show)
  (dom/remove-class! (sel1 :#tab-execs) :active)
  (dom/remove-class! (sel1 :#tab-execs) :show)
  (dom/remove-class! (sel1 :#tab-failed) :active)
  (dom/remove-class! (sel1 :#tab-failed) :show)
  ;(dom/remove-class! (sel1 :#tab-graphs) :active)
  ;(dom/remove-class! (sel1 :#tab-graphs) :show)
  (dom/add-class! (sel1 :#tab-running) :show)
  (dom/add-class! (sel1 :#tab-running) :active)
  (reset! TAB_GRAPHS false)
  (reset! TAB_FAILED false)
  (reset! TAB_RUNNING true)
  (reset! TAB_TIMES false)
  (reset! TAB_EXECUTIONS false))


;; do-func-failed
(defn- do-func-failed ""
  []
  (dom/remove-class! (sel1 :#tab-times) :active)
  (dom/remove-class! (sel1 :#tab-times) :show)
  (dom/remove-class! (sel1 :#tab-execs) :active)
  (dom/remove-class! (sel1 :#tab-execs) :show)
  (dom/remove-class! (sel1 :#tab-running) :active)
  (dom/remove-class! (sel1 :#tab-running) :show)
  ;(dom/remove-class! (sel1 :#tab-graphs) :active)
  ;(dom/remove-class! (sel1 :#tab-graphs) :show)
  (dom/add-class! (sel1 :#tab-failed) :show)
  (dom/add-class! (sel1 :#tab-failed) :active)
  (reset! TAB_GRAPHS false)
  (reset! TAB_FAILED true)
  (reset! TAB_RUNNING false)
  (reset! TAB_TIMES false)
  (reset! TAB_EXECUTIONS false))


;; do-func-graphs
(defn- do-func-graphs ""
  []
  (dom/remove-class! (sel1 :#tab-times) :active)
  (dom/remove-class! (sel1 :#tab-times) :show)
  (dom/remove-class! (sel1 :#tab-execs) :active)
  (dom/remove-class! (sel1 :#tab-execs) :show)
  (dom/remove-class! (sel1 :#tab-running) :active)
  (dom/remove-class! (sel1 :#tab-running) :show)
  ;(dom/add-class! (sel1 :#tab-graphs) :show)
  ;(dom/add-class! (sel1 :#tab-graphs) :active)
  (reset! TAB_GRAPHS true)
  (reset! TAB_FAILED false)
  (reset! TAB_RUNNING false)
  (reset! TAB_TIMES false)
  (reset! TAB_EXECUTIONS false))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;:;;;;;;;;;;;;;;;;;;;;
;; WEB CONTENT
;; FUNCTION:
(defn panel []
  (let [loading (re-frame/subscribe [::subs/loading])]
    [:div {:style {:width "auto"}}

      (when @GLOBALS/TAB_LOADING
        [:div.text-center
          [:img {:src "images/loading.gif" :alt "Loading..." :width "100px" :height "100px"}]])
      ;; TABS
      [:ul.nav.nav-tabs {:id "myTab2" :role "tablist"}
        [:li.nav-item
          [:a.nav-link.active {:id "times-tab" :data-toggle "tab" :href "" :role "tab"
           :on-click #(do-func-times)
           :aria-controls "times" :aria-selected "true"}
           [:i.fa.fa-fw.fa-circle {:style {:color "darkgreen"}}] " Completed"]]
        [:li.nav-item
          [:a.nav-link {:id "running-tab" :data-toggle "tab" :href "" :role "tab"
           :on-click #(do-func-running)
           :aria-controls "running" :aria-selected "true"}
           [:i.fa.fa-spin.fa-cog {:style {:color "gray"}}] " Running"]]
        [:li.nav-item
          [:a.nav-link {:id "failed-tab" :data-toggle "tab" :href "" :role "tab"
           :on-click #(do-func-failed)
           :aria-controls "failed" :aria-selected "true"}
           [:i.fa.fa-fw.fa-circle {:style {:color "darkred"}}] " Failed"]]
        ;[:li.nav-item
        ;  [:a.nav-link {:id "execs-tab" :data-toggle "tab" :href "" :role "tab"
        ;   :on-click #(do-func-execs)
        ;   :aria-controls "execs" :aria-selected "true"}
        ;   [:i.fa.fa-fw.fa-table {:style {:color "gray"}}] " All"]]

        ;[:li.nav-item
        ;  [:a.nav-link {:id "graphs-tab" :data-toggle "tab" :href "" :role "tab"
        ;   :on-click #(do-func-graphs)
        ;   :aria-controls "graphs" :aria-selected "true"} "Graphs"]]
      ]

      ;; CONTENT
      [:div.rowtab-content {:style {:margin-top "5px"}}
        ;; TIMES
        [:div.tab-pane {:id "tab-times" :role "tabpanel" :aria-labelledby "times-tab"}
          (when @TAB_TIMES [tabs-exec-times/panel])] ;-component])]
        ;; RUNNING
        [:div.tab-pane.fade {:id "tab-running" :role "tabpanel" :aria-labelledby "running-tab"}
          (when @TAB_RUNNING [tabs-exec-running/panel])]
        ;; FAILED
        [:div.tab-pane.fade {:id "tab-failed" :role "tabpanel" :aria-labelledby "failed-tab"}
          (when @TAB_FAILED [tabs-exec-failed/panel])]
        ;; EXECUTIONS
        [:div.tab-pane.fade {:id "tab-execs" :role "tabpanel" :aria-labelledby "execs-tab"}
          (when @TAB_EXECUTIONS [tabs-executions/panel-component])]
        ;; GRAPHS
        ;[:div.tab-pane.fade {:id "tab-graphs" :role "tabpanel" :aria-labelledby "graphs-tab"}
        ;  (when @TAB_GRAPHS [tabs-exec-graphs/panel-component])]
]]))
