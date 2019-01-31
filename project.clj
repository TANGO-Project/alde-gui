;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defproject uni-gui "0.1.4"
  :description "ALDE GUI"
  :url "-"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.238"]
                 [reagent "0.7.0"]                      ; MIT License ; https://github.com/reagent-project/reagent
                 [re-frame "0.10.2"]                    ; MIT License ; https://github.com/Day8/re-frame
                 [secretary "1.2.3"]                    ; Eclipse Public License ; https://github.com/gf3/secretary
                 [compojure "1.5.0"]                    ; Eclipse Public License ; https://github.com/weavejester/compojure
                 [org.clojure/data.json "0.2.6"]        ; Eclipse Public License 1.0 https://github.com/clojure/data.json
                 ;; env
                 [yogthos/config "1.1.1"]               ; Eclipse Public License ; https://github.com/yogthos/config
                 ;; dom
                 [prismatic/dommy "1.1.0"]              ; Eclipse Public License https://github.com/plumatic/dommy
                 ;; chesire
                 [cheshire "5.8.0"]                     ; MIT License   https://github.com/dakrone/cheshire
                 ;; http clients for CLJ and CLJS
                 [cljs-http "0.1.44"]                   ; Eclipse Public License https://github.com/r0man/cljs-http
                 ;; JS libraries
                 [cljsjs/vis "4.20.1-0"]                ; Graphs; http://visjs.org/#licenses; Vis.js is dual licensed under both Apache 2.0 and MIT.
                 ;; CHARTS
                 [cljsjs/chartjs "2.7.3-0"]             ; MIT License; https://github.com/chartjs/Chart.js/blob/master/LICENSE.md
                 ;; LOGS
                 [com.taoensso/timbre "4.10.0"]         ; Eclipse Public License 1.0; https://github.com/ptaoussanis/timbre
                 ;; RING - SERVER
                 [ring-cors/ring-cors "0.1.11"]         ; Eclipse Public License ; https://github.com/r0man/ring-cors
                 [ring/ring-json "0.4.0"]               ; MIT License ; https://github.com/ring-clojure/ring-json
                 [ring "1.5.1"]]

  :plugins [[lein-cljsbuild "1.1.7"
              :exclusions [[org.clojure/clojure]]]
            [lein-cloverage "1.0.9"]                      ; Eclipse Public License - Version 1.0  https://github.com/cloverage/cloverage ; https://github.com/codecov/example-clojure
            [jonase/eastwood "0.2.4"]                     ; Eclipse Public License "lein eastwood" https://github.com/jonase/eastwood
            [lein-kibit "0.1.5"]]                         ; Eclipse Public License "lein kibit" "lein kibit --replace --interactive"   https://github.com/jonase/kibit]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj" "src/cljs"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  ;; figwheel configuration: https://github.com/bhauman/lein-figwheel
  ;; SERVER options
  :figwheel {:css-dirs        ["resources/public/css"]
             :ring-handler    gui.handler/handler
             :server-port     8081          ;; default is 3449
             ;:server-ip       "0.0.0.0"     ;; default is "localhost"
             ;; if you need to watch files with polling instead of FS events
             ;:hawk-options {:watcher :polling}
             ;; ^ this can be useful in Docker environments
             :server-logfile  "logs/figwheel_server.log"}

  :profiles
    {:dev
      {:dependencies [[binaryage/devtools "0.9.4"]
                      [figwheel-sidecar "0.5.14"]]
       :plugins      [[lein-figwheel "0.5.14"]]}}

  ;; figwheel configuration: https://github.com/bhauman/lein-figwheel
  ;; CLIENT options
  :cljsbuild
    {:builds
      [{:id           "dev"
        :source-paths ["src/cljs"]
          :figwheel  {:on-jsload     "gui.core/mount-root"}
        :compiler     {:main                 gui.core
                       :output-to            "resources/public/js/compiled/app.js"
                       :output-dir           "resources/public/js/compiled/out"
                       :asset-path           "js/compiled/out"
                       :source-map-timestamp true
                       :preloads             [devtools.preload]
                       :external-config      {:devtools/config {:features-to-install :all}}}}
       ; [WARNING! 'min' does not work correctly when executing 'uberjar' command!!!]
       {:id           "min"
        :source-paths ["src/cljs"]
        :jar          true
        :compiler     {:main            gui.core
                       :output-to       "resources/public/js/compiled/app.js"
                       :optimizations   :advanced ;:advanced
                       :closure-defines {goog.DEBUG false}
                       :pretty-print    false}}]}

  :main gui.server

  :aot [gui.server]

  :uberjar-name "alde-gui-0.1.4.jar"

  ;; NOTE: tasks executed with 'uberjar'
  :prep-tasks [["cljsbuild" "once" "dev"] "compile"]

  ;; NOTE: compile and package up your project for deployment with `lein package`
  :aliases {"gen-jar"   ["do" ["clean"] ["uberjar"]]
            "start"     ["do" ["clean"] ["figwheel" "dev"]]
            "package"   ["do" "clean" ["cljsbuild" "once" "min"] "uberjar"]})
