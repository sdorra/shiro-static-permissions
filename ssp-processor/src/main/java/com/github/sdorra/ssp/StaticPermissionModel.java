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

  public StaticPermissionModel(String packageName, String className, String type, String permissionObject, Iterable<Action> permissions, Iterable<Action> globalPermissions) {
    this.packageName = packageName;
    this.className = className;
    this.type = type;
    this.permissionObject = permissionObject;
    this.permissions = permissions;
    this.globalPermissions = globalPermissions;
  }

  public String getFullClassName(){
    return packageName.concat(".").concat(className);
  }
  
  public String getPackageName() {
    return packageName;
  }
  
  public String getClassName() {
    return className;
  }

  public String getType() {
    return type;
  }

  public String getPermissionObject() {
    return permissionObject;
  }

  public Iterable<Action> getPermissions() {
    return permissions;
  }

  public Iterable<Action> getGlobalPermissions() {
    return globalPermissions;
  }
  
}
