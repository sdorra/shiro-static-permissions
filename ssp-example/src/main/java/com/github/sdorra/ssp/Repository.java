/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.sdorra.ssp;

/**
 *
 * @author Sebastian Sdorra
 */
@StaticPermissions("repositories")
public class Repository implements PermissionObject {

  @Override
  public String getId() {
    return "1";
  }

}
