# Shiro Static Permissions

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