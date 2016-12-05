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
 * Unit tests for {@link PermissionActionCheck}.
 *
 * @author Sebastian Sdorra
 */
@SubjectAware(
  username = "trillian",
  password = "secret",
  configuration = "classpath:com/github/sdorra/ssp/shiro-001.ini"
)
public class PermissionActionCheckTest {

  @Rule
  public ShiroRule shiro = new ShiroRule();

  /**
   * Tests {@link PermissionActionCheck#isPermitted(String)} and
   * {@link PermissionActionCheck#isPermitted(PermissionObject)}.
   */
  @Test
  public void testIsPermitted() {
    PermissionActionCheck<Repository> create = new PermissionActionCheck<>("repository:create");
    assertTrue(create.isPermitted("abc"));
    assertTrue(create.isPermitted(new Repository("abc")));
    assertFalse(create.isPermitted("123"));
    assertFalse(create.isPermitted(new Repository("123")));
  }

  /**
   * Tests {@link PermissionActionCheck#check(String)} and {@link PermissionActionCheck#check(PermissionObject)}.
   */
  @Test
  public void testCheck() {
    PermissionActionCheck<Repository> delete = new PermissionActionCheck<>("repository:delete");
    delete.check("123");
    delete.check(new Repository("123"));
  }

  /**
   * Tests {@link PermissionActionCheck#check(String)} with invalid id.
   */
  @Test(expected = AuthorizationException.class)
  public void testCheckWithInvalidId() {
    PermissionActionCheck<Repository> delete = new PermissionActionCheck<>("repository:delete");
    delete.check("abc");
  }

  /**
   * Tests {@link PermissionActionCheck#check(String)} with invalid item.
   */
  @Test(expected = AuthorizationException.class)
  public void testCheckWithInvalidItem() {
    PermissionActionCheck<Repository> delete = new PermissionActionCheck<>("repository:delete");
    delete.check(new Repository("abc"));
  }

  private static class Repository implements PermissionObject {

    private final String id;

    private Repository(String id) {
      this.id = id;
    }

    @Override
    public String getId() {
      return id;
    }

  }

}
