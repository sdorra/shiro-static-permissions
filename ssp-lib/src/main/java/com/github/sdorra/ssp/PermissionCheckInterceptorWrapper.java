package com.github.sdorra.ssp;

import org.apache.shiro.subject.Subject;

import java.util.function.BooleanSupplier;

public abstract class PermissionCheckInterceptorWrapper {

    public abstract void check(Subject subject, Runnable delegate);

    public abstract boolean isPermitted(Subject subject, BooleanSupplier delegate);

    public static PermissionCheckInterceptorWrapper none() {
        return new PermissionCheckInterceptorWrapper() {

            @Override
            public void check(Subject subject, Runnable delegate) {
                delegate.run();
            }

            @Override
            public boolean isPermitted(Subject subject, BooleanSupplier delegate) {
                return delegate.getAsBoolean();
            }
        };
    }

    public static PermissionCheckInterceptorWrapper forRaw(PermissionActionCheckInterceptor<?> interceptor) {
        return new PermissionCheckInterceptorWrapper() {

            @Override
            public void check(Subject subject, Runnable delegate) {
                interceptor.check(subject, delegate);
            }

            @Override
            public boolean isPermitted(Subject subject, BooleanSupplier delegate) {
                return interceptor.isPermitted(subject, delegate);
            }
        };
    }

    public static <T extends PermissionObject> PermissionCheckInterceptorWrapper forItem(T item, PermissionActionCheckInterceptor<T> interceptor) {
        return new PermissionCheckInterceptorWrapper() {

            @Override
            public void check(Subject subject, Runnable delegate) {
                interceptor.check(subject, item, delegate);
            }

            @Override
            public boolean isPermitted(Subject subject, BooleanSupplier delegate) {
                return interceptor.isPermitted(subject, item, delegate);
            }
        };
    }

    public static PermissionCheckInterceptorWrapper forId(String id, PermissionActionCheckInterceptor<?> interceptor) {
        return new PermissionCheckInterceptorWrapper() {

            @Override
            public void check(Subject subject, Runnable delegate) {
                interceptor.check(subject, id, delegate);
            }

            @Override
            public boolean isPermitted(Subject subject, BooleanSupplier delegate) {
                return interceptor.isPermitted(subject, id, delegate);
            }
        };
    }
}
