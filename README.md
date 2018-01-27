# web-monitor

Simple automatization of web pages checking using monitoring probes (with Selenium). Loud alarm sound is played if some of the probes reported an error. You should provide your own /src/main/resources/application.properties to configure already provided probes.
You are expected to modify already existing probes or implement your own probes according your needs. 

Homepage of web application displays regularly refreshed state of probes and the log informs about probe runs with success or error result messages.

Application is built on Spring Boot framework. It can be executed with: gradle bootRun 
