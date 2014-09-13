## JSF damals und heute

Mit Standard JSF-Tags:

```xhtml
<?xml version='1.0' encoding='UTF-8' ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html">
  <h:head>
    <title>Facelet Title</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <h:outputStylesheet library="css" name="style.css" />
    <h:outputScript library="js" name="script.js" />
  </h:head>
  <h:body>
    <h:panelGroup id="content" layout="block">
      <h:outputText value="Hello from Facelets" />  
    </h:panelGroup>
  </h:body>
</html>
```

Mit Standard HTML-Tags:

```xhtml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"   
      xmlns:jsf="http://xmlns.jcp.org/jsf">
  <head jsf:id="head">
    <title>Facelet Title</title>
    <meta charset="UTF-8" />
    <link jsf:library="css" jsf:name="style.css" />
    <script jsf:library="js" jsf:name="script.js" jsf:target="head" />
  </head>
  <body jsf:id="body">
    <div id="content">
      Hello from Facelets
    </div>
  </body>
</html>

```