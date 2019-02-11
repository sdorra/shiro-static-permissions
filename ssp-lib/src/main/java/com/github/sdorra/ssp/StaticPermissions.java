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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link StaticPermissions} annotation is picked up by a annotation processor, which generates a class for static
 * shiro permissions for the annotated class.
 * 
 * @author Sebastian Sdorra
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface StaticPermissions {
  
  /**
   * Returns type of permission.
   * 
   * @return type of permission
   */
  String value();
  
  /**
   * Name of the generated class.
   * 
   * @return name of generated class
   */
  String generatedClass() default "";
  
  /**
   * Returns item related permissions.
   * 
   * @return item related permissions
   */
  String[] permissions() default {"read", "modify", "delete"};
  
  /**
   * Returns global permissions.
   * 
   * @return global permissions
   */
  String[] globalPermissions() default {"create"};

  /**
   * Allow custom permission checks, by adding methods named custom to the generated class.
   *
   * @return custom permission
   */
  boolean custom() default false;

  /**
   * Allow custom global permission checks, by adding a method named custom to the generated class.
   *
   * @return custom permission
   */
  boolean customGlobal() default false;
}
