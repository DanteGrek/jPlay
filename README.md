# jPlay

jPlay is [Playwright](https://playwright.dev/java/) based ready to use framework which provides consumers with 
BDD syntax sugar and screen play pattern on board. It also has ready to use methods to check css pseudo-elements.

### Installation: 
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
### BDD syntax sugar example:

```
given()
  .browserConfig()
  .withBrowser(BrowserName.CHROME)
  .contextConfig()
  .withDevice(Device)
  .and()
  .startBrowser()
```

## For Contributors

* mvn test - to start unit tests.
* mvn package -P checkJavaDocs - to run tests, build jar and check if all java docks are added.
* mvn deploy -P release - to release to staging repo in Maven Central. (Requires secrets which are configured in github
  release_pipeline.yml)
