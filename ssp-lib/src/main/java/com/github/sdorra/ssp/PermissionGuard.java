package com.github.sdorra.ssp;

public interface PermissionGuard<T extends PermissionObject> {
    PermissionActionCheckInterceptor<T> intercept(String permission);
}
