# JBoss Tools BrowserSim Application 

## Summary

JBoss Tools BrowserSim application is a single jar, that can be executed as a standalone Java application.

See [Browsersim FAQ](http://tools.jboss.org/documentation/faq/browsersim.html) for details.

## Running Standalone BrowserSim application

To run standalone BrowserSim use the following command:
* Windows, Linux:

        java -jar browsersim.jar
		
* Mac OS:

        java -XstartOnFirstThread -jar browsersim.jar

IMPORTANT: To run standalone Browsersim on Linux with specific GTK version add `SWT_GTK3=1` (GTK 3) / `SWT_GTK3=0` (GTK 2) before the run command.
