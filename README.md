JavaAssertExtensions
====================

Adds additional Assert methods to the JUnit implementation

### Deploy to github.com - bertcarnellMavenMicroRepo

This project deploys artifacts to a local git clone which is pushed to github.com for use as a remote repo

In the project's pom.xml:

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

If you are using netbeans, these actions can aid in the deployment.  In the nbactions.xml file:

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
