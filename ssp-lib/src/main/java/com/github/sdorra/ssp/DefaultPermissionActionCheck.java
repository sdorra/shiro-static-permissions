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

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;

/**
 * {@link DefaultPermissionActionCheck} to check multiple objects with the same action type.
 *
 * @author Sebastian Sdorra
 * @param <T> permission object type
 */
public final class DefaultPermissionActionCheck<T extends PermissionObject> extends PermissionActionCheck<T> {

  private final String prefix;
  private final Subject subject;

  /**
   * Constructs a new instance.
   *
   * @param typedAction type of permission action
   */
  public DefaultPermissionActionCheck(String typedAction) {
    this.prefix = typedAction.concat(Constants.SEPARATOR);
    this.subject = SecurityUtils.getSubject();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Checks if the current authenticated user has the permission for the action with the given object id.
   *
   * @param id id of permission object
   *
   * @throws AuthorizationException if current user lacks the required permission
   */
  public void check(String id) {
    subject.checkPermission(asShiroString(id));
  }

  /**
   * Checks if the current authenticated user has the permission for the action with the given object.
   *
   * @param item permission object
   *
   * @throws AuthorizationException if current user lacks the required permission
   */
  public void check(T item) {
    check(item.getId());
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Returns {@code true} if the current authenticated user has the permission for the action with the given object id.
   *
   * @param id id of permission object
   *
   * @return {@code true} if the current authenticated user has the required permission
   */
  public boolean isPermitted(String id) {
    return subject.isPermitted(asShiroString(id));
  }

  /**
   * Returns {@code true} if the current authenticated user has the permission for the action with the given object.
   *
   * @param item permission object
   *
   * @return {@code true} if the current authenticated user has the required permission
   */
  public boolean isPermitted(T item) {
    return isPermitted(item.getId());
  }

  /**
   * Returns the shiro permission string for the given item.
   *
   * @param item permission object
   *
   * @return shiro permission string
   */
  public String asShiroString(T item) {
    return asShiroString(item.getId());
  }

  /**
   * Returns the shiro permission string for the given id.
   *
   * @param id id of permission object
   *
   * @return shiro permission string
   */
  public String asShiroString(String id) {
    return prefix.concat(nullToEmpty(id));
  }

  private String nullToEmpty(String id){
    return id == null ? "" : id;
  }
}
