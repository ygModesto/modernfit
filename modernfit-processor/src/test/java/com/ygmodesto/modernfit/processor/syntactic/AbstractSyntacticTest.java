/*
 * Copyright 2020 Yago Modesto González Diéguez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ygmodesto.modernfit.processor.syntactic;

import static com.google.testing.compile.JavaSourcesSubject.assertThat;

import com.ygmodesto.modernfit.processor.ModernfitProcessor;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import org.junit.BeforeClass;

public abstract class AbstractSyntacticTest {

  protected static JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
  protected static StandardJavaFileManager fileManager =
      javac.getStandardFileManager(null, null, null);

  @BeforeClass
  public static void setUp() throws IOException {
    fileManager.setLocation(
        StandardLocation.SOURCE_PATH,
        Collections.singleton(
            new File("src" + File.separator + "test" + File.separator + "java" + File.separator)));
  }

  protected <T> void coreTest(Class<T> interfaceClass, Class<? extends T> implementationClass)
      throws IOException {

    JavaFileObject interfaceJavaFileObject = fromClass(interfaceClass);
    JavaFileObject implementationJavaFileObject = fromClass(implementationClass);

    assertThat(interfaceJavaFileObject)
        .processedWith(new ModernfitProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(implementationJavaFileObject);
  }

  protected JavaFileObject fromClass(Class<?> clazz) throws IOException {
    return fileManager.getJavaFileForInput(
        StandardLocation.SOURCE_PATH, clazz.getCanonicalName(), Kind.SOURCE);
  }
}
