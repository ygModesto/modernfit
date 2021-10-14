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

package com.ygmodesto.modernfit.processor.functional;

import com.ygmodesto.modernfit.processor.ModernfitProcessor;
import com.ygmodesto.modernfit.processor.server.SpringBootTestServer;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.rules.TemporaryFolder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class AbstractFunctionalTest {

  private static final Logger LOGGER = Logger.getLogger(AbstractFunctionalTest.class.getName());

  @ClassRule public static TemporaryFolder tmp = new TemporaryFolder();

  protected static JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
  protected static ConfigurableApplicationContext configurableApplicationContext;
  protected static DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();
  protected static StandardJavaFileManager fileManager =
      javac.getStandardFileManager(null, null, null);

  @BeforeClass
  public static void setUpAbstract() throws IOException {

    fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singleton(tmp.newFolder()));
    fileManager.setLocation(
        StandardLocation.SOURCE_PATH,
        Collections.singleton(
            new File("src" + File.separator + "test" + File.separator + "java" + File.separator)));

    configurableApplicationContext =
        SpringApplication.run(SpringBootTestServer.class, new String[] {});
  }

  @AfterClass
  public static void stopServer() {
    SpringApplication.exit(configurableApplicationContext);
  }
  
  protected static <T> T util(Class<T> clazz, String implSuffix) throws Exception {
    return util(clazz, clazz, implSuffix);
  }

  protected static <T> T util(Class<?> sourceFile, Class<T> clazz, String implSuffix) throws Exception {

    Collection<JavaFileObject> inputs = new ArrayList<JavaFileObject>();
    JavaFileObject javaFileObject =
        fileManager.getJavaFileForInput(
            StandardLocation.SOURCE_PATH, sourceFile.getCanonicalName(), Kind.SOURCE);
    inputs.add(javaFileObject);

    JavaCompiler.CompilationTask task =
        javac.getTask(new PrintWriter(System.err), fileManager, collector, null, null, inputs);
    task.setProcessors(Collections.singleton(new ModernfitProcessor()));
    boolean result = task.call();
    if (!result || collector.getDiagnostics().size() > 0) {
      List<Diagnostic<? extends JavaFileObject>> diagnostics = collector.getDiagnostics();
      for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
        //				collector.report(diagnostic);
        LOGGER.log(Level.INFO, diagnostic.getMessage(Locale.ENGLISH));
        LOGGER.log(Level.INFO, diagnostic.getCode());
        LOGGER.log(Level.INFO, diagnostic.getSource().getName().toString());
        LOGGER.log(Level.INFO, String.valueOf(diagnostic.getLineNumber()));
        LOGGER.log(Level.INFO, String.valueOf(diagnostic.getStartPosition()));
        LOGGER.log(Level.INFO, String.valueOf(diagnostic.getPosition()));
        LOGGER.log(Level.INFO, String.valueOf(diagnostic.getEndPosition()));
      }
      //			LOGGER.log(Level.INFO, "Compile error");
    }

    ClassLoader loaderClassOutput = fileManager.getClassLoader(StandardLocation.CLASS_OUTPUT);
//    String implCanonicalName = clazz.getCanonicalName() + implSuffix;
//    String implCanonicalName = clazz.getPackageName() + "." + clazz.getSimpleName() + implSuffix;
    String implCanonicalName = clazz.getPackage().getName() + "." + clazz.getSimpleName() + implSuffix;
    Class<?> implClass = loaderClassOutput.loadClass(implCanonicalName);
    Class<?> builderClass = implClass.getClasses()[0];

    Method builderMethod = implClass.getMethod("builder");
    Method buildMethod = builderClass.getMethod("build");
    return clazz.cast(buildMethod.invoke(builderMethod.invoke(null)));
  }
}
