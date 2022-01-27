# Configuration

- Chainable Methods

    - [timeoutConfig()](###timeoutConfig())
        - [withDefaultNavigationTimeout(double)](###withDefaultNavigationTimeout(double))
        - [withDefaultTimeout(double)](###withDefaultTimeout(double))
        - [withExpectTimeout(double)](###withExpectTimeout(double))
    - [browserConfig()](###browserConfig())
        - [withBrowser(BrowserName)](###withBrowser(BrowserName browserName))
    - [contextConfig()](###contextConfig())
        - [withDevice(Device)](###withDevice(Device))
        - [withTrace(boolean)](###withTrace(boolean))
        - [withTraceNamePrefix(String)](###withTraceNamePrefix(String))
        - [withTraceDir(Path)](###withTraceDir(Path))

________

## Chainable Methods

[timeoutConfig()](###timeoutConfig), [browserConfig()](###browserConfig()) and [contextConfig()](###contextConfig())
can be navigated to from each other.

```
actor()
                .timeoutConfig()
                .browserConfig()
                .contextConfig()
                .timeoutConfig();
```

______

### timeoutConfig()

Timeout config method opens you chain to set navigation, expect and wait on element timeouts

```
    actor()
        .timeoutConfig()
        .withDefaultNavigationTimeout(2000)
        .withDefaultTimeout(1000)
        .withExpectTimeout(1000); // here you can continue with browser config
```

### withDefaultNavigationTimeout(double)

Maximum navigation time in milliseconds This setting will change the default maximum navigation time for the following
methods and related shortcuts:

* actor().goBack()
* actor().goForward()
* actor().navigate()
* actor().reload()
* actor().setContent(html)
* actor().navigateTo(url)
* and next native playwright methods too:
    * page.goBack([options])
    * page.goForward([options])
    * page.navigate(url[, options])
    * page.reload([options])
    * page.setContent(html[, options])
    * page.waitForNavigation([options], callback)
    * page.waitForURL(url[, options])

```
    actor()
        .timeoutConfig()
        .withDefaultNavigationTimeout(2000);
```

### withDefaultTimeout(double)

Maximum time in milliseconds This setting will change the default maximum time for all the methods accepting timeout
option.

```
    actor()
        .timeoutConfig()
        .withDefaultTimeout(1000);
```

### withExpectTimeout(double)

Maximum time in milliseconds This setting will change the default maximum time for all asserts in thread.

```
    actor()
        .timeoutConfig()
        .withExpectTimeout(1000);
```

____

### browserConfig()

Represents
all [playwright browser launch](https://playwright.dev/java/docs/next/api/class-browsertype#browser-type-launch)
options.

```
    actor()
                .browserConfig()
                .withBrowser(BrowserName.CHROME)
                .withHeadless(false)
                .withSlowMo(1000); // and you can continue set lauch options in chain.
```

### withBrowser(BrowserName)

Accepts build in enum with preset of supported browsers. All available options:

```
    actor()
                .browserConfig()
                .withBrowser(BrowserName.CHROME) // need to be installed.
                .withBrowser(BrowserName.MSEDGE) // need to be installed.
                .withBrowser(BrowserName.CHROMIUM)
                .withBrowser(BrowserName.WEBKIT)
                .withBrowser(BrowserName.FIREFOX);
```

To install Chrome and Microsoft EDGE:

```
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install chrome msedge"
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install-deps"
```

More information: https://playwright.dev/java/docs/cli#install-browsers
_____

### contextConfig()

Represents all [new context](https://playwright.dev/java/docs/api/class-browser#browser-new-context) options.

### withDevice(Device)

Device presets:

```
    actor()
        .contextConfig()
        .withDevice(Device.IPHONE_12);
```
### withTrace(boolean)
All started contexts will record trace.
```
    actor()
        .contextConfig()
        .withTrace(true);
```

### withTraceNamePrefix(String)
Trace recording:
By default trace name is '<browser_name>-trace.zip' so you can add a suffix to this default name.

```
    actor()
        .contextConfig()
        .withTrace(true)
        .withTraceNamePrefix("TestName");
```
### withTraceDir(Path)
By default, traces save in target/traces buy you can override default path:
```
    actor()
        .contextConfig()
        .withTrace(true)
        .withTraceDir(Paths.get("report"));
```
_____
Trace will be saved when you close browser or context. In case when you have more than one context in browser
trace will be saver only for active context. To save trace for all contexts close all context explicitly.
Next code snippet saves two traces:
```
    given()
        .contextConfig()
        .withTrace(true);

    when()
        .browserConfig()
        .withBrowser(browserName)
        .and()
        .startBrowser()
        .navigateTo("https://google.com");

    and()
        .createContextAndTab()
        .navigateTo("https://google.com");

    then()
         .contextConfig()
         .withTraceNamePrefix("Context1")
         .andActor()
         .closeCurrentContext()
         .contextConfig()
         .withTraceNamePrefix("Context2")
         .andActor()
         .switchContextByIndex(0)
         .closeBrowser();
```
_________
### and() & andActor()

and() or andActor() will returns chain to Actor.

```
    actor()
        .timeoutConfig()
        .withDefaultNavigationTimeout(2000)
        .and()      // here you can continue in main Actor chain
        .timeoutConfig()
        .withDefaultTimeout(1000)
        .withExpectTimeout(1000)
        .andActor() // here you can continue in main Actor chain
```
