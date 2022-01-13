# jPlay
This is wrapper on playwright library which provides syntax sugar and toolkit for screenplay pattern.  

## For Contributors
* mvn test - to start unit tests.
* mvn package -P checkJavaDocs - to run tests, build jar and check if all java docks are added.
* mvn deploy -P release - to release to staging repo in Maven Central. (Requires secrets which are configured in github release_pipeline.yml)
