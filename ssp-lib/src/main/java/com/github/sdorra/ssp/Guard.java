package com.github.sdorra.ssp;

/**
 * A <code>Guard</code> can be used to register an instance of a {@link PermissionGuard},
 * that can create {@link PermissionActionCheckInterceptor} instances for given permissions.
 * A {@link PermissionActionCheckInterceptor} generated this way will be called for each
 * permission check for the corresponding permission.
 */
public @interface Guard {

    /**
     * The concrete implementation of the {@link PermissionGuard} that should be used. The
     * class given here has to have a default constructor.
     *
     * @return class which implements the {@link PermissionGuard}
     */
    Class<? extends PermissionGuard> guard();

    /**
     * Optional list of permissions the guard should be used for. If this is omitted, the
     * guard will be used for every permission no other guard has been explicitly registered
     * for.
     * 
     * @return array of guarded permissions
     */
    String[] guardedPermissions() default {};
}
