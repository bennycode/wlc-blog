Wie ich mit JavaScript arbeite.
How I work with JavaScript.

- Schließen von Tags
- Loslassen von Tag-Minimierung
- Pattern für das Schachteln von Variablen
- Horchen auf DOMContentLoaded, Nutzung von MDN
- Funktions-Auslagerung mit "on"-Naming-Pattern
- Vermeiden von '' und "" und nur "" (weil gängig)
- FileButton hat Selektor (Attribut) und eine Funktion (onFileButtonClick) -> Indiz für eigenen Typ!

# Ausgangsbasis

```html
<!DOCTYPE html>
<html>
  <head>
    <title>Template</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  </head>
  <body>
    <h1>Title</h1>
  </body>
</html>
```

-> Close Tags

# Erstellung eines Datei-Dialogs

Als Erstes fügen wir ein `<input>`-Element vom Typ `file` ein. Die Angabe des Typ-Attributs (`type`) führt dazu, dass der Webbrowser automatisch einen Durchsuchen-Button generiert. 

Beim Klick auf den Button öffnet sich der Datei-Explorer des Betriebssystems und stellt eine Auswahlmöglichkeit bereit. Durch die Angabe `accept="image/*" ` wird die Dateiauswahl durch einen Filter auf Bilddateien begrenzt.

Um mehrere Bilddateien auswählen zu können, kann die Angabe `multiple` verwendet werden, welche eine Mehrfachauswahl ermöglicht. Der Code unseres `<input>`-Elements sieht demnach wie folgt aus:

```js
<input id="file-input" 
       type="file"           
       accept="image/*" 
       multiple
       />
```

Mit HTML5 ist es valide geworden, für Attributangaben eine Kurzschreibweise zu verwenden. Wie im obigen Code-Beispiel zu sehen, hat das Attribut `multiple` keine Wertzuweisung. Die fehlende Wertzuweisung sparrt Tipparbeit. Nach XHTML-Standard ist die Minimierung von Schlüssel-Wert-Paaren aber verboten. 

Aus Gründen der Konsistenz rate ich dazu, auf die minimierte Schreibweise zu verzichten und einen Schlüssel **immer** zusammen mit seinem Wert anzugeben:

```js
<input id="file-input" 
       type="file"           
       accept="image/*" 
       multiple="multiple"
       />
```

...

```js
      var fileInput = "#file-input";
      var fileButton = "#file-button";
```
Okay: 2 Definitionen, 2 Elemente (ab 2 kann geschachtelt werden)

```js
      var selector = {
        fileInput: "#file-input",
        fileButton: "#file-button"
      };
```

Gut: Zwei Layer, 2 Elemente (nicht alphabetisch)

```js
      var selector = {
        fileButton: "#file-button"
        fileInput: "#file-input"
      };
```

Sehr gut: Zwei Layer, 2 Elemente (alphabetisch sortiert)


```js
      var selector = {
        file: {
          button: "#file-button",
          input: "#file-input"
        }
      };
```

Nicht gut: 3 Layers für 2 Elemente. Wir schachteln erst ab 2. Also wäre das ab 3 Elementen gerechtfertigt.

...

```html
<script>
  var selector = {
    fileButton: "#file-button",
    fileInput: "#file-input"
  };

  var element = {
    fileButton: document.querySelector(selector.fileButton),
    fileInput: document.querySelector(selector.fileInput)
  };

  element.fileButton.addEventListener('click', function (event) {
    console.log('Ouch!');
  }, false);
</script>
```

```
Uncaught TypeError: Cannot read property 'addEventListener' of null
```

...

https://developer.mozilla.org/en-US/docs/Web/Events/DOMContentLoaded

```
The DOMContentLoaded event is fired when the document has been completely loaded and parsed, without waiting for stylesheets and images.
```

```html
<script>
  document.addEventListener("DOMContentLoaded", function (event) {
    var selector = {
      fileButton: "#file-button",
      fileInput: "#file-input"
    };

    var element = {
      fileButton: document.querySelector(selector.fileButton),
      fileInput: document.querySelector(selector.fileInput)
    };

    element.fileButton.addEventListener('click', function (event) {
      console.log('Ouch!');
    }, false);
  });
</script>
```

```js
<script>
  var onDOMContentLoaded = function (event) {
    var selector = {
      fileButton: "#file-button",
      fileInput: "#file-input"
    };

    var element = {
      fileButton: document.querySelector(selector.fileButton),
      fileInput: document.querySelector(selector.fileInput)
    };

    element.fileButton.addEventListener('click', function (event) {
      console.log('Ouch!');
    }, false);
  };

  document.addEventListener("DOMContentLoaded", onDOMContentLoaded);
</script>
```


...

```js
      var onDOMContentLoaded = function (event) {
        var selector = {
          fileButton: "#file-button",
          fileInput: "#file-input"
        };

        var element = {
          fileButton: document.querySelector(selector.fileButton),
          fileInput: document.querySelector(selector.fileInput)
        };

        var onFileButtonClick = function (event) {
          console.log('Ouch!');
        };

        element.fileButton.addEventListener('click', onFileButtonClick, false);
      };

      document.addEventListener("DOMContentLoaded", onDOMContentLoaded);
```

Make things obious which are not and name Parameters after their name in the definiton:
https://developer.mozilla.org/en/docs/Web/API/EventTarget.addEventListener

```js
        var useCapture = false;
        element.fileButton.addEventListener('click', onFileButtonClick, useCapture);
```

Implement click:

```js
        var onFileButtonClick = function (event) {
          console.log('Ouch!');
          element.fileInput.click();
        };
```

FileButton kann FileInput als Parameter bekommen. Regel für maximal 2 Parameter einführen. :)

`.click()` kann man faken mit:

```js
        function click(element) {
  var event = document.createEvent('Event');
  event.initEvent('click', true, true);
  element.dispatchEvent(event);
}
```

Auch wichtig: Erwähnen, dass `function(e)` bekloppt ist. Man soll `function(event)` benutzen.

`onchange` vorstellen:

```js
      <input class="file-input" 
             type="file"
             multiple="multiple" 
             accept="image/*" 
             onchange="handleFiles(this.files)"
             data-bind="file_select: upload_files" />
```

Sagen, dass es besser ist, wenn man die Listener im HTML-Element hat anstatt das man die mit JavaScript setzt. Oder ist JavaScript doch besser? Weil man dann im Code besser debuggen kann? Sieht man dann im Inspector, dass die Funktion applied ist? Vielleicht eignet sich hier ein View Binding? Objekt? Prototyp?

---

Prototyp bauen
Singleton zeigen?
In Clojure einschließen
Zeigen, warum ;function() wichtig ist
