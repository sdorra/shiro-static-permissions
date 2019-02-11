/*
 * The MIT License
 *
 * Copyright 2016 Sebastian Sdorra.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.sdorra.ssp;

/**
 * Model which is used as input for the template engine.
 *
 * @author Sebastian Sdorra
 */
public class StaticPermissionModel {

  private final String packageName;
  private final String className;
  private final String type;
  private final String permissionObject;
  private final Iterable<Action> permissions;
  private final Iterable<Action> globalPermissions;
  private final boolean custom;
  private final boolean customGlobal;

  /**
   * Constructs a new instance.
   *
   * @param annotation found {@code StaticPermissions} annotation
   * @param packageName package name for generated class
   * @param className simple name of generated class
   * @param permissionObject class of permission object
   * @param permissions list of item specific permissions
   * @param globalPermissions list of global permissions
   */
  StaticPermissionModel(StaticPermissions annotation, String packageName, String className, String permissionObject,
    Iterable<Action> permissions, Iterable<Action> globalPermissions) {
    this.packageName = packageName;
    this.className = className;
    this.type = annotation.value();
    this.permissionObject = permissionObject;
    this.permissions = permissions;
    this.globalPermissions = globalPermissions;
    this.custom = annotation.custom();
    this.customGlobal = annotation.customGlobal();
  }

  /**
   * Returns the full class name (packageName + className).
   *
   * @return full class name
   */
  public String getFullClassName() {
    return packageName.concat(".").concat(className);
  }

  /**
   * Returns package name for generated class.
   *
   * @return package name
   */
  public String getPackageName() {
    return packageName;
  }

  /**
   * Returns class name for generated class.
   *
   * @return class name
   */
  public String getClassName() {
    return className;
  }

  /**
   * Returns permission type.
   *
   * @return permission type
   */
  public String getType() {
    return type;
  }

  /**
   * Returns class of permission object.
   *
   * @return class of permission object
   */
  public String getPermissionObject() {
    return permissionObject;
  }

  /**
   * Returns list of item specific permissions.
   *
   * @return list of item specific permissions
   */
  public Iterable<Action> getPermissions() {
    return permissions;
  }

  /**
   * Returns list of gobal permissions.
   *
   * @return list of global permissions
   */
  public Iterable<Action> getGlobalPermissions() {
    return globalPermissions;
  }

  /**
   * Return {@code true} if custom permissions are allowed.
   *
   * @return {@code true} if custom permissions are allowed.
   */
  public boolean isCustom() {
    return custom;
  }

  /**
   * Return {@code true} if global custom permissions are allowed.
   *
   * @return {@code true} if global custom permissions are allowed.
   */
  public boolean isCustomGlobal() {
    return customGlobal;
  }
}
