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

package com.ygmodesto.modernfit.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.ygmodesto.modernfit.annotations.Body;
import com.ygmodesto.modernfit.annotations.ComponentModel;
import com.ygmodesto.modernfit.annotations.DELETE;
import com.ygmodesto.modernfit.annotations.Field;
import com.ygmodesto.modernfit.annotations.FieldMap;
import com.ygmodesto.modernfit.annotations.FormUrlEncoded;
import com.ygmodesto.modernfit.annotations.GET;
import com.ygmodesto.modernfit.annotations.HEAD;
import com.ygmodesto.modernfit.annotations.Header;
import com.ygmodesto.modernfit.annotations.HeaderMap;
import com.ygmodesto.modernfit.annotations.Headers;
import com.ygmodesto.modernfit.annotations.Modernfit;
import com.ygmodesto.modernfit.annotations.Multipart;
import com.ygmodesto.modernfit.annotations.OPTIONS;
import com.ygmodesto.modernfit.annotations.PATCH;
import com.ygmodesto.modernfit.annotations.POST;
import com.ygmodesto.modernfit.annotations.PUT;
import com.ygmodesto.modernfit.annotations.Part;
import com.ygmodesto.modernfit.annotations.PartMap;
import com.ygmodesto.modernfit.annotations.Path;
import com.ygmodesto.modernfit.annotations.Query;
import com.ygmodesto.modernfit.annotations.QueryMap;
import com.ygmodesto.modernfit.annotations.Url;
import com.ygmodesto.modernfit.processor.generator.CodeGenerator;
import com.ygmodesto.modernfit.processor.generator.CodeGeneratorStandalone;
import com.ygmodesto.modernfit.processor.model.InterfaceImplementationInformation;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * Annotation processor for interfaces annotated with {@code @Modernfit}. 
 */
@AutoService(Processor.class)
public class ModernfitProcessor extends AbstractProcessor {

  private static final Logger logger =
      Logger.getLogger(ModernfitProcessor.class.getCanonicalName());

  private Filer filer;
  private Messager messager;
  private Utils utils;

  private CodeGenerator codeGenerator;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnvironment) {
    super.init(processingEnvironment);
    filer = processingEnvironment.getFiler();
    messager = processingEnvironment.getMessager();
    utils =
        Utils.getInstance(processingEnv.getTypeUtils(), processingEnvironment.getElementUtils());
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

    for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(Modernfit.class)) {

      try {

        InterfaceImplementationInformation implementationInformation =
            new InterfaceImplementationInformation.Builder()
                .addInfterfaceElement(annotatedElement)
                .build();

        if (implementationInformation.getComponentModel() == ComponentModel.STANDALONE) {
          codeGenerator = new CodeGeneratorStandalone(utils);
        } else if ((implementationInformation.getComponentModel() == ComponentModel.DAGGER)
            || (implementationInformation.getComponentModel() == ComponentModel.JSR330)) {
          throw new UnsupportedOperationException("Component model Not implemented yet");
        } else if (implementationInformation.getComponentModel() == ComponentModel.SPRING) {
          throw new UnsupportedOperationException("Spring component model Not implemented yet");
        }

        JavaFile.builder(
                implementationInformation.getPackageName(),
                codeGenerator.generateInterfaceImplementation(implementationInformation))
            .build()
            .writeTo(filer);

      } catch (ModernfitProcessorException e) {
        error(e);
      } catch (IOException e) {
        error(e);
      }
    }

    return true;
  }

  private void error(Exception e) {
    messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
  }

  private void error(ModernfitProcessorException e) {
    messager.printMessage(
        Diagnostic.Kind.ERROR,
        e.getMessage(),
        e.getElement(),
        e.getAnnotationMirror(),
        e.getAnnotationValue());
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> annotations = new LinkedHashSet<String>();
    annotations.add(Modernfit.class.getCanonicalName());
    annotations.add(GET.class.getCanonicalName());
    annotations.add(POST.class.getCanonicalName());
    annotations.add(PUT.class.getCanonicalName());
    annotations.add(PATCH.class.getCanonicalName());
    annotations.add(OPTIONS.class.getCanonicalName());
    annotations.add(HEAD.class.getCanonicalName());
    annotations.add(DELETE.class.getCanonicalName());
    annotations.add(Body.class.getCanonicalName());
    annotations.add(Url.class.getCanonicalName());
    annotations.add(Header.class.getCanonicalName());
    annotations.add(Headers.class.getCanonicalName());
    annotations.add(HeaderMap.class.getCanonicalName());
    annotations.add(Path.class.getCanonicalName());
    annotations.add(Query.class.getCanonicalName());
    annotations.add(QueryMap.class.getCanonicalName());
    annotations.add(FormUrlEncoded.class.getCanonicalName());
    annotations.add(Field.class.getCanonicalName());
    annotations.add(FieldMap.class.getCanonicalName());
    annotations.add(Multipart.class.getCanonicalName());
    annotations.add(Part.class.getCanonicalName());
    annotations.add(PartMap.class.getCanonicalName());
    return annotations;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }
}
