# User actions

*User actions are wrapped playwright methods with auto-waiting.*
____

- Chainable Methods

  - [Actions with browser](#ActionsWithBrowser) 
    - [navigateTo(String url)](#navigateTo)
    - [goBack()](#goBack())
    - [goForward()](#goForward())
    - [reloadTab()](#reloadTab())
  - [Actions with site](#ActiaonsWithSite)
    - [useStrict(boolean isStrict)](#useStrict)
    - [focus(String selector)](#focus)
    - [click(String selector)](#click)
    - [rightClick(String selector)](#rightClick)
    - [doubleClick(String selector)](#doubleClick)
    - [dragAndDrop(String sourceSelector, String targetSelector)](#dragAndDrop)
    - [check(String selector)](#check)
    - [uncheck(String selector)](#uncheck)
    - [typeText(String selector, String text)](#typeText)
    - [typeTextWithDelay(String selector, String text, double delayTimeout)](#typeTextWithDelay)
    - [fillText(String selector, String text)](#fillText)
    - [tap(String selector)](#tap)
    - [hover(String selector)](#hover)
    - [selectByValue(String selector, String value)](#selectByValue)
    - [selectByValue(String selector, String... values)](#selectByValues)
    - [selectByText(String selector, String label)](#selectByText)
    - [selectByText(String selector, String... labels)](#selectByTexts)
    - [uploadFile(String selector, Path file)](#uploadFile)
    - [uploadFile(String selector, boolean hiddenInput, Path file)](#uploadFileHidden)
    - [uploadFiles(String selector, List<*Path*> files)](#uploadFiles)
    - [clickAndWaitTillDownload(String selector)](#clickAndWaitTillDownload)
    - [clickAndWaitTillDownload(String selector, double timeout)](#clickAndWaitTillDownloadTimeout)
    - [getPseudoElementContent(String selector, String pseudoElement)](#getPseudoElementContent)
  - [Keyboard](#Keyboard)
    - [press(Key key) & pressKey(Key key)](#key)
    - [keyDown(Key key)](#keyDown)
    - [keyUp(Key key)](#keyUp)
    - [insertText(String text)](#insertText)
  - [Dialogs](#Dialogs)
    - [dialog()](#dialog)
    - [acceptAll()](#acceptAll)
    - [acceptOnce()](#acceptOnce)
    - [acceptConfirmOnce()](#acceptConfirmOnce)
    - [acceptAllConfirms()](#acceptAllConfirms)
    - [acceptPromptOnce()](#acceptPromptOnce)
    - [acceptPromptOnce(String text)](#acceptPromptOnceWithText)
    - [acceptAllPrompts()](#acceptAllPrompts)
    - [acceptAllPrompts(String text)](#acceptAllPromptsWithText)
  - [Helpers](#helpers)
    - [setContent(String html)](#setContent)
    - [evaluate(String jsScript)](#evaluate)

___
<h2 id="ActionsWithBrowser">
    Actions with browser
</h2>

<h3 id="navigateTo">
    navigateTo(String url)
</h3>

Open url in browser and wait till *WaitUntilState.DOMCONTENTLOADED*

```
    actor()
         .startBrowser()
         .navigateTo("https://google.com");
```

<h3 id="goBack()">
    goBack()
</h3>

Click back browser button.

```
    actor()
        .startBrowser()
        .navigateTo("https://google.com")
        .navigateTo("https://bing.com")
        .goBack();
```

<h3 id="goForward()">
    goForward()
</h3>

Click froward browser button.

```
    actor()
        .startBrowser()
        .navigateTo("https://google.com")
        .navigateTo("https://bing.com")
        .goBack()
        .goForward();
```

<h3 id="reloadTab()">
    reloadTab()
</h3>

Click reload browser button.

```
    actor()
        .startBrowser()
        .navigateTo("https://google.com")
        .reloadTab();
```
___

<h2 id="ActionsWithSite">
    Actions with site
</h2>

<h3 id="useStrict">
    useStrict(boolean isStrict)
</h3>

Use strict make all user actions like clicks, hover... expects that selector resolves to single element 
otherwise it will fail.

```
    given()
        .startBrowser()
        .navigateTo("https://google.com");
    when()
        .useStrict(true)
        .focus("input"); // throws exeption because more than one input element found.
```
 
```
    when()
        .useStrict(true)
        .focus("input:visible"); // works fine because only one visible input found.
```

<h3 id="focus">
    focus(String selector)
</h3>

Focus on web element.

```
    actor()
        .navigateTo("https://google.com");
        .click("input:visible");
```

<h3 id="click">
    click(String selector)
</h3>

Click on element.

```
    actor()
        .navigateTo("https://google.com");
        .click("input:visible");
```

<h3 id="rightClick">
    rightClick(String selector)
</h3>

Right button click on element.

```
     actor()
        .navigateTo("https://google.com");
        .rightClick("input:visible");   
```

<h3 id="doubleClick">
    doubleClick(String selector)
</h3>

Double button click on element.

```
    when()
        .doubleClick("button");
```

<h3 id="dragAndDrop">
    dragAndDrop(String sourceSelector, String targetSelector)
</h3>

Drag and drop element to another element.

```
    when()
        .dragAndDrop("#dragable", "#targetArea");
```

<h3 id="check">
    check(String selector)
</h3>

Check checkbox will work if checkbox is not checked yet, if it is checked, method will not have effect. 

```
    when()
        .check("#checkBoxId");
```

<h3 id="uncheck">
    uncheck(String selector)
</h3>

Uncheck checkbox will work if checkbox is already checked, but if checkbox is not marked method will not have effect.

```
    when()
        .uncheck("#checkBoxId");
```

<h3 id="typeText">
    typeText(String selector, String text)
</h3>

This method type chars into field one by one.

```
    when()
        .typeText("#password", "SuperPassw0rd!"); 
```

<h3 id="typeTextWithDelay">
    typeTextWithDelay(String selector, String text, double delayTimeout)
</h3>

This method type chars into field one by one with delay between each symbol.

<h3 id="fillText">
    fillText(String selector, String text)
</h3>

This method puts text in to field.

```
    when()
        .fillText("#password", "SuperPassw0rd!"); 
```

<h3 id="tap">
    tap(String selector)
</h3>

This method taps an element matching selector by performing the following steps:
Find an element matching selector. If there is none, wait until a matching element is attached to the DOM.
Wait for actionability checks on the matched element, unless force option is set. If the element is detached during the checks, the whole action is retried.
Scroll the element into view if needed. 
Use Page.touchscreen() to tap the center of the element, or the specified position.
Wait for initiated navigations to either succeed or fail, unless noWaitAfter option is set.

```
    actor()
        .contextConfig()
        .withDevice(Device.IPHONE_11_PRO)
        .and()
        .startBrowser()
        .navigateTo("https://google.com")
        .tap("input:visible");
```

<h3 id="hover">
    hover(String selector)
</h3>

This method performs mouse over.

```
    when()
        .hover("#menuIcon");
```

<h3 id="selectByValue">
    selectByValue(String selector, String value)
</h3>

Single selection matching the value attribute.

```
    given()
        .startBrowser()
        .navigateTo("https://dantegrek.github.io/testautomation-playground/forms.html")
        .waitTillNetworkIdle();
    when()
        .selectByValue("#select_tool", "sel");
```

<h3 id="selectByValues">
    selectByValue(String selector, String... values)
</h3>

Select multiple items.

```
    given()
        .startBrowser()
        .navigateTo("https://dantegrek.github.io/testautomation-playground/forms.html")
        .waitTillNetworkIdle();
    when()
        .selectByValue("#select_lang", "java", "javascript");
```

<h3 id="selectByText">
    selectByText(String selector, String label)
</h3>

Single selection matching the label/text.

```
    given()
        .startBrowser()
        .navigateTo("https://dantegrek.github.io/testautomation-playground/forms.html")
        .waitTillNetworkIdle();
    when()
        .selectByText("#select_tool", "Selenium"); // just joke better use jPlay
```

<h3 id="selectByTexts">
    selectByText(String selector, String... labels)
</h3>

Select multiple element matching the labels/texts.

```
    given()
        .startBrowser()
        .navigateTo("https://dantegrek.github.io/testautomation-playground/forms.html")
        .waitTillNetworkIdle();
    when()
        .selectByText("#select_lang", "Java", "JavaScript");
```

<h3 id="uploadFile">
    uploadFile(String selector, Path file)
</h3>

This method wait till upload file input visible, then scroll into view if needed and sets value.
Sets the value of the file input to these file paths or files. 
If some filePaths are relative paths, then they are resolved relative to
the current working directory. For empty array, clears the selected files.

```  
  when()
      .uploadFile("#upload_cv", Paths.get("src", "test", "resources", "fileToUpload.txt"));
```

<h3 id="uploadFileHidden">
    uploadFile(String selector, boolean hiddenInput, Path file)
</h3>

This method is useful when input field which is used to upload file is not visible and clickable.
Parameter ***hiddenInput*** if false wait till input visible and scroll if needed, true wait till input attached to the dom.

<h3 id="uploadFiles"></h3>

###uploadFiles(String selector, List<*Path*> files)

To upload multiple files at once. This method is overloaded:
* uploadFiles(String selector, boolean hiddenInput, List<*Path*> files)
* uploadFiles(String selector, Path... files)
* uploadFiles(String selector, boolean hiddenInput, Path... files)

<h3 id="clickAndWaitTillDownload">
    clickAndWaitTillDownload(String selector)
</h3>

Click on button and wait default playwright timeout till file will be downloaded.

```
  when()
      .clickAndWaitTillDownload("#downloadFile");
```

<h3 id="clickAndWaitTillDownloadTimeout">
    clickAndWaitTillDownload(String selector, double timeout)
</h3>

Click on button and wait till file will be downloaded with in custom timeout.

```
  when()
      .clickAndWaitTillDownload("#downloadFile", 1000);
```

<h3 id="getPseudoElementContent">
    getPseudoElementContent(String selector, String pseudoElement)
</h3>

This method helps extract content from pseudo-element, helps in tricky cases.

```
  String content = actor()
      .getPseudoElementContent(".star-rating", "::after");
```
___
<h2 id="Keyboard">
    Keyboard
</h2>

<h3 id="key">
    press(Key key) & pressKey(Key key)
</h3>

Performs key down and key up. All predefined keyboard you can find in [Key](/src/main/java/io/github/dantegrek/enums/Key.java) enum

```
  when()
      .click("#area")
      .press(DIGIT_0);
```
or 
```
  when()
      .click("#area")
      .pressKey(DIGIT_0);
```
or with dilay between press and release:
```
when()
      .click("#area")
      .pressKeyWithDelay(DIGIT_0, 1000); //one second delay.
```

<h3 id="keyDown">
    keyDown(Key key)
</h3>

Dispatches a keydown event.
After the key is pressed once, subsequent calls to keyDown(key) will have repeat set to true. 
To release the key, use .

```
  when()
      .click("#area")
      .keyDown(SHIFT);
```

<h3 id="keyUp">
    keyUp(Key key)
</h3>

Dispatches a keyup event.

To simulate typing with holding shift button.
```
  when()
      .click("#area")
      .keyDown(SHIFT);
  and()
     .key(KEY_A)
     .keyUp(SHIFT);
```

<h3 id="insertText">
    insertText(String text)
</h3>

Dispatches only input event, does not emit the keydown, keyup or keypress events.
Emulate exotic characters from keyboard like "嗨"

```
  when()
      .click("#area")
      .insertText("嗨");
```
___
<h2 id="Dialogs">
    Dialogs
</h2>

This dialog invocation chain represents wrapped [playwright Dialog class](https://playwright.dev/java/docs/api/class-dialog)

*Dialogs are dismissed automatically, unless there is a Page.onDialog(handler) listener. When listener is present, 
it must either Dialog.accept([promptText]) or Dialog.dismiss() the dialog - otherwise the page will freeze waiting for 
the dialog, and actions like click will never finish.*

<h3 id="dialog">
    dialog()
</h3>

Opens chain to handle dialogs like: alert, beforeunload, confirm or prompt.

```
  when()
      .dialog()
      .acceptAll();
```

<h3 id="acceptAll">
    acceptAll()
</h3>

Accept all dialogs in current page/tab. By default all dialogs dismiss automatically.


<h3 id="acceptOnce">
    acceptOnce()
</h3>

Accept any Dialog once in current page/tab, and rest of dialogs will be dismissed.

<h3 id="acceptConfirmOnce">
    acceptConfirmOnce()
</h3>

Accept Confirm dialog once.

<h3 id="acceptAllConfirms">
    acceptAllConfirms()
</h3>

Accept all Confirm dialogs in current page/tab.

<h3 id="acceptPromptOnce">
    acceptPromptOnce()
</h3>

Accept Prompt dialog once.

<h3 id="acceptPromptOnceWithText">
    acceptPromptOnce(String text)
</h3>

Put answer and accept Prompt dialog once.

<h3 id="acceptAllPrompts">
    acceptAllPrompts()
</h3>

Accept all prompts with default value or without any value.

<h3 id="acceptAllPromptsWithText">
    acceptAllPrompts(String text)
</h3>

Accept all prompts with text
___

<h2 id="helpers">
    Helpers
</h2>

<h3 id="setContent">
    setContent(String html)
</h3>

Set content in tab. 

```
  String html =
                "<!DOCTYPE html>" +
                "<html>" +
                    "<head>" +
                        "<button>jPlay</button>" +
                    "</head>" +
                "</html>";

  given()
      .startBrowser()
      .setContent(html);
```

<h3 id="evaluate">
    evaluate(String jsScript)
</h3>

Evaluate performs js in browser.

```
  when()
      .evaluate("confirm('Are you here?');");
```

or

```
  when()
      .evaluate("([text]) => console.log(text)", List.of("Are you here?"))
```
