DocumentManagementSystem
========================

This is a simple File/Document Management system based only on javax.servlet technology. 


Run application by simply:

**mvn tomcat7:run-war**

Access the application on:

**http://localhost:8888/storage/documents**


**NOTE** Standard **port** for starting the application is **8888**. In case there is another 
process running on **8888**, please configure tomcat's startup port in pom.xml file, section plugins.
