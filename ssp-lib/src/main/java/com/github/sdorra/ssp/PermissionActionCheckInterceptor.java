package com.github.sdorra.ssp;

import org.apache.shiro.subject.Subject;

import java.util.function.BooleanSupplier;

public interface PermissionActionCheckInterceptor<T extends PermissionObject> {
    default void check(Subject subject, Runnable delegate) {
        delegate.run();
    }

    default void check(Subject subject, String id, Runnable delegate) {
        delegate.run();
    }

    default void check(Subject subject, T item, Runnable delegate) {
        this.check(subject, item.getId(), delegate);
    }

    default boolean isPermitted(Subject subject, BooleanSupplier delegate) {
        return delegate.getAsBoolean();
    }

    default boolean isPermitted(Subject subject, String id, BooleanSupplier delegate) {
        return delegate.getAsBoolean();
    }

    default boolean isPermitted(Subject subject, T item, BooleanSupplier delegate) {
        return isPermitted(subject, item.getId(), delegate);
    }
}
