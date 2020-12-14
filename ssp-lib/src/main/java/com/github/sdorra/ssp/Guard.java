package com.github.sdorra.ssp;

public @interface Guard {

    Class<? extends PermissionGuard> guard();

    String[] guardedPermissions() default {};
}
