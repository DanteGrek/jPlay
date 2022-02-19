# Configuration

- Chainable Methods
    - [timeoutConfig()](#timeout-config)
        - [withDefaultNavigationTimeout(double)](#withDefaultNavigationTimeout)
        - [withDefaultTimeout(double)](#withDefaultTimeout(double))
        - [withExpectTimeout(double)](#withExpectTimeout(double))
    - [browserConfig()](#browserConfig())
        - [withBrowser(BrowserName)](#withBrowser(BrowserName browserName))
    - [contextConfig()](#contextConfig())
        - [withDevice(Device)](#withDevice(Device))
        - [withTrace(boolean)](#withTrace(boolean))
        - [withTraceNamePrefix(String)](#withTraceNamePrefix(String))
        - [withTraceDir(Path)](#withTraceDir(Path))
    - [clearConfig()](#clearConfig())
    - [and() & andActor()](#and()&andActor())

________

## Chainable Methods

[timeoutConfig()](#timeout-config), [browserConfig()](###browserConfig()) and [contextConfig()](###contextConfig())
can be navigated to from each other.

```
actor()
                .timeoutConfig()
                .browserConfig()
                .contextConfig()
                .timeoutConfig();
```

______

<h3 id="timeout-config">
    timeoutConfig()
</h3>

Timeout config method opens you chain to set navigation, expect and wait on element timeouts

```
    actor()
        .timeoutConfig()
        .withDefaultNavigationTimeout(2000)
        .withDefaultTimeout(1000)
        .withExpectTimeout(1000); // here you can continue with browser config
```

<h3 id="withDefaultNavigationTimeout">
    withDefaultNavigationTimeout(double)
</h3>

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

<h3 id="withDefaultTimeout(double)">
    withDefaultTimeout(double)
</h3>

Maximum time in milliseconds This setting will change the default maximum time for all the methods accepting timeout
option.

```
    actor()
        .timeoutConfig()
        .withDefaultTimeout(1000);
```

<h3 id="withExpectTimeout(double)">
    withExpectTimeout(double)
</h3>

Maximum time in milliseconds This setting will change the default maximum time for all asserts in thread.

```
    actor()
        .timeoutConfig()
        .withExpectTimeout(1000);
```

____
<h3 id="browserConfig()">
    browserConfig()
</h3>

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
<h3 id="withBrowser(BrowserName)">
    withBrowser(BrowserName)
</h3>

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
<h3 id="contextConfig()">
    contextConfig()
</h3>

Represents all [new context](https://playwright.dev/java/docs/api/class-browser#browser-new-context) options.

Context config will be applied to all new contexts. 
In case you want to avoid use the same config for all new contexts use [clearConfig()](#clearConfig()).

<h3 id="withDevice(Device)">
    withDevice(Device)
</h3>

Device presets:

```
    actor()
        .contextConfig()
        .withDevice(Device.IPHONE_12);
```

<h3 id="withTrace(boolean)">
    withTrace(boolean)
</h3>

All started contexts will record trace.

```
    actor()
        .contextConfig()
        .withTrace(true);
```

<h3 id="withTraceNamePrefix(String)">
    withTraceNamePrefix(String)
</h3>

Trace recording:
By default trace name is '<browser_name>-trace.zip' so you can add a suffix to this default name.

```
    actor()
        .contextConfig()
        .withTrace(true)
        .withTraceNamePrefix("TestName");
```

<h3 id="withTraceDir(Path)">
    withTraceDir(Path)
</h3>

By default, traces save in target/traces buy you can override default path:

```
    actor()
        .contextConfig()
        .withTrace(true)
        .withTraceDir(Paths.get("report"));
```

_____
Trace will be saved when you close browser or context. In case when you have more than one context in browser trace will
be saver only for active context. To save trace for all contexts close all context explicitly. Next code snippet saves
two traces:

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

____
<h3 id="clearConfig()">
    clearConfig()
</h3>

Clear config function will clear all configurations: browser, context, timeouts.

```
    actor()
        .timeoutConfig()
        .withDefaultNavigationTimeout(2000)
        ...
        .clearConfig()
```
You can use this method in the middle of scenario to start new context with different configuration or 
between tests in the same thread or class to avoid sharing config from previous tests.
_________

<h3 id="and()&andActor()">
    and() & andActor()
</h3>

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
