package com.github.sdorra.ssp;

/**
 * Implement this interface to create {@link PermissionActionCheckInterceptor} instances that will
 * be used to intercept permission requests.
 * @param <T> The permission object this guard is used for.
 */
public interface PermissionGuard<T extends PermissionObject> {
    PermissionActionCheckInterceptor<T> intercept(String permission);
}
