# JBoss Tools BrowserSim Application 

## Summary

JBoss Tools BrowserSim application is a single jar, that can be executed as a standalone Java application.

See [Browsersim FAQ](http://tools.jboss.org/documentation/faq/browsersim.html) for details.

## Building JBoss Tools Standalone BrowserSim application

To build standalone version of _JBoss Tools Browser Simulator_ requires specific versions of Java (1.6+), Ant (1.5+) and Maven (3.1+). See this [link](https://github.com/jbosstools/jbosstools-devdoc/blob/master/building/readme.md) for more information on how to setup, run and configure build.

To run Browsersim Standalone build use the following command from the *products* folder:

    $ mvn clean package

After build passed, standalone Browsersim jar can be found into *browsersim-standalone/target/application* folder. To run it use the following command:
* Windows, Linux:

        java -jar browsersim.jar
		
* Mac OS:

        java -XstartOnFirstThread -jar browsersim.jar

IMPORTANT: To run standalone Browsersim on Linux with specific GTK version add `SWT_GTK3=1` (GTK 3) / `SWT_GTK3=0` (GTK 2) before the run command.