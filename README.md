# jPlay
_____________

jPlay is [Playwright](https://playwright.dev/java/) based ready to use framework which provides consumers with BDD
syntax sugar and screen play pattern on board. It also has ready to use methods to check css pseudo-elements.
_____________
### Installation:
Java 11 or higher is required.

Maven central: https://mvnrepository.com/artifact/io.github.dantegrek/jplay

Add dependency to pom.xml

```
<dependency>
    <groupId>io.github.dantegrek</groupId>
    <artifactId>jplay</artifactId>
    <version>0.3.0-beta</version>
</dependency>
```

Add dependency to gradle.build

```
implementation group: 'io.github.dantegrek', name: 'jplay', version: '0.3.0-beta'
```

or

```
implementation 'io.github.dantegrek:jplay:0.3.0-beta'
```

jPlay already has playwright inside so you do not need it explicitly.
_____________
#### Topics:

##### [Configuration](/docs/configuration.md) | [User actions](/docs/user_actions.md) | [Asserts](/docs/asserts.md) | [Actions](/docs/actions.md) | [Tasks](/docs/tasks.md) | [Mocks](/docs/mocks) | [Request modifications](docs/request_modifications.md) | [REST API](docs/rest_api.md)
_________
### BDD syntax sugar example:

```
given()
    .browserConfig()
    .withBrowser(BrowserName.CHROME)
    .withHeadless(false)
    .contextConfig()
    .withDevice(Device.IPHONE_12)
    .and()
    .startBrowser()
    .navigateTo("https://google.com");

when()
    .fillText("input:visible", "maven jplay")
    .key(Key.ENTER);

then()
    .expectThat()
    .selector("text=jplay")
    .isVisible();
```
________
Idea of jPlay is build around main actor object. Actor represents a user or better to say tester who configure browsers
performs all actions and tasks and expect some expected results.

Current framework provide consumers with six methods synonyms:

```
given(), when(), then(), user(), actor(), jPlay()
```

All of them return instance of Actor and this Actor is the main object which can navigate user to all framework features
via invocation chain. So you can start wright test, or hooks just to call any of those methods.
__________
### Parallel execution:
jPlay is already built with parallel execution in mind that is why you should not be afraid about static methods in
framework because Actor is wrapped in ThreadLocal inside, that means that each thread will use its own instance of
Actor.
_________
### Possible Issues and recommendations:

If you have long test suite you may face with Firefox or Chrome related OOM or pid occupied issues. It happens
because by default ***maven-surefire-plugin*** reuses forks. Set reuseForks to false will solve the issue. 
This change will make ***maven-surefire-plugin*** to create new fork for each test class.
```
<plugin>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0-M5</version>
    <configuration>
        <reuseForks>false</reuseForks>
    </configuration>
</plugin>
```
____________
## For Contributors

* mvn test - to start unit tests.
* mvn package -P checkJavaDocs - to run tests, build jar and check if all java docks are added.
* mvn deploy -P release - to release to staging repo in Maven Central. (Requires secrets which are configured in github
  release_pipeline.yml)
