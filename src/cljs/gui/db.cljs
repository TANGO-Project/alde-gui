;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license.
;; Please, refer to the LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.db)

;; DB with default values
(def default-db
  {:name                            "re-frame"
   ;:active-panel                    :home-panel
   :is-rest-api-online?             false
   :external-apis                   {}
   :clusters                        {}
   :selected-cluster                {}
   :selected-cluster-node           {}
   :selected-node-name              nil
   :selected-cluster-name           "-"
   :selected-app-id                 "-"
   :selected-app                    {}
   :selected-app-type               "-"
   :selected-option-panel-clusters  :view-clusters
   :loading                         false
   :executions                      [:tr]
   :testbeds                        [:li.list-group-item "-empty-"]
   :total-testbeds                  0
   :total-nodes                     0
   :total-apps                      0})
