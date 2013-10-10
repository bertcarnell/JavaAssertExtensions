JavaAssertExtensions
====================

Adds additional Assert methods to the JUnit implementation

### Deploy to github.com - bertcarnellMavenMicroRepo

This project deploys artifacts to a local git clone which is pushed to github.com for use as a remote repo

In the project's pom.xml:

```xml
<project>
  ...
  <distributionManagement>
      <repository>
          <id>repo</id>
          <url>https://raw.github.com/bertcarnell/bertcarnellMavenMicroRepo/master/releases</url>
      </repository>
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
<actions>
        <action>
            <actionName>CUSTOM-deploy</actionName>
            <displayName>deploy</displayName>
            <goals>
                <goal>clean</goal>
                <goal>source:jar</goal>
                <goal>javadoc:jar</goal>
                <goal>deploy</goal>
            </goals>
            <properties>
                <altDeploymentRepository>snapshot-repo::default::file:../../bertcarnellMavenMicroRepo/snapshots</altDeploymentRepository>
            </properties>
        </action>
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

