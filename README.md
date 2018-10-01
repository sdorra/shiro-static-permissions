# Shiro Static Permissions
[![Build Status](https://travis-ci.org/sdorra/shiro-static-permissions.svg?branch=master)](https://travis-ci.org/sdorra/shiro-static-permissions)
[![Quality Gates](https://sonarcloud.io/api/project_badges/measure?project=com.github.sdorra%3Assp&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.github.sdorra%3Assp)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.github.sdorra%3Assp&metric=coverage)](https://sonarcloud.io/dashboard?id=com.github.sdorra%3Assp)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=com.github.sdorra%3Assp&metric=sqale_index)](https://sonarcloud.io/dashboard?id=com.github.sdorra%3Assp)
[![JitPack](https://www.jitpack.io/v/sdorra/shiro-static-permissions.svg)](https://www.jitpack.io/#sdorra/shiro-static-permissions)

Shiro Static Permissions consists of two parts an AnnotationProcessor and a small library.
The AnnotationProcessor generates classes for static permission checks with [Apache Shrio](http://shiro.apache.org/)
which allows checks like the following:

```java
// check for repository create permission
if (RepositoryPermissions.create().isPermitted()) {
    // create repository
}

// throw exception if the user lacks create permission
RepositoryPermissions.create().check();

// check for repository 123 delete permission
if (RepositoryPermissions.delete("123").isPermitted()) {
    // delete repository
}

// throw exception if the user lacks delete permission for repository 123
RepositoryPermissions.delete(new Repository("123"));
```

## Usage

```xml
<dependency>
    <groupId>com.github.sdorra</groupId>
    <artifactId>ssp-lib</artifactId>
    <version>x.y.z</version>
</dependency>

<dependency>
    <groupId>com.github.sdorra</groupId>
    <artifactId>ssp-processor</artifactId>
    <version>x.y.z</version>
    <optional>true</optional>
</dependency>
```

```java
@StaticPermissions("repositories")
public class Repository implements PermissionObject {

  private final String id;
  
  public Repository(String id) {
    this.id = id;
  }
  
  @Override
  public String getId() {
    return id;
  }

}
```

## Real world example

[SCM-Manager](https://bitbucket.org/sdorra/scm-manager):

* Annotation: [`User`](https://bitbucket.org/sdorra/scm-manager/src/2735fa5f6f9602c5cf4b9a09b5637cfc343cb300/scm-core/src/main/java/sonia/scm/user/User.java)
* Permission Check: [`UserManager`](https://bitbucket.org/sdorra/scm-manager/src/a392fe563b9b879e4480de3354e7890ca87a684f/scm-webapp/src/main/java/sonia/scm/user/DefaultUserManager.java#lines-285)
