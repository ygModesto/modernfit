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

package com.ygmodesto.modernfit.processor.model;

import com.ygmodesto.modernfit.annotations.ComponentModel;
import com.ygmodesto.modernfit.annotations.Modernfit;
import com.ygmodesto.modernfit.processor.ModernfitProcessorException;
import com.ygmodesto.modernfit.processor.Utils;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.util.ElementFilter;

/**
 * Defines all the elements that are required to create the implementation of an interface 
 * annotated with {@code @Modernfit}.
 */
public class InterfaceImplementationInformation {

  private TypeElement interfaceElement;
  private String packageName;
  private String interfaceName;
  private String implementationName;

  private String baseUrl;

  private TypeElement converterFactory;
  private TypeElement httpClient;

  private ComponentModel componentModel;

  private List<MethodInformation> methodsInformation;

  private InterfaceImplementationInformation(Builder builder) throws ModernfitProcessorException {

    this.interfaceElement = builder.interfaceElement;
    this.packageName = builder.packageName;
    this.interfaceName = builder.interfaceName;
    this.implementationName = builder.implementationName;
    this.baseUrl = builder.baseUrl;

    this.converterFactory = builder.converterFactory;
    this.httpClient = builder.httpClient;

    this.componentModel = builder.componentModel;

    this.methodsInformation = builMethods();
  }

  public String getPackageName() {
    return packageName;
  }

  public String getInterfaceName() {
    return interfaceName;
  }

  public String getImplementationName() {
    return implementationName;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public List<MethodInformation> getMethodsInformation() {
    return methodsInformation;
  }

  public TypeElement getConverterFactory() {
    return converterFactory;
  }

  public TypeElement getHttpClient() {
    return httpClient;
  }

  public ComponentModel getComponentModel() {
    return componentModel;
  }

  public TypeElement getInterfaceElement() {
    return interfaceElement;
  }

  public static Builder builder() {
    return new Builder();
  }

  private List<MethodInformation> builMethods() throws ModernfitProcessorException {

    List<ExecutableElement> methodsIn =
        ElementFilter.methodsIn(interfaceElement.getEnclosedElements());
    List<MethodInformation> methods = new ArrayList<>(methodsIn.size());

    for (ExecutableElement methodElement : methodsIn) {
      try {

        MethodInformation.Builder methodBuilder =
            new MethodInformation.Builder().addExecutableElement(methodElement).addBaseUrl(baseUrl);

        methodBuilder.addInterfaceImplementationInformation(this);
        methods.add(methodBuilder.build());

      } catch (RuntimeException e) {
        throw new ModernfitProcessorException("Unexpected exception processing", methodElement);
      }
    }

    return methods;
  }

  /**
   * Builder class for {@link InterfaceImplementationInformation
   * InterfaceImplementationInformation}.
   */
  public static final class Builder {

    private Utils utils = Utils.getInstance();

    private TypeElement interfaceElement;

    private String packageName;
    private String interfaceName;
    private String implementationName;
    private String baseUrl;

    private TypeElement converterFactory;
    private TypeElement httpClient;

    private ComponentModel componentModel;

    public Builder addInfterfaceElement(Element interfaceElement) {
      this.interfaceElement = TypeElement.class.cast(interfaceElement);
      return this;
    }

    public Builder addInfterfaceElement(TypeElement interfaceElement) {
      this.interfaceElement = interfaceElement;
      return this;
    }

    /** Builer method. */
    public InterfaceImplementationInformation build() throws ModernfitProcessorException {

      assert interfaceElement != null : "interfaceElement == null";

      Modernfit modernfitAnnotation = interfaceElement.getAnnotation(Modernfit.class);
      packageName = utils.getElements().getPackageOf(interfaceElement).toString();
      interfaceName = interfaceElement.getSimpleName().toString();
      implementationName = interfaceName + modernfitAnnotation.suffix();
      baseUrl = modernfitAnnotation.value();
      componentModel = modernfitAnnotation.componentModel();

      try {
        Class<?> clazz = modernfitAnnotation.converterFactory();
        converterFactory = utils.getElements().getTypeElement(clazz.getCanonicalName());
      } catch (MirroredTypeException mte) {
        DeclaredType classTypeMirror = DeclaredType.class.cast(mte.getTypeMirror());
        converterFactory = TypeElement.class.cast(classTypeMirror.asElement());
      }
      try {
        Class<?> clazz = modernfitAnnotation.client();
        httpClient = utils.getElements().getTypeElement(clazz.getCanonicalName());
      } catch (MirroredTypeException mte) {
        DeclaredType classTypeMirror = DeclaredType.class.cast(mte.getTypeMirror());
        httpClient = TypeElement.class.cast(classTypeMirror.asElement());
      }

      return new InterfaceImplementationInformation(this);
    }
  }
}
