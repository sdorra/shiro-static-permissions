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
 * Checks a defined shiro permission.
 *
 * @author Sebastian Sdorra
 */
public final class PermissionCheck {

  private final String permission;
  private final PermissionCheckInterceptorWrapper interceptorWrapper;

  /**
   * Constructs a new instance.
   *
   * @param permission permission for the check
   */
  public PermissionCheck(String permission) {
    this(permission, PermissionCheckInterceptorWrapper.none());
  }

  public PermissionCheck(String permission, PermissionCheckInterceptorWrapper interceptorWrapper)
  {
    this.permission = permission;
    this.interceptorWrapper = interceptorWrapper;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Checks if the current authenticated user has the required permission.
   *
   * @throws AuthorizationException if current user lacks the required permission
   */
  public void check() {
    Subject subject = SecurityUtils.getSubject();
    interceptorWrapper.check(subject, () -> subject.checkPermission(permission));
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Returns {@code true} if the current authenticated user has the required permission.
   *
   * @return {@code true} if the current authenticated user has the required permission
   */
  public boolean isPermitted() {
    Subject subject = SecurityUtils.getSubject();
    return interceptorWrapper.isPermitted(subject, () -> subject.isPermitted(permission));
  }

  /**
   * Returns the permission as apache shiro string.
   *
   * @return shiro permission string
   */
  public String asShiroString() {
    return permission;
  }

  @Override
  public String toString() {
    return permission;
  }

}
