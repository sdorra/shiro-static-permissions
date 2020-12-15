package com.github.sdorra.ssp;

public class PassThroughPermissionGuard<T extends PermissionObject> implements PermissionGuard<T> {
    @Override
    public PermissionActionCheckInterceptor<T> intercept(String permission) {
        return new PermissionActionCheckInterceptor<T>() {};
    }
}
