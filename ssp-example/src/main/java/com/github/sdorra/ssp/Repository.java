/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.sdorra.ssp;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;

import java.util.function.BooleanSupplier;

/**
 *
 * @author Sebastian Sdorra
 */
@StaticPermissions(
        value = "repositories",
        custom = true,
        customGlobal = true,
        guards = {
                @Guard(guard = Repository.DeleteGuard.class, guardedPermissions = "delete")
        }
)
public class Repository implements PermissionObject {

  private final String id;

  public Repository(String id) {
    this.id = id;
  }

  @Override
  public String getId() {
    return id;
  }

  public static class DeleteGuard implements PermissionGuard<Repository> {

    @Override
    public PermissionActionCheckInterceptor<Repository> intercept(String permission) {
      return new PermissionActionCheckInterceptor<Repository>() {
        @Override
        public void check(Subject subject, String id, Runnable delegate) {
          if (id.equals("mustNotBeDeleted")) {
            throw new AuthorizationException("this repository must not be deleted");
          } else {
            delegate.run();
          }
        }

        @Override
        public boolean isPermitted(Subject subject, String id, BooleanSupplier delegate) {
          return !id.equals("mustNotBeDeleted") && delegate.getAsBoolean();
        }
      };
    }
  }
}
