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

package com.ygmodesto.modernfit.processor.generator;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for the interface implementation generation process.
 *
 * @see CodeGenerator
 */
public abstract class MetaSpec {

  protected TypeSpec.Builder implementationBuilder;

  protected List<MethodSpec.Builder> interfaceMethods = new ArrayList<>();

  protected Map<TypeName, ConverterSpec> urlConverters = new LinkedHashMap<>();
  protected Map<TypeName, ConverterSpec> requestConverters = new LinkedHashMap<>();
  protected Map<TypeName, ConverterSpec> responseConverters = new LinkedHashMap<>();

  protected String requestInfoBuilderName;
  protected String bodyBuilderName;

  public MetaSpec(String requestInfoBuilderName, String bodyBuilderName) {
    this.requestInfoBuilderName = requestInfoBuilderName;
    this.bodyBuilderName = bodyBuilderName;
  }

  public TypeSpec.Builder getImplementationBuilder() {
    return implementationBuilder;
  }

  public void setImplementationBuilder(TypeSpec.Builder implementationBuilder) {
    this.implementationBuilder = implementationBuilder;
  }

  public List<MethodSpec.Builder> getInterfaceMethods() {
    return interfaceMethods;
  }

  public void setInterfaceMethods(List<MethodSpec.Builder> interfaceMethods) {
    this.interfaceMethods = interfaceMethods;
  }

  public void addInterfaceMethod(MethodSpec.Builder interfaceMethod) {
    interfaceMethods.add(interfaceMethod);
  }

  public ConverterSpec getUrlConverterBySource(TypeName source) {
    return urlConverters.get(source);
  }

  public Map<TypeName, ConverterSpec> getUrlConverters() {
    return urlConverters;
  }

  public void setUrlConverters(Map<TypeName, ConverterSpec> urlConverters) {
    this.urlConverters = urlConverters;
  }

  /**
   * Add a ConverterSpec to the urlConverters list.
   *
   * @param urlConverter a ConverterSpec.
   */
  public boolean addUrlConverter(ConverterSpec urlConverter) {
    if (!urlConverters.containsKey(urlConverter.getSource())) {
      urlConverters.put(urlConverter.getSource(), urlConverter);
      return true;
    }
    return false;
  }

  public ConverterSpec getRequestConverterBySource(TypeName source) {
    return requestConverters.get(source);
  }

  public Map<TypeName, ConverterSpec> getRequestConverters() {
    return requestConverters;
  }

  public void setRequestConverters(Map<TypeName, ConverterSpec> requestConverters) {
    this.requestConverters = requestConverters;
  }


  /**
   * Add a ConverterSpec to the requestConverters list.
   *
   * @param requestConverter a ConverterSpec.
   */
  public boolean addRequestConverter(ConverterSpec requestConverter) {
    if (!requestConverters.containsKey(requestConverter.getSource())) {
      requestConverters.put(requestConverter.getSource(), requestConverter);
      return true;
    }
    return false;
  }

  public ConverterSpec getResponseConverterByDestination(TypeName destination) {
    return responseConverters.get(destination);
  }

  public Map<TypeName, ConverterSpec> getResponseConverters() {
    return responseConverters;
  }

  public void setResponseConverters(Map<TypeName, ConverterSpec> responseConvertersField) {
    this.responseConverters = responseConvertersField;
  }

  /**
   * Add a ConverterSpec to the responseConverters list.
   *
   * @param responseConverter a ConverterSpec.
   */
  public boolean addResponseConverter(ConverterSpec responseConverter) {
    if (!responseConverters.containsKey(responseConverter.getDestination())) {
      responseConverters.put(responseConverter.getDestination(), responseConverter);
      return true;
    }
    return false;
  }

  public String getRequestInfoBuilderName() {
    return requestInfoBuilderName;
  }

  public void setRequestInfoBuilderName(String requestInfoBuilderName) {
    this.requestInfoBuilderName = requestInfoBuilderName;
  }

  public String getBodyBuilderName() {
    return bodyBuilderName;
  }

  public void setBodyBuilderName(String bodyBuilderName) {
    this.bodyBuilderName = bodyBuilderName;
  }
}
