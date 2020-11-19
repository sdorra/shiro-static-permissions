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

import com.github.sdorra.shiro.ShiroRule;
import com.github.sdorra.shiro.SubjectAware;
import org.apache.shiro.authz.AuthorizationException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;

/**
 * Unit tests for {@link PermissionCheck}.
 *
 * @author Sebastian Sdorra
 */
@SubjectAware(
  username = "trillian",
  password = "secret",
  configuration = "classpath:com/github/sdorra/ssp/shiro-001.ini"
)
public class PermissionCheckTest {

  @Rule
  public ShiroRule shiro = new ShiroRule();

  /**
   * Tests {@link PermissionCheck#check()}.
   */
  @Test
  public void testCheck() {
    PermissionCheck check = new DefaultPermissionCheck("something:create");
    check.check();
  }

  /**
   * Tests {@link PermissionCheck#check()} without the required permission.
   */
  @Test(expected = AuthorizationException.class)
  public void testCheckWithoutRequiredPermission() {
    PermissionCheck check = new DefaultPermissionCheck("other:create");
    check.check();
  }

  /**
   * Tests {@link PermissionCheck#isPermitted()}.
   */
  @Test
  public void testIsPermitted() {
    assertTrue(new DefaultPermissionCheck("something:create").isPermitted());
    assertTrue(new DefaultPermissionCheck("something:delete").isPermitted());
    assertFalse(new DefaultPermissionCheck("other:delete").isPermitted());
  }

  /**
   * Tests {@link PermissionCheck#asShiroString()}
   */
  @Test
  public void testAsShiroString() {
    assertEquals("something:create", new DefaultPermissionCheck("something:create").asShiroString());
  }

  /**
   * Tests {@link PermissionCheck#toString()}
   */
  @Test
  public void testToString() {
    assertEquals("something:create", new DefaultPermissionCheck("something:create").toString());
  }
}
