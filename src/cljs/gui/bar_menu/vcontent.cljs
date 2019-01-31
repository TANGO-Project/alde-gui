;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.bar-menu.vcontent)

;; FUNCTION: menu
(defn menu "Show / generate top bar menu"
  []
  [:div

    [:div.collapse.navbar-collapse {:id "navbarResponsive"}
      ;; LEFT MENU
      [:ul.navbar-nav.navbar-sidenav {:id "accordion"}
        [:li.nav-item {:data-toggle "tooltip" :data-placement "right" :title "Dashboard"}
          [:a.nav-link {:href "#/home"}
            [:i.fa.fa-fw.fa-home {:style {:color "#FFFFFF"}}]
            [:span.nav-link-text "Dashboard"]]]

        [:li.nav-item {:data-toggle "tooltip" :data-placement "right" :title "Testbeds and Nodes"}
          [:a.nav-link {:href "#/testbeds"}
            [:i.fa.fa-fw.fa-server {:style {:color "#CED8F2"}}]
            [:span.nav-link-text "Testbeds"]]]

        [:li.nav-item {:data-toggle "tooltip" :data-placement "right" :title "Applications and Executables"}
          [:a.nav-link {:href "#/apps"}
            [:i.fa.fa-fw.fa-cogs {:style {:color "#CED8F6"}}]
            [:span.nav-link-text "Applications"]]]

        [:li.nav-item {:data-toggle "tooltip" :data-placement "right" :title "Executions status"}
          [:a.nav-link {:href "#/execs"}
            [:i.fa.fa-fw.fa-chart-pie {:style {:color "#E0E0F8"}}]
            [:span.nav-link-text "Executions / Results"]]]

        [:li.nav-item {:data-toggle "tooltip" :data-placement "right" :title "Configuration"}
          [:a.nav-link {:href "#/config"}
            [:i.fa.fa-fw.fa-wrench {:style {:color "#A4A4A4"}}]
            [:span.nav-link-text "Configuration"]]]]

      ;; Messages
      (comment
      [:ul.navbar-nav {:style {:margin-left "250px" }}
        [:li.nav-item.dropdown
          [:a.nav-link.dropdown-toggle.mr-lg-2 {:id "messagesDropdown" :href "#"
                                                :data-toggle "dropdown" :aria-haspopup "true" :aria-expanded "false"}
            [:i.fa.fa-fw.fa-envelope]
            [:span.d-lg-none "Messages"
              [:span.badge.badge-pill.badge-primary "12 New"]]
            [:span.indicator.text-primary.d-none.d-lg-block
              [:i.fa.fa-fw.fa-circle]]]

          [:div.dropdown-menu {:aria-labelledby "messagesDropdown"}
            [:h6.dropdown-header "New Messages:"]
            [:div.dropdown-divider]
            [:a.dropdown-item {:href "#"}
              [:strong "David Miller"]
              [:span.small.float-right.text-muted "11:21 AM"]
              [:div.dropdown-message.small
                "Hey there! This new version of SB Admin is pretty awesome! ... the sides!"]]

            [:div.dropdown-divider]
            [:a.dropdown-item {:href "#"}
              [:strong "Jane Smith"]
              [:span.small.float-right.text-muted "11:21 AM"]
              [:div.dropdown-message.small
                "Hey there! This new version of SB Admin is pretty awesome! ... the sides!"]]

            [:div.dropdown-divider]
            [:a.dropdown-item.small {:href "#"} "View all messages"]]]

        [:li.nav-item.dropdown
          [:a.nav-link.dropdown-toggle.mr-lg-2 {:id "alertsDropdown" :href "#"
                                                :data-toggle "dropdown" :aria-haspopup "true" :aria-expanded "false"}
            [:i.fa.fa-fw.fa-bell]
            [:span.d-lg-none ">Alerts"
              [:span.badge.badge-pill.badge-warning "6 New"]]
            [:span.indicator.text-warning.d-none.d-lg-block
              [:i.fa.fa-fw.fa-circle]]]

          [:div.dropdown-menu {:aria-labelledby "alertsDropdown"}
            [:h6.dropdown-header "New Alerts:"]
            [:div.dropdown-divider]
            [:a.dropdown-item {:href "#"}
              [:strong
                [:i.fa.fa-long-arrow-up.fa-fw] "Status Update"]
              [:span.small.float-right.text-muted "11:21 AM"]
              [:div.dropdown-message.small
                "This is an automated server response message ... the sides!"]]

            [:div.dropdown-divider]
            [:a.dropdown-item {:href "#"}
              [:strong
                [:i.fa.fa-long-arrow-up.fa-fw] "Status Update"]
              [:span.small.float-right.text-muted "13:55 AM"]
              [:div.dropdown-message.small
                "This is an automated server response message ... the sides!"]]

            [:div.dropdown-divider]
            [:a.dropdown-item.small {:href "#"} "View all messages"]]]]
        )
]])
