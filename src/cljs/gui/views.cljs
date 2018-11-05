;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license.
;; Please, refer to the LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.views
  (:require [re-frame.core :as re-frame]
            [gui.subs :as subs]
            [gui.bar-menu.vcontent :as vbar-menu]
            [gui.breadcrumb.vcontent :as breadcrumb]
            [gui.footer.vcontent :as vfooter]
            [gui.home.vcontent :as vhome]
            [gui.config.vcontent :as vconfig]
            [gui.apps.vcontent :as vapps]
            [gui.testbeds.vcontent :as vtestbeds]))

;; bar-menu
(defn bar-menu-panel [] (vbar-menu/menu))

;; breadcrumb
(defn breadcrumb [] (breadcrumb/panel))

;; footer
(defn footer-panel [] (vfooter/menu))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; main-panel: main content

;; home
(defn- home-panel [] (vhome/panel))

;; config-panel
(defn- config-panel [] (vconfig/panel))

;; testbeds-panel
(defn- testbeds-panel [] (vtestbeds/panel))

;; apps-panel
(defn- apps-panel [] (vapps/panel))

;; default
(defn- default-panel []
  [:div "This is the Default Page..."
   [:div [:a {:href "#/"} "go to Home Page"]]])

;; PANELS Handler
(defn- panels [panel-name]
  (case panel-name
    :home-panel       [home-panel]
    :testbeds-panel   [testbeds-panel]
    :apps-panel       [apps-panel]
    :config-panel     [config-panel]
    :default-panel    [default-panel]
    [home-panel]))

;; show-panel
(defn- show-panel [panel-name]
  [panels panel-name])

;; main-panel
(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [show-panel @active-panel]))
