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

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import com.ygmodesto.modernfit.processor.Utils;
import com.ygmodesto.modernfit.processor.model.InterfaceImplementationInformation;
import com.ygmodesto.modernfit.services.HttpClient;
import java.util.Map;
import javax.lang.model.element.Modifier;

/**
 * Class that supports {@link CodeGenerator} when generating the implementations of the interfaces 
 * that have their componentModel defined in {@code @Modernfit} as ComponentModel.STANDALONE.
 */
public class CodeGeneratorStandalone extends CodeGenerator {

  private static final String BUILDER_CLASSNAME = "Builder";
  private static final String BUILDER_FIELDNAME = "builder";
  private static final String BUILDER_METHODNAME = "builder";
  private static final String BUILD_METHODNAME = "build";
  private static final String BUILDER_ADDHTTPCLIENTNAME = "addHttpClient";
  private static final String BUILDER_ADDCONVERTERNAME = "addConverterFactory";

  private MetaSpecStandalone metaSpecStandalone;

  public CodeGeneratorStandalone(Utils utils) {
    super(utils);
  }

  @Override
  protected void createMetaSpec(String requestInfoBuilderName, String bodyBuilderName) {

    this.metaSpecStandalone = new MetaSpecStandalone(requestInfoBuilderName, bodyBuilderName);
    this.metaSpec = this.metaSpecStandalone;
  }

  // TODO comprobar o hacer que httpClient y converter en el generado nunca sean null
  @Override
  protected TypeSpec.Builder generateInterfaceImplementationClass(
      InterfaceImplementationInformation interfaceImplementationInformation) {

    String packageName = interfaceImplementationInformation.getPackageName();
    String implementationName = interfaceImplementationInformation.getImplementationName();
    ClassName httpClient = ClassName.get(interfaceImplementationInformation.getHttpClient());
    ClassName converterFactory =
        ClassName.get(interfaceImplementationInformation.getConverterFactory());

    ClassName className = ClassName.get(packageName, implementationName);
    ClassName builderClassName = ClassName.get(packageName, implementationName, BUILDER_CLASSNAME);

    MethodSpec.Builder constructorBuilder =
        MethodSpec.constructorBuilder()
        .addModifiers(Modifier.PRIVATE)
        .addParameter(builderClassName, BUILDER_FIELDNAME)
        .addStatement("baseUrl = $S", interfaceImplementationInformation.getBaseUrl())
        .addCode(getHttpClientInitializer(httpClient))
        .addCode(getConverterInitializer(converterFactory));

    MethodSpec builderMethod =
        MethodSpec.methodBuilder(BUILDER_METHODNAME)
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .returns(builderClassName)
        .addStatement("return new $T()", builderClassName)
        .build();

    FieldSpec httpClientField =
        FieldSpec.builder(HttpClient.class, HTTPCLIENT_NAME).addModifiers(Modifier.PRIVATE).build();

    FieldSpec converterFactoryField =
        FieldSpec.builder(converterFactory, CONVERTERFACTORY_NAME)
        .addModifiers(Modifier.PRIVATE)
        .build();

    MethodSpec addHttpClient =
        MethodSpec.methodBuilder(BUILDER_ADDHTTPCLIENTNAME)
        .addModifiers(Modifier.PUBLIC)
        .addParameter(ParameterSpec.builder(httpClient, HTTPCLIENT_NAME).build())
        .returns(builderClassName)
        .addStatement("this.$N = $N", HTTPCLIENT_NAME, HTTPCLIENT_NAME)
        .addStatement("return this")
        .build();

    MethodSpec addConverterFactory =
        MethodSpec.methodBuilder(BUILDER_ADDCONVERTERNAME)
        .addModifiers(Modifier.PUBLIC)
        .addParameter(ParameterSpec.builder(converterFactory, CONVERTERFACTORY_NAME).build())
        .returns(builderClassName)
        .addStatement("this.$N = $N", CONVERTERFACTORY_NAME, CONVERTERFACTORY_NAME)
        .addStatement("return this")
        .build();

    TypeSpec.Builder builder =
        TypeSpec.classBuilder(builderClassName)
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .addField(httpClientField)
        .addField(converterFactoryField)
        .addMethod(addHttpClient)
        .addMethod(addConverterFactory)
        .addMethod(
            MethodSpec.methodBuilder(BUILD_METHODNAME)
            .addModifiers(Modifier.PUBLIC)
            .returns(className)
            .addStatement("return new $T(this)", className)
            .build());

    metaSpecStandalone.setConstructor(constructorBuilder);
    metaSpecStandalone.setBuilderClass(builder);

    return TypeSpec.classBuilder(implementationName)
        .addSuperinterface(
            TypeName.get(interfaceImplementationInformation.getInterfaceElement().asType()))
        .addModifiers(Modifier.PUBLIC)
        .addMethod(builderMethod);
  }

  @Override
  public TypeSpec buildAll() {

    Builder implementationBuilder = metaSpecStandalone.getImplementationBuilder();
    implementationBuilder.addType(metaSpecStandalone.getBuilderClass().build());

    for (Map.Entry<TypeName, ConverterSpec> entry :
        metaSpecStandalone.getUrlConverters().entrySet()) {
      ConverterSpec urlConverter = entry.getValue();
      implementationBuilder.addField(urlConverter.getField());
      instantiateUrlConverterField(metaSpecStandalone.getConstructor(), urlConverter);
    }

    for (Map.Entry<TypeName, ConverterSpec> entry :
        metaSpecStandalone.getRequestConverters().entrySet()) {
      ConverterSpec requestConverter = entry.getValue();
      implementationBuilder.addField(requestConverter.getField());
      instantiateRequestConverterField(metaSpecStandalone.getConstructor(), requestConverter);
    }

    for (Map.Entry<TypeName, ConverterSpec> entry :
        metaSpecStandalone.getResponseConverters().entrySet()) {
      ConverterSpec responseConverter = entry.getValue();
      implementationBuilder.addField(responseConverter.getField());
      instantiateResponseConvertersFields(metaSpecStandalone.getConstructor(), responseConverter);
    }

    for (MethodSpec.Builder interfaceMethodBuilder : metaSpecStandalone.getInterfaceMethods()) {
      implementationBuilder.addMethod(interfaceMethodBuilder.build());
    }

    implementationBuilder.addMethod(metaSpecStandalone.getConstructor().build());

    return implementationBuilder.build();
  }

  private void instantiateUrlConverterField(
      MethodSpec.Builder constructorBuilder, ConverterSpec urlConverter) {
    String zombieFieldName = "zombie" + urlConverter.getField().name;
    constructorBuilder.addStatement("$T $L = null", urlConverter.getSource(), zombieFieldName);
    constructorBuilder.addStatement(
        "$N = $L.getUrlConverter($L, new $T() {})",
        urlConverter.getField(),
        CONVERTERFACTORY_NAME,
        zombieFieldName,
        ParameterizedTypeName.get(customType, urlConverter.getSource()));
  }

  private void instantiateRequestConverterField(
      MethodSpec.Builder constructorBuilder, ConverterSpec requestConverter) {
    String zombieFieldName = "zombie" + requestConverter.getField().name;
    constructorBuilder.addStatement("$T $L = null", requestConverter.getSource(), zombieFieldName);
    constructorBuilder.addStatement(
        "$N = $L.getRequestConverter($L, new $T() {})",
        requestConverter.getField(),
        CONVERTERFACTORY_NAME,
        zombieFieldName,
        ParameterizedTypeName.get(customType, requestConverter.getSource()));
  }

  private void instantiateResponseConvertersFields(
      MethodSpec.Builder constructorBuilder, ConverterSpec responseConverter) {
    String zombieFieldName = "zombie" + responseConverter.getField().name;
    constructorBuilder.addStatement(
        "$T $L = null", responseConverter.getDestination(), zombieFieldName);
    constructorBuilder.addStatement(
        "$N = $L.getResponseConverter($L, new $T() {})",
        responseConverter.getField(),
        CONVERTERFACTORY_NAME,
        zombieFieldName,
        ParameterizedTypeName.get(customType, responseConverter.getDestination()));
  }

  private CodeBlock getConverterInitializer(ClassName converter) {

    CodeBlock.Builder builderInitializer = CodeBlock.builder();
    builderInitializer.addStatement(
        "$N = ( $N.$L == null) ? $T.create() : $N.$L",
        CONVERTERFACTORY_NAME,
        BUILDER_FIELDNAME,
        CONVERTERFACTORY_NAME,
        converter,
        BUILDER_FIELDNAME,
        CONVERTERFACTORY_NAME);

    return builderInitializer.build();
  }

  private CodeBlock getHttpClientInitializer(ClassName httpClient) {

    CodeBlock.Builder builderInitializer = CodeBlock.builder();

    builderInitializer.addStatement(
        "$N = ( $N.$L == null) ? $T.create() : $N.$L",
        HTTPCLIENT_NAME,
        BUILDER_FIELDNAME,
        HTTPCLIENT_NAME,
        httpClient,
        BUILDER_FIELDNAME,
        HTTPCLIENT_NAME);

    return builderInitializer.build();
  }
}
