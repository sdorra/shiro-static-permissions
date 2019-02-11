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
                    "",
                    "public final class APermissions {",
                    "",
                    "  private static final String TYPE = \"a\";",
                    "",
                    "  public static final String ACTION_CREATE = \"create\";",
                    "",
                    "  private APermissions(){}",
                    "",
                    "  public static PermissionCheck create() {",
                    "    return check(ACTION_CREATE);",
                    "  }",
                    "",
                    "  private static PermissionActionCheck<A> actionCheck(String action) {",
                    "    return new PermissionActionCheck<>(TYPE.concat(Constants.SEPARATOR).concat(action));",
                    "  }",
                    "",
                    "  private static PermissionCheck check(String permission) {",
                    "    return new PermissionCheck(TYPE.concat(Constants.SEPARATOR).concat(permission));",
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
                    "",
                    "public final class APermissions {",
                    "",
                    "  private static final String TYPE = \"a\";",
                    "",
                    "  public static final String ACTION_CREATE = \"create\";",
                    "",
                    "  private APermissions(){}",
                    "",
                    "  public static PermissionCheck create() {",
                    "    return check(ACTION_CREATE);",
                    "  }",
                    "",
                    "  public static PermissionCheck custom(String customAction) {",
                    "    return check(customAction);",
                    "  }",
                    "",
                    "",
                    "  private static PermissionActionCheck<A> actionCheck(String action) {",
                    "    return new PermissionActionCheck<>(TYPE.concat(Constants.SEPARATOR).concat(action));",
                    "  }",
                    "",
                    "  private static PermissionCheck check(String permission) {",
                    "    return new PermissionCheck(TYPE.concat(Constants.SEPARATOR).concat(permission));",
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
                    "",
                    "public final class APermissions {",
                    "",
                    "  private static final String TYPE = \"a\";",
                    "",
                    "  public static final String ACTION_CREATE = \"create\";",
                    "",
                    "  private APermissions(){}",
                    "",
                    "  public static PermissionCheck create() {",
                    "    return check(ACTION_CREATE);",
                    "  }",
                    "",
                    "  public static PermissionCheck custom(String customAction, String id) {",
                    "    return check(customAction.concat(Constants.SEPARATOR).concat(id));",
                    "  }",
                    "",
                    "  public static PermissionCheck custom(String customAction, A item) {",
                    "    return check(customAction.concat(Constants.SEPARATOR).concat(item.getId()));",
                    "  }",
                    "",
                    "  public static PermissionActionCheck<A> customActionCheck(String customAction) {",
                    "    return actionCheck(customAction);",
                    "  }",
                    "",
                    "",
                    "  private static PermissionActionCheck<A> actionCheck(String action) {",
                    "    return new PermissionActionCheck<>(TYPE.concat(Constants.SEPARATOR).concat(action));",
                    "  }",
                    "",
                    "  private static PermissionCheck check(String permission) {",
                    "    return new PermissionCheck(TYPE.concat(Constants.SEPARATOR).concat(permission));",
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
        when(mustache.execute(any(Writer.class), any())).thenReturn(writer);
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
