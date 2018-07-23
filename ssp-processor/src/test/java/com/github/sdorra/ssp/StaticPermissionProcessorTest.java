package com.github.sdorra.ssp;

import com.google.common.base.Joiner;
import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourcesSubjectFactory;
import org.junit.Test;

import javax.tools.JavaFileObject;

import java.util.Arrays;

import static org.junit.Assert.*;

public class StaticPermissionProcessorTest {

    @Test
    public void test() {
        final JavaFileObject input = JavaFileObjects.forSourceString(
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
        final JavaFileObject output = JavaFileObjects.forSourceString(
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

        Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(Arrays.asList(input))
                .processedWith(new StaticPermissionProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(output);
    }

}
