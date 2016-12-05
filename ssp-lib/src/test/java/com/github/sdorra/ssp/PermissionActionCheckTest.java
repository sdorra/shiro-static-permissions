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