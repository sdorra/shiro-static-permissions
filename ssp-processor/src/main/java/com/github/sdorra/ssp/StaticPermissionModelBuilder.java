/*
 * The MIT License
 *
 * Copyright 2016 Sebastian Sdorra.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.sdorra.ssp;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Generates {@link StaticPermissionModel} from {@link TypeElement}.
 *
 * @author Sebastian Sdorra
 */
final class StaticPermissionModelBuilder {

  /**
   * Processes the type element and generates the model.
   *
   *
   * @param processingEnv environment for the annotation processing
   * @param classElement class element
   *
   * @return model for template input
   */
  StaticPermissionModel process(ProcessingEnvironment processingEnv, TypeElement classElement) {
      if (!TypeElements.isAssignableFrom(PermissionObject.class, classElement)) {
          throw new IllegalStateException(
                  "static permissions can only be generated if the target class implements the PermissionObject interface"
          );
      }

      StaticPermissions staticPermissions = classElement.getAnnotation(StaticPermissions.class);
      if (staticPermissions == null) {
          throw new IllegalStateException("type element is not annotated with StaticPermissions annotation");
      }

      Map<String, String> permissionsWithGuards = extractGuards(processingEnv, classElement);

      String className = classElement.getSimpleName().toString();
      String packageName = getPackageName(classElement);

      String generatedClass = staticPermissions.generatedClass();
      if (generatedClass.isEmpty()) {
          generatedClass = className.concat("Permissions");
      }

      return new StaticPermissionModel(
              staticPermissions,
              packageName,
              generatedClass,
              className,
              convert(staticPermissions.permissions()),
              convert(staticPermissions.globalPermissions()),
              permissionsWithGuards
      );
  }

    private Map<String, String> extractGuards(ProcessingEnvironment processingEnv, TypeElement classElement) {
        Map<String, String> permissionsWithGuards = new HashMap<>();

        TypeMirror staticPermissionsType = processingEnv.getElementUtils().getTypeElement(StaticPermissions.class.getName() ).asType();
        Optional<? extends AnnotationMirror> staticPermissionAnnotation = classElement.getAnnotationMirrors()
                .stream()
                .filter(mirror -> mirror.getAnnotationType().equals(staticPermissionsType)).findFirst();

        staticPermissionAnnotation
                .flatMap(annotationMirror -> this.<Collection<AnnotationMirror>>extractElement(annotationMirror, "guards"))
                .ifPresent(guards -> guards.forEach(guardAnnotationMirror -> {
                            Optional<Collection<AnnotationValue>> guardedPermissionsValue =
                                    extractElement(guardAnnotationMirror, "guardedPermissions");
                            this.<DeclaredType>extractElement(guardAnnotationMirror, "guard")
                                    .ifPresent(guardClass -> {
                                        if (guardedPermissionsValue.isPresent()) {
                                            guardedPermissionsValue
                                                    .get()
                                                    .forEach(guardedPermissionValue -> permissionsWithGuards.put(guardedPermissionValue.getValue().toString(), guardClass.toString()));
                                        } else {
                                            // No explicit permissions mentioned. Use this as fallback.
                                            permissionsWithGuards.put(null, guardClass.toString());
                                        }
                                    });
                        }
                ));
        return permissionsWithGuards;
    }

    @SuppressWarnings("unchecked") // we do known the types of the annotations and they are guaranteed by the compiler
    private <T> Optional<T> extractElement(AnnotationMirror annotationMirror, String elementName) {
        return annotationMirror.getElementValues().entrySet().stream()
                .filter(ee -> ee.getKey().getSimpleName().contentEquals(elementName))
                .map(Map.Entry::getValue)
                .map(AnnotationValue::getValue)
                .map(o -> (T) o)
                .findFirst();
    }

    private Collection<Action> convert(String[] permissions) {
    List<Action> actions = new ArrayList<>();
    for (String permission : permissions) {
      actions.add(new Action(permission, permission.toUpperCase(Locale.ENGLISH)));
    }
    return actions;
  }

  private String getPackageName(TypeElement classElement) {
    return ((PackageElement) classElement.getEnclosingElement()).getQualifiedName().toString();
  }

}
