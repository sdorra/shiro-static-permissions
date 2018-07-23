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

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.kohsuke.MetaInfServices;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

/**
 * Processes each type which is annotated with {@link StaticPermissions} and generates a
 * class for permission checks. The processor uses {@link StaticPermissionModelBuilder} to
 * generate a model and writes it to the disk with the mustache template engine.
 *
 * @author Sebastian Sdorra
 */
@SupportedAnnotationTypes("*")
@MetaInfServices(Processor.class)
@SuppressWarnings({"Since16", "Since15"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class StaticPermissionProcessor extends AbstractProcessor {

  private static final String TEMPLATE = "com/github/sdorra/ssp/template.mustache";

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    StaticPermissionModelBuilder builder = new StaticPermissionModelBuilder();
    for (Element e : roundEnv.getElementsAnnotatedWith(StaticPermissions.class)) {
      if (e.getKind() == ElementKind.CLASS) {
        handle(builder, (TypeElement) e);
      }
    }
    return true;
  }

  private void handle(StaticPermissionModelBuilder builder, TypeElement typeElement) {
    try {
      StaticPermissionModel model = builder.process(typeElement);

      write(model);
    }
    catch (IOException ex) {
      throw new IllegalStateException("failed to create model", ex);
    }
  }

  private void write(StaticPermissionModel model) throws IOException {
    Filer filer = processingEnv.getFiler();

    JavaFileObject jfo = filer.createSourceFile(model.getFullClassName());

    MustacheFactory mf = new DefaultMustacheFactory();
    Mustache mustache = mf.compile(TEMPLATE);

    try (Writer writer = jfo.openWriter()) {
      mustache.execute(writer, model).flush();
    }
  }

}
