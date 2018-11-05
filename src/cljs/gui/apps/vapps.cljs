;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license.
;; Please, refer to the LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.apps.vapps
  (:require [gui.apps.graphs :as graphs]
            [reagent.core :as r]
            [gui.subs :as subs]
            [gui.events :as events]
            [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [gui.apps.modal :as modal]
            [dommy.core :refer-macros [sel sel1] :as dom]
            ;; globals
            [gui.globals :as GLOBALS]
            ;; tabs content
            [gui.apps.tabs.applications :as tabs-applications]
            [gui.apps.tabs.executions :as tabs-executions]))


(def TAB_APPS (r/atom true))
(def TAB_EXECUTIONS (r/atom false))

;; apps-tab
(defn- do-func2 ""
  []
  (dom/remove-class! (sel1 :#tab-execs) :active)
  (dom/remove-class! (sel1 :#tab-execs) :show)
  (dom/add-class! (sel1 :#tab-apps) :show)
  (dom/add-class! (sel1 :#tab-apps) :active)
  (reset! TAB_APPS true)
  (reset! TAB_EXECUTIONS false))

;; execs-tab
(defn- do-func3 ""
  []
  ;(reset! GLOBALS/TAB_LOADING true)
  (dom/remove-class! (sel1 :#tab-apps) :active)
  (dom/remove-class! (sel1 :#tab-apps) :show)
  (dom/add-class! (sel1 :#tab-execs) :show)
  (dom/add-class! (sel1 :#tab-execs) :active)
  (reset! TAB_APPS false)
  (reset! TAB_EXECUTIONS true))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;:;;;;;;;;;;;;;;;;;;;;
;; WEB CONTENT
;; FUNCTION:
(defn- panel-component []
  (let [loading (re-frame/subscribe [::subs/loading])]
    [:div {:style {:width "auto"}}

      (when @GLOBALS/TAB_LOADING
        [:div.text-center
          [:img {:src "images/loading.gif" :alt "Loading..." :width "100px" :height "100px"}]])

      [modal/modal]
      ;; TABS
      [:ul.nav.nav-tabs {:id "myTab" :role "tablist"}
        [:li.nav-item
          [:a.nav-link.active {:id "apps-tab" :data-toggle "tab" :href "" :role "tab"
           :on-click #(do-func2)
           :aria-controls "apps" :aria-selected "true"} "Applications"]]
        [:li.nav-item
          [:a.nav-link {:id "execs-tab" :data-toggle "tab" :href "" :role "tab"
           :on-click #(do-func3)
           :aria-controls "execs" :aria-selected "true"} "Executions"]]]

      ;; CONTENT
      [:div.rowtab-content {:style {:margin-top "5px"}}
        ;; APPS
        [:div.tab-pane.fade.show.active {:id "tab-apps" :role "tabpanel" :aria-labelledby "apps-tab"}
          [tabs-applications/panel-component]]
        ;; EXECUTIONS
        [:div.tab-pane.fade {:id "tab-execs" :role "tabpanel" :aria-labelledby "execs-tab"}
          (when @TAB_EXECUTIONS [tabs-executions/panel-component])]]]))


;; FUNCTION:
(defn panel []
  (reagent/create-class  ;; <-- expects a map of functions
    {;; Mounting
     ;:component-will-mount    #(println "component-will-mount")
     :component-did-mount     #(do (graphs/gen-data-nodes))
     ;; Updating
     ;:component-will-update   #(println "component-will-update")
     ;:component-did-update    #(println "component-did-update")
     ;; Unmounting
     ;:component-will-unmount  #(println "component-will-unmount")
     ;; Class Properties
     ;:display-name            "apps-component"  ;; for more helpful warnings & errors
     ;; render
     :reagent-render           #(panel-component)}))
