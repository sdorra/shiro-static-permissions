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
