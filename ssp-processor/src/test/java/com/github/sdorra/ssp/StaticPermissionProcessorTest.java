package com.github.sdorra.ssp;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.base.Joiner;
import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourcesSubjectFactory;
import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class StaticPermissionProcessorTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    final JavaFileObject clazzInput = JavaFileObjects.forSourceString(
            "com.example.A",
            Joiner.on(System.lineSeparator()).join(
                    "package com.example;",
                    "",
                    "import com.github.sdorra.ssp.StaticPermissions;",
                    "import com.github.sdorra.ssp.PermissionObject;",
                    "",
                    "@StaticPermissions(value = \"a\", permissions = {})",
                    "public class A implements PermissionObject {",
                    "",
                    "  public String getId() {",
                    "    return \"a\";",
                    "  }",
                    "",
                    "}"
            )
    );

    final JavaFileObject expectedOutput = JavaFileObjects.forSourceString(
            "com.example.APermissions",
            Joiner.on(System.lineSeparator()).join(
                    "package com.example;",
                    "",
                    "import com.github.sdorra.ssp.Constants;",
                    "import com.github.sdorra.ssp.PermissionCheck;",
                    "import com.github.sdorra.ssp.PermissionActionCheck;",
                    "import com.github.sdorra.ssp.PermissionActionCheckInterceptor;",
                    "import com.github.sdorra.ssp.PermissionCheckInterceptorWrapper;",
                    "import com.github.sdorra.ssp.PermissionGuard;",
                    "",
                    "public final class APermissions {",
                    "",
                    "  private static final String TYPE = \"a\";",
                    "",
                    "  public static final String ACTION_CREATE = \"create\";",
                    "",
                    "  private static final PermissionGuard<A> __CUSTOM_GUARD = new com.github.sdorra.ssp.PassThroughPermissionGuard();",
                    "",
                    "  private static final PermissionGuard<A> CREATE_GUARD = new com.github.sdorra.ssp.PassThroughPermissionGuard();",
                    "",
                    "  private APermissions(){}",
                    "",
                    "  public static PermissionCheck create() {",
                    "    return check(ACTION_CREATE, createPermissionCheckWrapper(\"create\", CREATE_GUARD));",
                    "  }",
                    "",
                    "  private static PermissionCheckInterceptorWrapper createPermissionCheckWrapper(String customAction, PermissionGuard<A> guard) {",
                    "    PermissionActionCheckInterceptor<A> interceptor = guard.intercept(customAction);",
                    "    PermissionCheckInterceptorWrapper wrapper = PermissionCheckInterceptorWrapper.forRaw(interceptor);",
                    "    return wrapper;",
                    "  }",
                    "",
                    "  private static PermissionCheckInterceptorWrapper createPermissionCheckWrapper(String customAction, String id, PermissionGuard<A> guard) {",
                    "    PermissionActionCheckInterceptor<A> interceptor = guard.intercept(customAction);",
                    "    PermissionCheckInterceptorWrapper wrapper = PermissionCheckInterceptorWrapper.forId(id, interceptor);",
                    "    return wrapper;",
                    "  }",
                    "",
                    "  private static PermissionCheckInterceptorWrapper createPermissionCheckWrapper(String customAction, A item, PermissionGuard<A> guard) {",
                    "    PermissionActionCheckInterceptor<A> interceptor = guard.intercept(customAction);",
                    "    PermissionCheckInterceptorWrapper wrapper = PermissionCheckInterceptorWrapper.forItem(item, interceptor);",
                    "    return wrapper;",
                    "  }",
                    "",
                    "  private static PermissionActionCheck<A> actionCheck(String action, PermissionGuard<A> guard) {",
                    "    PermissionActionCheckInterceptor<A> interceptor = guard.intercept(action);",
                    "    return new PermissionActionCheck<>(TYPE.concat(Constants.SEPARATOR).concat(action), interceptor);",
                    "  }",
                    "",
                    "  private static PermissionCheck check(String permission, PermissionCheckInterceptorWrapper wrapper) {",
                    "    return new PermissionCheck(TYPE.concat(Constants.SEPARATOR).concat(permission), wrapper);",
                    "  }",
                    "}"
            )
    );

    final JavaFileObject interfaceInputWithCustomGlobal = JavaFileObjects.forSourceString(
            "com.example.A",
            Joiner.on(System.lineSeparator()).join(
                    "package com.example;",
                    "",
                    "import com.github.sdorra.ssp.StaticPermissions;",
                    "import com.github.sdorra.ssp.PermissionObject;",
                    "",
                    "@StaticPermissions(value = \"a\", permissions = {}, customGlobal = true)",
                    "interface A extends PermissionObject {",
                    "}"
            )
    );

    final JavaFileObject expectedOutputWithCustomGlobal = JavaFileObjects.forSourceString(
            "com.example.APermissions",
            Joiner.on(System.lineSeparator()).join(
                    "package com.example;",
                    "",
                    "import com.github.sdorra.ssp.Constants;",
                    "import com.github.sdorra.ssp.PermissionCheck;",
                    "import com.github.sdorra.ssp.PermissionActionCheck;",
                    "import com.github.sdorra.ssp.PermissionActionCheckInterceptor;",
                    "import com.github.sdorra.ssp.PermissionCheckInterceptorWrapper;",
                    "import com.github.sdorra.ssp.PermissionGuard;",
                    "",
                    "public final class APermissions {",
                    "",
                    "  private static final String TYPE = \"a\";",
                    "",
                    "  public static final String ACTION_CREATE = \"create\";",
                    "",
                    "  private static final PermissionGuard<A> __CUSTOM_GUARD = new com.github.sdorra.ssp.PassThroughPermissionGuard();",
                    "",
                    "  private static final PermissionGuard<A> CREATE_GUARD = new com.github.sdorra.ssp.PassThroughPermissionGuard();",
                    "",
                    "  private APermissions(){}",
                    "",
                    "  public static PermissionCheck create() {",
                    "    return check(ACTION_CREATE, createPermissionCheckWrapper(\"create\", CREATE_GUARD));",
                    "  }",
                    "",
                    "  public static PermissionCheck custom(String customAction) {",
                    "    return check(customAction, createPermissionCheckWrapper(customAction, __CUSTOM_GUARD));",
                    "  }",
                    "",
                    "  private static PermissionCheckInterceptorWrapper createPermissionCheckWrapper(String customAction, PermissionGuard<A> guard) {",
                    "    PermissionActionCheckInterceptor<A> interceptor = guard.intercept(customAction);",
                    "    PermissionCheckInterceptorWrapper wrapper = PermissionCheckInterceptorWrapper.forRaw(interceptor);",
                    "    return wrapper;",
                    "  }",
                    "",
                    "  private static PermissionCheckInterceptorWrapper createPermissionCheckWrapper(String customAction, String id, PermissionGuard<A> guard) {",
                    "    PermissionActionCheckInterceptor<A> interceptor = guard.intercept(customAction);",
                    "    PermissionCheckInterceptorWrapper wrapper = PermissionCheckInterceptorWrapper.forId(id, interceptor);",
                    "    return wrapper;",
                    "  }",
                    "",
                    "  private static PermissionCheckInterceptorWrapper createPermissionCheckWrapper(String customAction, A item, PermissionGuard<A> guard) {",
                    "    PermissionActionCheckInterceptor<A> interceptor = guard.intercept(customAction);",
                    "    PermissionCheckInterceptorWrapper wrapper = PermissionCheckInterceptorWrapper.forItem(item, interceptor);",
                    "    return wrapper;",
                    "  }",
                    "",
                    "  private static PermissionActionCheck<A> actionCheck(String action, PermissionGuard<A> guard) {",
                    "    PermissionActionCheckInterceptor<A> interceptor = guard.intercept(action);",
                    "    return new PermissionActionCheck<>(TYPE.concat(Constants.SEPARATOR).concat(action), interceptor);",
                    "  }",
                    "",
                    "  private static PermissionCheck check(String permission, PermissionCheckInterceptorWrapper wrapper) {",
                    "    return new PermissionCheck(TYPE.concat(Constants.SEPARATOR).concat(permission), wrapper);",
                    "  }",
                    "}"
            )
    );

    final JavaFileObject interfaceInputWithCustom = JavaFileObjects.forSourceString(
            "com.example.A",
            Joiner.on(System.lineSeparator()).join(
                    "package com.example;",
                    "",
                    "import com.github.sdorra.ssp.StaticPermissions;",
                    "import com.github.sdorra.ssp.PermissionObject;",
                    "",
                    "@StaticPermissions(value = \"a\", permissions = {}, custom = true)",
                    "interface A extends PermissionObject {",
                    "}"
            )
    );

    final JavaFileObject expectedOutputWithCustom = JavaFileObjects.forSourceString(
            "com.example.APermissions",
            Joiner.on(System.lineSeparator()).join(
                    "package com.example;",
                    "",
                    "import com.github.sdorra.ssp.Constants;",
                    "import com.github.sdorra.ssp.PermissionCheck;",
                    "import com.github.sdorra.ssp.PermissionActionCheck;",
                    "import com.github.sdorra.ssp.PermissionActionCheckInterceptor;",
                    "import com.github.sdorra.ssp.PermissionCheckInterceptorWrapper;",
                    "import com.github.sdorra.ssp.PermissionGuard;",
                    "",
                    "public final class APermissions {",
                    "",
                    "  private static final String TYPE = \"a\";",
                    "",
                    "  public static final String ACTION_CREATE = \"create\";",
                    "",
                    "  private static final PermissionGuard<A> __CUSTOM_GUARD = new com.github.sdorra.ssp.PassThroughPermissionGuard();",
                    "",
                    "  private static final PermissionGuard<A> CREATE_GUARD = new com.github.sdorra.ssp.PassThroughPermissionGuard();",
                    "",
                    "  private APermissions(){}",
                    "",
                    "  public static PermissionCheck create() {",
                    "    return check(ACTION_CREATE, createPermissionCheckWrapper(\"create\", CREATE_GUARD));",
                    "  }",
                    "",
                    "  public static PermissionCheck custom(String customAction, String id) {",
                    "    return check(customAction.concat(Constants.SEPARATOR).concat(id), createPermissionCheckWrapper(customAction, id, __CUSTOM_GUARD));",
                    "  }",
                    "",
                    "  public static PermissionCheck custom(String customAction, A item) {",
                    "    return check(customAction.concat(Constants.SEPARATOR).concat(item.getId()), createPermissionCheckWrapper(customAction, item, __CUSTOM_GUARD));",
                    "  }",
                    "",
                    "  public static PermissionActionCheck<A> customActionCheck(String customAction) {",
                    "    return actionCheck(customAction, __CUSTOM_GUARD);",
                    "  }",
                    "",
                    "  private static PermissionCheckInterceptorWrapper createPermissionCheckWrapper(String customAction, PermissionGuard<A> guard) {",
                    "    PermissionActionCheckInterceptor<A> interceptor = guard.intercept(customAction);",
                    "    PermissionCheckInterceptorWrapper wrapper = PermissionCheckInterceptorWrapper.forRaw(interceptor);",
                    "    return wrapper;",
                    "  }",
                    "",
                    "  private static PermissionCheckInterceptorWrapper createPermissionCheckWrapper(String customAction, String id, PermissionGuard<A> guard) {",
                    "    PermissionActionCheckInterceptor<A> interceptor = guard.intercept(customAction);",
                    "    PermissionCheckInterceptorWrapper wrapper = PermissionCheckInterceptorWrapper.forId(id, interceptor);",
                    "    return wrapper;",
                    "  }",
                    "",
                    "  private static PermissionCheckInterceptorWrapper createPermissionCheckWrapper(String customAction, A item, PermissionGuard<A> guard) {",
                    "    PermissionActionCheckInterceptor<A> interceptor = guard.intercept(customAction);",
                    "    PermissionCheckInterceptorWrapper wrapper = PermissionCheckInterceptorWrapper.forItem(item, interceptor);",
                    "    return wrapper;",
                    "  }",
                    "",
                    "  private static PermissionActionCheck<A> actionCheck(String action, PermissionGuard<A> guard) {",
                    "    PermissionActionCheckInterceptor<A> interceptor = guard.intercept(action);",
                    "    return new PermissionActionCheck<>(TYPE.concat(Constants.SEPARATOR).concat(action), interceptor);",
                    "  }",
                    "",
                    "  private static PermissionCheck check(String permission, PermissionCheckInterceptorWrapper wrapper) {",
                    "    return new PermissionCheck(TYPE.concat(Constants.SEPARATOR).concat(permission), wrapper);",
                    "  }",
                    "}"
            )
    );

    final JavaFileObject interfaceInputWithGuards = JavaFileObjects.forSourceString(
            "com.example.A",
            Joiner.on(System.lineSeparator()).join(
                    "package com.example;",
                    "",
                    "import com.github.sdorra.ssp.Guard;",
                    "import com.github.sdorra.ssp.StaticPermissions;",
                    "import com.github.sdorra.ssp.PermissionObject;",
                    "import com.github.sdorra.ssp.PermissionGuard;",
                    "import com.github.sdorra.ssp.PermissionActionCheckInterceptor;",
                    "",
                    "@StaticPermissions(",
                    "        value = \"a\",",
                    "        permissions = {},",
                    "        custom = true,",
                    "        guards = {",
                    "          @Guard(guard = A.CreateGuard.class, guardedPermissions = \"create\"),",
                    "          @Guard(guard = A.DefaultGuard.class)",
                    "        }",
                    "    )",
                    "interface A extends PermissionObject {",
                    "  static class CreateGuard implements PermissionGuard<A> {",
                    "    @Override",
                    "    public PermissionActionCheckInterceptor<A> intercept(String permission) {",
                    "      return new PermissionActionCheckInterceptor<A>(){};",
                    "    }",
                    "  }",
                    "",
                    "  static class DefaultGuard implements PermissionGuard<A> {",
                    "    @Override",
                    "    public PermissionActionCheckInterceptor<A> intercept(String permission) {",
                    "      return new PermissionActionCheckInterceptor<A>(){};",
                    "    }",
                    "  }",
                    "}"
            )
    );

    final JavaFileObject expectedOutputWithGuards = JavaFileObjects.forSourceString(
            "com.example.APermissions",
            Joiner.on(System.lineSeparator()).join(
                    "package com.example;",
                    "",
                    "import com.github.sdorra.ssp.Constants;",
                    "import com.github.sdorra.ssp.PermissionCheck;",
                    "import com.github.sdorra.ssp.PermissionActionCheck;",
                    "import com.github.sdorra.ssp.PermissionActionCheckInterceptor;",
                    "import com.github.sdorra.ssp.PermissionCheckInterceptorWrapper;",
                    "import com.github.sdorra.ssp.PermissionGuard;",
                    "",
                    "public final class APermissions {",
                    "",
                    "  private static final String TYPE = \"a\";",
                    "",
                    "  public static final String ACTION_CREATE = \"create\";",
                    "",
                    "  private static final PermissionGuard<A> __CUSTOM_GUARD = new com.example.A.DefaultGuard();",
                    "",
                    "  private static final PermissionGuard<A> CREATE_GUARD = new com.example.A.CreateGuard();",
                    "",
                    "  private APermissions(){}",
                    "",
                    "  public static PermissionCheck create() {",
                    "    return check(ACTION_CREATE, createPermissionCheckWrapper(\"create\", CREATE_GUARD));",
                    "  }",
                    "",
                    "  public static PermissionCheck custom(String customAction, String id) {",
                    "    return check(customAction.concat(Constants.SEPARATOR).concat(id), createPermissionCheckWrapper(customAction, id, __CUSTOM_GUARD));",
                    "  }",
                    "",
                    "  public static PermissionCheck custom(String customAction, A item) {",
                    "    return check(customAction.concat(Constants.SEPARATOR).concat(item.getId()), createPermissionCheckWrapper(customAction, item, __CUSTOM_GUARD));",
                    "  }",
                    "",
                    "  public static PermissionActionCheck<A> customActionCheck(String customAction) {",
                    "    return actionCheck(customAction, __CUSTOM_GUARD);",
                    "  }",
                    "",
                    "  private static PermissionCheckInterceptorWrapper createPermissionCheckWrapper(String customAction, PermissionGuard<A> guard) {",
                    "    PermissionActionCheckInterceptor<A> interceptor = guard.intercept(customAction);",
                    "    PermissionCheckInterceptorWrapper wrapper = PermissionCheckInterceptorWrapper.forRaw(interceptor);",
                    "    return wrapper;",
                    "  }",
                    "",
                    "  private static PermissionCheckInterceptorWrapper createPermissionCheckWrapper(String customAction, String id, PermissionGuard<A> guard) {",
                    "    PermissionActionCheckInterceptor<A> interceptor = guard.intercept(customAction);",
                    "    PermissionCheckInterceptorWrapper wrapper = PermissionCheckInterceptorWrapper.forId(id, interceptor);",
                    "    return wrapper;",
                    "  }",
                    "",
                    "  private static PermissionCheckInterceptorWrapper createPermissionCheckWrapper(String customAction, A item, PermissionGuard<A> guard) {",
                    "    PermissionActionCheckInterceptor<A> interceptor = guard.intercept(customAction);",
                    "    PermissionCheckInterceptorWrapper wrapper = PermissionCheckInterceptorWrapper.forItem(item, interceptor);",
                    "    return wrapper;",
                    "  }",
                    "",
                    "  private static PermissionActionCheck<A> actionCheck(String action, PermissionGuard<A> guard) {",
                    "    PermissionActionCheckInterceptor<A> interceptor = guard.intercept(action);",
                    "    return new PermissionActionCheck<>(TYPE.concat(Constants.SEPARATOR).concat(action), interceptor);",
                    "  }",
                    "",
                    "  private static PermissionCheck check(String permission, PermissionCheckInterceptorWrapper wrapper) {",
                    "    return new PermissionCheck(TYPE.concat(Constants.SEPARATOR).concat(permission), wrapper);",
                    "  }",
                    "}"
            )
    );

    @Test
    public void processClass() {
        processAndAssert(clazzInput, expectedOutput);
    }

    @Test
    public void processInterface() {
        final JavaFileObject interfaze = JavaFileObjects.forSourceString(
                "com.example.A",
                Joiner.on(System.lineSeparator()).join(
                        "package com.example;",
                        "",
                        "import com.github.sdorra.ssp.StaticPermissions;",
                        "import com.github.sdorra.ssp.PermissionObject;",
                        "",
                        "@StaticPermissions(value = \"a\", permissions = {})",
                        "interface A extends PermissionObject {",
                        "}"
                )
        );

        processAndAssert(interfaze, expectedOutput);
    }

    @Test
    public void shouldGenerateWithCustom() {
        processAndAssert(interfaceInputWithCustom, expectedOutputWithCustom);
    }

    @Test
    public void shouldGenerateWithCustomGlobal() {
        processAndAssert(interfaceInputWithCustomGlobal, expectedOutputWithCustomGlobal);
    }

    @Test
    public void shouldGenerateWithGuards() {
        processAndAssert(interfaceInputWithGuards, expectedOutputWithGuards);
    }

    private void processAndAssert(JavaFileObject input, JavaFileObject output) {
        Truth.assert_()
             .about(JavaSourcesSubjectFactory.javaSources())
             .that(Collections.singletonList(input))
             .processedWith(new StaticPermissionProcessor())
             .compilesWithoutError()
             .and()
             .generatesSources(output);
    }

    @Test
    public void testExceptionOnWrite() throws IOException {
        StaticPermissionModelBuilder builder = new StaticPermissionModelBuilder();
        MustacheFactory mustacheFactory = mock(MustacheFactory.class);
        Mustache mustache = mock(Mustache.class);
        when(mustacheFactory.compile(anyString())).thenReturn(mustache);
        Writer writer = mock(Writer.class);
        when(mustache.execute(any(Writer.class), any(StaticPermissionModel.class))).thenReturn(writer);
        doThrow(IOException.class).when(writer).flush();

        expectedException.expect(RuntimeException.class);
        expectedException.expectCause(CoreMatchers.<Throwable>instanceOf(IllegalStateException.class));

        Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(Collections.singletonList(clazzInput))
                .processedWith(new StaticPermissionProcessor(builder, mustacheFactory))
                .failsToCompile()
                .withErrorContaining("failed to create model");
    }

}
