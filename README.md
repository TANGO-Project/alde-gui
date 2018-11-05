# ALDE-GUI

&copy; Atos Spain S.A. 2018

[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0.txt)

The ALDE Graphical User Interface is a component of the European Project TANGO (http://tango-project.eu ).

-----------------------

[LICENSE](#license)

[Description](#description)

[Installation Guide](#installation-guide)

[Usage Guide](#usage-guide)

[Relation to other TANGO components](#relation-to-other-tango-components)

-----------------------

### Description

This web component offers a Graphical User Interface to the [Application Lifecycle Deployment Engine](https://github.com/TANGO-Project/alde) (ALDE).


-----------------------

### Installation Guide
##### 1. Requirements

- Java 8
- [Leiningen](https://leiningen.org/) 2.0.0 or above installed.

##### 2. Install ALDE-GUI

- Download this repository

- Go to project directory and execute the following command:

```bash
lein start
```

- In order to create a runnable _.jar_ and use it to launch the GUI application, execute the following:

```bash
lein gen-jar
java -jar target/alde-gui.jar
```

- By default, the GUI can be accessed in port 3001. But this can be modified by launching the _.jar_ file with an extra parameter:

```bash
lein gen-jar
java -jar target/alde-gui.jar 8080
```

-----------------------

### Usage Guide

- Before starting the web application, go to folder `src/cljs/gui` and modify the ALDE REST API URL:

```clojure
{
  :app {
    ;; GUI properties
    :name "ALDE-GUI"
    :version "0.1.1-SNAPSHOT"
    :alde-rest-api-url "http://xxx.xxx.xxx.xxx:xxx/api/v1/"
    :user "user"
    :pwd "user"
  }
}
```

- The ALDE REST API URL can also be modified in the configuration section of the ALDE GUI.

-----------------------

### Relation to other TANGO components

This component connects to the **ALDE** REST API interface in order to execute the operations.

-----------------------

### LICENSE

Alde-gui is licensed under a [GNU General Public License, version 3](LICENSE.TXT).

This project depends on a set of libraries licensed under the [Eclipse Public License v1](https://www.eclipse.org/legal/epl-v10.html) and the [Eclipse Public License v2](https://www.eclipse.org/legal/epl-v20.html). These libraries are imported through [leiningen](https://leiningen.org/) or [maven](https://maven.apache.org/), and they don't belong to the distribution of this project. This means that the original source code of these libraries was not modified. These libraries are the following:


| Library                    | Version               | Project URL                            |
|----------------------------|-----------------------|----------------------------------------|
| org.clojure/clojurescript  | 1.10.238              | https://github.com/clojure/clojurescript |
| secretary                  | 1.2.3                 | https://github.com/gf3/secretary |
| compojure                  | 1.5.0                 | https://github.com/weavejester/compojure |
| org.clojure/data.json      | 0.2.6                 | https://github.com/clojure/data.json |
| prismatic/dommy            | 1.1.0                 | https://github.com/plumatic/dommy |
| cljs-http                  | 0.1.44                | https://github.com/r0man/cljs-http |
| com.taoensso/timbre        | 4.10.0                | https://github.com/ptaoussanis/timbre |
| ring-cors/ring-cors        | 0.1.11                | https://github.com/r0man/ring-cors |
| lein-cljsbuild             | 1.1.7                 | https://github.com/emezeske/lein-cljsbuild |
| lein-figwheel              | 0.5.14                | https://github.com/bhauman/lein-figwheel |
