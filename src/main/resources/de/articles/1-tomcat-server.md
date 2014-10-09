<!--*
  title: Tomcat Server

  description: Erklärung wie "Tomcat Server" die Welt verändern können.

  tags: ["Schon gewusst", "Kurztipp", "Tomcat v8.0.12", "Application server"]
*-->

# Tomcat Server starten

Eine kleine Einleitung folgt später...

## Erste Überschrift

Benny testet Code-Rendering:

```java
String webappDirLocation = "src/main/webapp/";
Tomcat tomcat = new Tomcat();

//[wlc:code]The port that we should run on can be set into an environment variable
//Look for that variable and default to 8080 if it isn't there.
String webPort = System.getenv("PORT");
if(webPort == null || webPort.isEmpty()) {
    webPort = "8080";
}
```

## Relative Image

![Ant](image.png)