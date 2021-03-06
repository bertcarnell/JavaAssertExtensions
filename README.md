JavaAssertExtensions
====================

Adds additional Assert methods to the [JUnit](http://junit.org/) framework

Please see the document contained [here]( http://bertcarnell.github.io/JavaAssertExtensions)

[![Build Status](https://www.travis-ci.org/bertcarnell/JavaAssertExtensions.svg?branch=master)](https://www.travis-ci.org/bertcarnell/JavaAssertExtensions)
[![codecov](https://codecov.io/gh/bertcarnell/JavaAssertExtensions/branch/master/graph/badge.svg)](https://codecov.io/gh/bertcarnell/JavaAssertExtensions)

### Quick start guide for Maven Users
- See the [bertcarnellMavenMicroRepo](https://github.com/bertcarnell/bertcarnellMavenMicroRepo) for directions on how to include this project in your application or library as a dependency
- Import the methods into your Class

```java
import static com.gmail.bertcarnell.assertextensions.ExceptionAssertExtensions.*;
import static com.gmail.bertcarnell.assertextensions.NumericAssertExtensions.*;
```

- Start writing tests using assertThrows

```java
    // check that an exception is thrown in Double.parseDouble("a") using reflection
    assertThrows(NumberFormatException.class, new Double(0), "parseDouble", "a");
    // check that an exception is thrown from a constructor (Double("a")) using reflection
    assertConstuctorThrows(NumberFormatException.class, Double.class.getConstructor(String.class), "a");
    // check that an exception is thrown using a Runnable to enclose the method call
    assertThrows(NumberFormatException.class, new Runnable(){
        @Override
        public void run() {
            Double.parseDouble("a");
        }
    });
    // check that an exception is thrown using a closure that allows for additional checks in the Catch
    assertThrowsAndDoAssertsInCatch(NumberFormatException.class, new ExceptionAssertionsPerformer(){
          @Override
          public void performThrowingAction() {
              Double.parseDouble("a");
          }
          @Override
          public void performAssertionsAfterCatch(Object th) throws Exception {
              // if this metod is called, we know that the object is a NumberFormatException or assignable from NumberFormatException
              NumberFormatException nfe = (NumberFormatException) th;
              assertEquals(nfe.getMessage(), "For input string: \"a\"");
          }
     });
```
- Write tests using Log Relative Error

```java
     // assert that two numbers "agree within 7 digits"
     assertEqualsLRE(1234.5678, 1234.5679, 7);
```

- Check the [JUnit](http://junit.org/) tests for the package to see more [examples](https://github.com/bertcarnell/JavaAssertExtensions/tree/master/AssertExtensions/src/test/java/com/gmail/bertcarnell/assertextensions) of tests that pass when the correct <code>Exception</code> is thrown, tests that fail when the wrong <code>Exception</code> is thrown, and tests that fail when no <code>Exception</code> is thrown.

### Deploy this project to the [bertcarnellMavenMicroRepo](https://github.com/bertcarnell/bertcarnellMavenMicroRepo)

This project deploys artifacts to a local git clone which is pushed to [github.com](https://github.com) for use as a remote repo

In the project's <code>pom.xml</code>:

```xml
<project>
  ...
  <!-- Identify the repository locations where the artifacts will eventually reside -->
  <distributionManagement>
      <!-- Release repository -->
      <repository>
          <!-- this ID will be reflected in the build -->
          <id>repo</id>
          <!-- This is the URL for the github.com project --> 
          <url>https://raw.github.com/bertcarnell/bertcarnellMavenMicroRepo/master/releases</url>
      </repository>
      <!-- Snapshot repository -->
      <snapshotRepository>
          <id>snapshot-repo</id>
          <url>https://raw.github.com/bertcarnell/bertcarnellMavenMicroRepo/master/snapshots</url>
      </snapshotRepository>
  </distributionManagement>
</project>
```

If you are using [Netbeans](https://netbeans.org/), these actions can aid in the deployment.  In the <code>nbactions.xml</code> file:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- Netbeans actions used by right-clicking on the project => Custom => deploy -->
<actions>
        <!-- Deploy snapshots -->
        <action>
            <!-- action name identifying this as custom -->
            <actionName>CUSTOM-deploy</actionName>
            <!-- name that is displayed in the Netbeans context menu -->
            <displayName>deploy</displayName>
            <!-- the goals to be executed -->
            <goals>
                <goal>clean</goal>
                <goal>source:jar</goal>
                <goal>javadoc:jar</goal>
                <goal>deploy</goal>
            </goals>
            <!-- additional maven command line options -->
            <properties>
                <!-- the file path is relative to the project pom.xml.  An absolute path can also be used -->
                <altDeploymentRepository>snapshot-repo::default::file:../../bertcarnellMavenMicroRepo/snapshots</altDeploymentRepository>
            </properties>
        </action>
        <!-- Deploy releases -->
        <action>
            <actionName>CUSTOM-deploy-release</actionName>
            <displayName>deploy-release</displayName>
            <goals>
                <goal>clean</goal>
                <goal>source:jar</goal>
                <goal>javadoc:jar</goal>
                <goal>deploy</goal>
            </goals>
            <properties>
                <altDeploymentRepository>repo::default::file:../../bertcarnellMavenMicroRepo/releases</altDeploymentRepository>
            </properties>
        </action>
    </actions>
```

The artifacts can also be deployed on the command line:

```
mvn clean source:jar javadoc:jar deploy -DaltDeploymentRepository=snapshot-repo::default::file:../../bertcarnellMavenMicroRepo/snapshots
mvn clean source:jar javadoc:jar deploy -DaltDeploymentRepository=repo::default::file:../../bertcarnellMavenMicroRepo/releases
```
