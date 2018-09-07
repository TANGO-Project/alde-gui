# ALDE-GUI

&copy; Atos Spain S.A. 2018

The ALDE Graphical User Interface is a component of the European Project TANGO (http://tango-project.eu ).

ALDE-GUI is distributed under a Apache License, version 2.0.

-----------------------

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
- [Leiningen]() 2.0.0 or above installed.

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

-----------------------

### Relation to other TANGO components

This component connects to the **ALDE** REST API interface in order to execute the operations.
