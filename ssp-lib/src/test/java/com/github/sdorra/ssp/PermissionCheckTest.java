/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    PermissionCheck check = new PermissionCheck("something:create");
    check.check();
  }
  
  /**
   * Tests {@link PermissionCheck#check()} without the required permission.
   */
  @Test(expected = AuthorizationException.class)
  public void testCheckWithoutRequiredPermission() {
    PermissionCheck check = new PermissionCheck("other:create");
    check.check();
  }
  
  /**
   * Tests {@link PermissionCheck#isPermitted()}.
   */
  @Test
  public void testIsPermitted() {
    assertTrue(new PermissionCheck("something:create").isPermitted());
    assertTrue(new PermissionCheck("something:delete").isPermitted());
    assertFalse(new PermissionCheck("other:delete").isPermitted());
  }

}