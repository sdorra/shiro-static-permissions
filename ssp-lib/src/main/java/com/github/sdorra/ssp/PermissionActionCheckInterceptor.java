package com.github.sdorra.ssp;

import org.apache.shiro.subject.Subject;

import java.util.function.BooleanSupplier;

/**
 * Instances of this class will be called for each permission request it has been
 * registered for. The default methods of this interface that take instances of
 * {@link PermissionObject}s will simply call the corresponding method with the
 * id of this object, the other methods will delegate to the given delegate parameter
 * calling the original check done by shiro.
 *
 * @param <T> The permission object this guard is used for.
 */
public interface PermissionActionCheckInterceptor<T extends PermissionObject> {

    /**
     * Called for global permission checks that may throw exceptions, when the permission is not granted.
     * The default implementation delegates to the original shiro check by calling
     * <code>delegate.run()</code>.
     * <br>
     * If you override this, you should also override {@link #isPermitted(Subject, BooleanSupplier)}.
     *
     * @param subject  The current subject.
     * @param delegate The original check performed by shiro.
     */
    default void check(Subject subject, Runnable delegate) {
        delegate.run();
    }

    /**
     * Called for permission checks regarding the permission object with the given id that may throw exceptions,
     * when the permission is not granted.
     * The default implementation delegates to the original shiro check by calling
     * <code>delegate.run()</code>.
     * <br>
     * If you override this, you should also override {@link #isPermitted(Subject, String, BooleanSupplier)}.
     *
     * @param subject  The current subject.
     * @param id       The id of the permission object.
     * @param delegate The original check performed by shiro.
     */
    default void check(Subject subject, String id, Runnable delegate) {
        delegate.run();
    }

    /**
     * Called for permission checks regarding the given permission object that may throw exceptions,
     * when the permission is not granted.
     * The default implementation delegates to {@link #check(Subject, String, Runnable)} with the id
     * of the permission object.
     * <br>
     * If you override this, you should also override {@link #isPermitted(Subject, PermissionObject, BooleanSupplier)}.
     *
     * @param subject  The current subject.
     * @param item     The permission object.
     * @param delegate The original check performed by shiro.
     */
    default void check(Subject subject, T item, Runnable delegate) {
        this.check(subject, item.getId(), delegate);
    }

    /**
     * Called for global permission checks that tell whether the subject has the permission.
     * The default implementation delegates to the original shiro check by calling
     * <code>delegate.getAsBoolean()</code>.
     * <br>
     * If you override this, you should also override {@link #check(Subject, Runnable)}.
     *
     * @param subject  The current subject.
     * @param delegate The original check performed by shiro.
     */
    default boolean isPermitted(Subject subject, BooleanSupplier delegate) {
        return delegate.getAsBoolean();
    }

    /**
     * Called for permission checks that tell whether the subject has the permission regarding
     * the permission object with the given id.
     * The default implementation delegates to the original shiro check by calling
     * <code>delegate.getAsBoolean()</code>.
     * <br>
     * If you override this, you should also override {@link #check(Subject, String, Runnable)}.
     *
     * @param subject  The current subject.
     * @param id       The id of the permission object.
     * @param delegate The original check performed by shiro.
     */
    default boolean isPermitted(Subject subject, String id, BooleanSupplier delegate) {
        return delegate.getAsBoolean();
    }

    /**
     * Called for permission checks that tell whether the subject has the permission regarding
     * the given permission object.
     * The default implementation delegates to {@link #isPermitted(Subject, String, BooleanSupplier)}
     * with the id of the permission object.
     * <br>
     * If you override this, you should also override {@link #check(Subject, PermissionObject, Runnable)}.
     *
     * @param subject  The current subject.
     * @param item     The permission object.
     * @param delegate The original check performed by shiro.
     */
    default boolean isPermitted(Subject subject, T item, BooleanSupplier delegate) {
        return isPermitted(subject, item.getId(), delegate);
    }
}
