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
import com.squareup.javapoet.CodeBlock.Builder;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.ygmodesto.modernfit.converters.Converter;
import com.ygmodesto.modernfit.processor.ModernfitProcessorException;
import com.ygmodesto.modernfit.processor.Utils;
import com.ygmodesto.modernfit.processor.model.AbstractBodyInformation;
import com.ygmodesto.modernfit.processor.model.AnnotationInformation;
import com.ygmodesto.modernfit.processor.model.DiscreteBodyInformation;
import com.ygmodesto.modernfit.processor.model.FormUrlEncodedBodyInformation;
import com.ygmodesto.modernfit.processor.model.HeadersInformation;
import com.ygmodesto.modernfit.processor.model.InterfaceImplementationInformation;
import com.ygmodesto.modernfit.processor.model.MethodInformation;
import com.ygmodesto.modernfit.processor.model.MultipartBodyInformation;
import com.ygmodesto.modernfit.processor.model.ReturnInformation;
import com.ygmodesto.modernfit.processor.model.UrlInformation;
import com.ygmodesto.modernfit.processor.model.UrlInformation.Segment;
import com.ygmodesto.modernfit.services.AbstractInterfaceImpl;
import com.ygmodesto.modernfit.services.BodyContent;
import com.ygmodesto.modernfit.services.CustomType;
import com.ygmodesto.modernfit.services.FormUrlEncodedBody;
import com.ygmodesto.modernfit.services.HttpMethod;
import com.ygmodesto.modernfit.services.ModernfitException;
import com.ygmodesto.modernfit.services.MultipartBody;
import com.ygmodesto.modernfit.services.OneObjectDiscreteBody;
import com.ygmodesto.modernfit.services.RequestInfo;
import com.ygmodesto.modernfit.services.ResponseContent;
import com.ygmodesto.modernfit.services.TypedContent;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.functions.Supplier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Class that generates the code necessary to implement the interfaces annotated with 
 * {@code @Modernfit} using the help of javapoet.
 */
public abstract class CodeGenerator {

  public static final String REQUESTINFOBUILDER_NAME_PREFIX = "requestInfoBuilder";
  public static final String BODYBUILDER_NAME_PREFIX = "bodyBuilder";
  public static final String URLCONVERTER_NAME_PREFIX = "urlConverter";
  public static final String REQUESTCONVERTER_NAME_PREFIX = "requestConverter";
  public static final String RESPONSECONVERTER_NAME_PREFIX = "responseConverter";
  public static final String HTTPRESPONSECONVERTER_NAME_PREFIX = "httpResponseConverter";

  public static final String CONVERTERFACTORY_NAME = "converterFactory";
  public static final String HTTPCLIENT_NAME = "httpClient";

  public static final String JACKSON_OBJECTMAPPER_GETTER_NAME = "getObjectMapper";
  public static final String GSON_OBJECTMAPPER_GETTER_NAME = "getGson";
  public static final String OKHTTP_OKHTTPCLIENT_GETTER_NAME = "getOkHttpClient";

  public static final TypeName stringTypeName = TypeName.get(String.class);
  public static final ClassName mapClassName = ClassName.get(Map.class);
  public static final ClassName hashMapClassName = ClassName.get(HashMap.class);
  public static final ClassName converterClassName = ClassName.get(Converter.class);
  public static final ClassName callableClassName = ClassName.get(Callable.class);
  public static final ClassName supplierClassName = ClassName.get(Supplier.class);
  public static final TypeName bodyContentTypeName = TypeName.get(BodyContent.class);
  public static final TypeName responseContentTypeName = TypeName.get(ResponseContent.class);

  protected ClassName customType = ClassName.get(CustomType.class);
  protected MetaSpec metaSpec;

  private NameHelper nameHelper = NameHelper.getInstance();

  private Utils utils;

  public CodeGenerator(Utils utils) {
    this.utils = utils;
  }

  public Utils getUtils() {
    return utils;
  }

  public void setUtils(Utils utils) {
    this.utils = utils;
  }

  protected abstract void createMetaSpec(String requestInfoBuilderName, String bodyBuilderName);

  protected abstract TypeSpec.Builder generateInterfaceImplementationClass(
      InterfaceImplementationInformation interfaceImplementationInformation);

  public abstract TypeSpec buildAll();

  /**
   * Constructs a {@code TypeSpec} of an implementation of an interface contained 
   * in interfaceImplementacionInformation.
   *
   * @param interfaceImplementationInformation all the interface information to implement.
   * @return un TypeSpec representing the interface implementation.
   * @throws ModernfitProcessorException if any errors are detected during processing.
   */
  public TypeSpec generateInterfaceImplementation(
      InterfaceImplementationInformation interfaceImplementationInformation)
          throws ModernfitProcessorException {
    List<String> conflictsNames = getConflictsNames(interfaceImplementationInformation);
    createMetaSpec(
        nameHelper.generateFreeName(REQUESTINFOBUILDER_NAME_PREFIX, conflictsNames),
        nameHelper.generateFreeName(BODYBUILDER_NAME_PREFIX, conflictsNames));

    TypeSpec.Builder implementationBuilder =
        generateInterfaceImplementationClass(interfaceImplementationInformation);
    implementationBuilder.superclass(AbstractInterfaceImpl.class);
    implementationBuilder.addField(
        FieldSpec.builder(
            ClassName.get(interfaceImplementationInformation.getConverterFactory()),
            CONVERTERFACTORY_NAME,
            Modifier.PRIVATE)
        .build());

    this.metaSpec.setImplementationBuilder(implementationBuilder);

    for (MethodInformation mi : interfaceImplementationInformation.getMethodsInformation()) {
      metaSpec.addInterfaceMethod(generateMethod(mi));
    }

    return buildAll();
  }

  /**
   * Generate a {@code Builder} of {@code MethodSpec} from a MethodInformation.
   *
   * @param methodInformation all the information to create the {@code MethodSpec.Builder}
   * @return an MethodSpec.Builder.
   * @throws ModernfitProcessorException if any errors are detected during processing.
   */
  public MethodSpec.Builder generateMethod(MethodInformation methodInformation)
      throws ModernfitProcessorException {

    ExecutableElement executableElement = methodInformation.getExecutableElement();
    HeadersInformation headersInformation = methodInformation.getHeaderInformation();
    UrlInformation urlInformation = methodInformation.getUrlInformation();
    AbstractBodyInformation bodyInformation = methodInformation.getBodyInformation();

    // Method signature
    MethodSpec.Builder methodBuilder = methodSkeleton(methodInformation, executableElement);

    generateUrlBlock(methodBuilder, urlInformation);
    generateHeadersBlock(methodBuilder, headersInformation);
    generateBodyBlock(methodBuilder, bodyInformation);

    generateReturnStatement(methodBuilder, methodInformation.getReturnInformation());

    return methodBuilder;
  }


  private ConverterSpec registerUrlConvertersField(TypeName sourceTypeName) {

    sourceTypeName = sourceTypeName.isPrimitive() ? sourceTypeName.box() : sourceTypeName;
    ParameterizedTypeName requestConverterTypeName =
        ParameterizedTypeName.get(converterClassName, sourceTypeName, stringTypeName);

    FieldSpec urlConverterField =
        FieldSpec.builder(
            requestConverterTypeName,
            URLCONVERTER_NAME_PREFIX + metaSpec.getUrlConverters().size())
        .addModifiers(Modifier.PRIVATE)
        .build();

    ConverterSpec converterSpec =
        new ConverterSpec(urlConverterField, sourceTypeName, stringTypeName);
    boolean added = metaSpec.addUrlConverter(converterSpec);
    if (added) {
      return converterSpec;
    } else {
      return metaSpec.getUrlConverterBySource(sourceTypeName);
    }
  }


  private ConverterSpec registerRequestConvertersField(TypeName sourceTypeName) {

    sourceTypeName = sourceTypeName.isPrimitive() ? sourceTypeName.box() : sourceTypeName;
    ParameterizedTypeName requestConverterTypeName =
        ParameterizedTypeName.get(converterClassName, sourceTypeName, bodyContentTypeName);

    FieldSpec requestConverterField =
        FieldSpec.builder(
            requestConverterTypeName,
            REQUESTCONVERTER_NAME_PREFIX + metaSpec.getRequestConverters().size())
        .addModifiers(Modifier.PRIVATE)
        .build();

    ConverterSpec converterSpec =
        new ConverterSpec(requestConverterField, sourceTypeName, bodyContentTypeName);
    boolean added = metaSpec.addRequestConverter(converterSpec);
    if (added) {
      return converterSpec;
    } else {
      return metaSpec.getRequestConverterBySource(sourceTypeName);
    }
  }

  private ConverterSpec registerResponseConvertersFields(ReturnInformation returnInformation) {

    TypeMirror returnBodyType = returnInformation.getReturnBodyType();
    TypeName destinationTypeName = TypeName.get(returnBodyType);

    destinationTypeName =
        destinationTypeName.isPrimitive() ? destinationTypeName.box() : destinationTypeName;
    ParameterizedTypeName responseConverterTypeName =
        ParameterizedTypeName.get(converterClassName, responseContentTypeName, destinationTypeName);

    FieldSpec responseConverterField =
        FieldSpec.builder(
                responseConverterTypeName,
                RESPONSECONVERTER_NAME_PREFIX + metaSpec.getResponseConverters().size())
            .addModifiers(Modifier.PRIVATE)
            .build();

    ConverterSpec converterSpec =
        new ConverterSpec(responseConverterField, responseContentTypeName, destinationTypeName);
    boolean added =
        metaSpec.addResponseConverter(
            new ConverterSpec(
                responseConverterField, responseContentTypeName, destinationTypeName));
    if (added) {
      return converterSpec;
    } else {
      return metaSpec.getResponseConverterByDestination(destinationTypeName);
    }
  }

  /**
   * Generates the code part of the method that defines the body 
   * of the FormUrlEncodedBodyInformation request.
   *
   * @param methodBuilder a {@code MethodSpec.Builder} in which to insert the code.
   * @param bodyInformation the information needed to generate the body.
   */
  public void generateBodyBlock(
      MethodSpec.Builder methodBuilder, FormUrlEncodedBodyInformation bodyInformation) {

    String bodyBuilderName = metaSpec.getBodyBuilderName();

    CodeBlock.Builder builder = CodeBlock.builder();

    builder.addStatement(
        "final $T.Builder $L = $T.builder()",
        FormUrlEncodedBody.class,
        bodyBuilderName,
        FormUrlEncodedBody.class);
    for (AnnotationInformation fieldInformation : bodyInformation.getFields()) {

      ConverterSpec requestConvertersField =
          registerRequestConvertersField(
              TypeName.get(getBodyFieldTypeMirror(fieldInformation.getVariableElement().asType())));
      String statement =
          fieldInformation.isEncoded()
          ? "$L.addField($S, $L, this.$N)"
              : "$L.addFieldNotEncoded($S, $L, this.$N)";
      builder.addStatement(
          statement,
          bodyBuilderName,
          fieldInformation.getName(),
          fieldInformation.getVariableName(),
          requestConvertersField.getField());
    }
    for (AnnotationInformation fieldMapInformation : bodyInformation.getFieldsMap()) {

      ConverterSpec requestConvertersField =
          registerRequestConvertersField(
              TypeName.get(
                  utils.getSecondTypeArgument(fieldMapInformation.getVariableElement().asType())));
      String statement =
          fieldMapInformation.isEncoded()
          ? "$L.addFieldMap($L, this.$N)"
              : "$L.addFieldMapNotEncoded($L, this.$N)";
      builder.addStatement(
          statement,
          bodyBuilderName,
          fieldMapInformation.getVariableName(),
          requestConvertersField.getField());
    }
    methodBuilder.addCode(builder.build());
  }

  /**
   * Generates the code part of the method that defines the body 
   * of the MultipartBodyInformation request.
   *
   * @param methodBuilder a {@code MethodSpec.Builder} in which to insert the code.
   * @param bodyInformation the information needed to generate the body.
   */
  public void generateBodyBlock(
      MethodSpec.Builder methodBuilder, MultipartBodyInformation bodyInformation) {

    String bodyBuilderName = metaSpec.getBodyBuilderName();

    CodeBlock.Builder builder = CodeBlock.builder();

    builder.addStatement(
        "final $T.Builder $L = $T.builder()",
        MultipartBody.class,
        bodyBuilderName,
        MultipartBody.class);
    for (AnnotationInformation partInformation : bodyInformation.getParts()) {
      // TODO revisar estas comparaciones, mirar bien que sean hijos de iterable

      if (utils.isSameType(partInformation.getVariableElement().asType(), TypedContent.class)
          || utils.isAllSubtypes(
              partInformation.getVariableElement().asType(), Iterable.class, TypedContent.class)
          || utils.isSubtypeArray(
              partInformation.getVariableElement().asType(), TypedContent.class)) {
        builder.addStatement(
            "$L.addPart($S, $L)",
            bodyBuilderName,
            partInformation.getName(),
            partInformation.getVariableName());
      } else {
        ConverterSpec requestConvertersField =
            registerRequestConvertersField(
                TypeName.get(partInformation.getVariableElement().asType()));
        builder.addStatement(
            "$L.addPart($S, $L, this.$N)",
            bodyBuilderName,
            partInformation.getName(),
            partInformation.getVariableName(),
            requestConvertersField.getField());
      }
    }

    // TODO tener en cuenta los que no son typedContent, objetos normales
    for (VariableElement variableElement : bodyInformation.getPartsMap()) {
      if (utils.isAllSubtypes(
          variableElement.asType(), Map.class, Object.class, TypedContent.class)) {
        builder.addStatement("$L.addPart($L)", bodyBuilderName, variableElement.getSimpleName());
      } else if (utils.isAllSubtypes(
          variableElement.asType(), Map.class, Object.class, Object.class)) {
        ConverterSpec requestConvertersField =
            registerRequestConvertersField(
                TypeName.get(utils.getSecondTypeArgument(variableElement.asType())));
        builder.addStatement(
            "$L.addPart($L, this.$N)",
            bodyBuilderName,
            variableElement.getSimpleName(),
            requestConvertersField.getField());
      } else {
        // TODO Error y test
      }
    }

    methodBuilder.addCode(builder.build());
  }

  /**
   * Generates the code part of the method that defines the body 
   * of the DiscreteBodyInformation request.
   *
   * @param methodBuilder a {@code MethodSpec.Builder} in which to insert the code.
   * @param bodyInformation the information needed to generate the body.
   */
  public void generateBodyBlock(
      MethodSpec.Builder methodBuilder, DiscreteBodyInformation bodyInformation) {

    String bodyBuilderName = metaSpec.getBodyBuilderName();

    CodeBlock.Builder builder = CodeBlock.builder();
    TypeName bodyTypeName =
        bodyInformation.getBody() != null
        ? TypeName.get(bodyInformation.getBody().asType()).box()
            : TypeName.get(Void.class);

    builder.addStatement(
        "final $T.Builder<$T> $L = $T.builder()",
        OneObjectDiscreteBody.class,
        bodyTypeName,
        bodyBuilderName,
        OneObjectDiscreteBody.class);
    ConverterSpec requestConvertersField = registerRequestConvertersField(bodyTypeName);
    builder.addStatement(
        "$L.addConverter(this.$N)", bodyBuilderName, requestConvertersField.getField());
    if (bodyInformation.getBody() != null) {
      builder.addStatement("$L.addBody($L)", bodyBuilderName, bodyInformation.getBody());
    }

    methodBuilder.addCode(builder.build());
  }

  /**
   * Generates the code part of the method that defines the body request.
   *
   * @param methodBuilder a {@code MethodSpec.Builder} in which to insert the code.
   * @param abstractBodyInformation the information needed to generate the body.
   */
  public void generateBodyBlock(
      MethodSpec.Builder methodBuilder, AbstractBodyInformation abstractBodyInformation) {

    if (abstractBodyInformation instanceof MultipartBodyInformation) {
      MultipartBodyInformation multipartBodyInformation =
          MultipartBodyInformation.class.cast(abstractBodyInformation);
      generateBodyBlock(methodBuilder, multipartBodyInformation);
    } else if (abstractBodyInformation instanceof FormUrlEncodedBodyInformation) {
      FormUrlEncodedBodyInformation formUrlEncodedBodyInformation =
          FormUrlEncodedBodyInformation.class.cast(abstractBodyInformation);
      generateBodyBlock(methodBuilder, formUrlEncodedBodyInformation);
    } else {
      DiscreteBodyInformation discreteBodyInformation =
          DiscreteBodyInformation.class.cast(abstractBodyInformation);
      generateBodyBlock(methodBuilder, discreteBodyInformation);
    }
  }

  /**
   * Generates the code part of the method that defines the url of the request.
   *
   * @param methodBuilder a {@code MethodSpec.Builder} in which to insert the code.
   * @param urlInformation the information to generate the url.
   */
  public void generateUrlBlock(MethodSpec.Builder methodBuilder, UrlInformation urlInformation) {

    String requestInfoBuilderName = metaSpec.getRequestInfoBuilderName();

    CodeBlock.Builder builder = CodeBlock.builder();

    if (!urlInformation.containsParameterUrl()) {
      builder.addStatement(
          "final $T.Builder $L = $T.baseUrl(this.baseUrl)",
          RequestInfo.class,
          requestInfoBuilderName,
          RequestInfo.class);
    } else {
      builder.addStatement(
          "final $T.Builder $L = $T.baseUrl($L)",
          RequestInfo.class,
          requestInfoBuilderName,
          RequestInfo.class,
          urlInformation.getParameterUrl().getSimpleName());
    }

    
    for (Segment segment : urlInformation.getSegmentsUrl()) {
      if (segment.isExpression()) {
        AnnotationInformation pathParameter =
            urlInformation.getPaths().get(segment.getValue());
        ConverterSpec converterSpec =
            registerUrlConvertersField(
                TypeName.get(pathParameter.getVariableElement().asType()));
        String statement =
            pathParameter.isEncoded()
            ? "$L.addUrlPath($L, this.$N)"
                : "$L.addUrlPathNotEncoded($L, this.$N)";
        builder.addStatement(
            statement,
            requestInfoBuilderName,
            pathParameter.getVariableName(),
            converterSpec.getField());
      } else {
        builder.addStatement(
            "$L.addUrlPathAsConstant($S)", requestInfoBuilderName, segment.getValue());
      }
    }

    builder.addStatement(
        "$N.addHttpMethod($T.$L)",
        requestInfoBuilderName,
        HttpMethod.class,
        urlInformation.getMethodInformation().getHttpMethod());

    for (AnnotationInformation queryParameter : urlInformation.getQueries()) {
      ConverterSpec converterSpec =
          registerUrlConvertersField(
              TypeName.get(getBodyFieldTypeMirror(queryParameter.getVariableElement().asType())));
      String statement =
          queryParameter.isEncoded()
          ? "$N.addParameter($S, $L, this.$N)"
              : "$N.addParameterNotEncoded($S, $L, this.$N)";
      builder.addStatement(
          statement,
          requestInfoBuilderName,
          queryParameter.getName(),
          queryParameter.getVariableName(),
          converterSpec.getField());
    }
    for (AnnotationInformation queryMapInformation : urlInformation.getQueriesMaps()) {

      VariableElement queryMap = queryMapInformation.getVariableElement();
      //TypeMirror firstTypeArgument = utils.getFirstTypeArgument(queryMap.asType());
      TypeMirror secondTypeArgument = utils.getSecondTypeArgument(queryMap.asType());
      //TypeName mapEntryTypeName = ParameterizedTypeName.get(mapEntryclassName,
      //TypeName.get(firstTypeArgument), TypeName.get(secondTypeArgument));
      ConverterSpec converterSpec =
          registerUrlConvertersField(TypeName.get(secondTypeArgument));

      String statement =
          queryMapInformation.isEncoded()
          ? "$N.addParameterMap($L, this.$N)"
              : "$N.addParameterMapNotEncoded($L, this.$N)";
      //builder.beginControlFlow("if ($L != null)", queryMap.getSimpleName().toString());
      //builder.beginControlFlow("for ($T entry : $L.entrySet())", mapEntryTypeName,
      // queryMap.getSimpleName().toString());
      builder.addStatement(
          statement, requestInfoBuilderName, queryMap.getSimpleName(), converterSpec.getField());
      //builder.endControlFlow();
      //builder.endControlFlow();
    }

    methodBuilder.addCode(builder.build());
  }

  /**
   * Generates the code part of the method that defines the request headers.
   *
   * @param methodBuilder a {@code MethodSpec.Builder} in which to insert the code.
   * @param headersInformation the information to generate the headers.
   */
  public void generateHeadersBlock(
      MethodSpec.Builder methodBuilder, HeadersInformation headersInformation) {

    String requestInfoBuilderName = metaSpec.getRequestInfoBuilderName();

    methodBuilder.addStatement("$N.addHeaders(this.getHeaders())", requestInfoBuilderName);

    Map<String, String> headers = headersInformation.getHeaders();
    List<AnnotationInformation> headersParameter = headersInformation.getHeadersParameter();
    List<VariableElement> headersMap = headersInformation.getHeadersMap();

    for (Map.Entry<String, String> entry : headers.entrySet()) {
      methodBuilder.addStatement(
          "$N.addHeader($S, $S)", requestInfoBuilderName, entry.getKey(), entry.getValue());
    }
    for (AnnotationInformation headerParameter : headersParameter) {
      methodBuilder.addStatement(
          "$N.addHeader($S, $L)",
          requestInfoBuilderName,
          headerParameter.getName(),
          headerParameter.getVariableName());
    }

    for (VariableElement headerMap : headersMap) {
      methodBuilder.addStatement(
          "$N.addHeaders($L)", requestInfoBuilderName, headerMap.getSimpleName());
    }
  }

  private MethodSpec.Builder methodSkeleton(
      MethodInformation methodInformation, ExecutableElement executableElement) {

    MethodSpec.Builder methodBuilder =
        MethodSpec.methodBuilder(methodInformation.getMethodName())
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .addException(ModernfitException.class)
        .returns(TypeName.get(methodInformation.getReturnInformation().getReturnType()));

    for (VariableElement va : executableElement.getParameters()) {
      methodBuilder.addParameter(TypeName.get(va.asType()), va.getSimpleName().toString());
    }

    return methodBuilder;
  }

  private void generateReturnStatement(
      MethodSpec.Builder methodBuilder, ReturnInformation returnInformation) {

    if (returnInformation.isRxJava2()) {
      rxJava2HttpCallStatement(methodBuilder, returnInformation);
    } else if (returnInformation.isRxJava3()) {
      rxJava3HttpCallStatement(methodBuilder, returnInformation);
    } else if (returnInformation.isAsynchronos()) {
      asyncHttpCallStatement(methodBuilder, returnInformation);
    } else {
      syncHttpCallStatement(methodBuilder, returnInformation);
    }
  }

  private void syncHttpCallStatement(
      MethodSpec.Builder methodBuilder, ReturnInformation returnInformation) {

    if (utils.isVoid(returnInformation.getReturnType())) {
      methodBuilder.addStatement(
          "this.httpClient.callMethod($N.build(), $L.build())",
          metaSpec.getRequestInfoBuilderName(),
          metaSpec.getBodyBuilderName());
    } else {
      String statement =
          (returnInformation.isHttpInfo())
          ? "return this.toHttpInfo(this.$N, this.httpClient.callMethod($N.build(), $L.build()))"
              : "return this.$N.convert(this.httpClient.callMethod($N.build(), $L.build()))";

      ConverterSpec converterSpec = registerResponseConvertersFields(returnInformation);
      methodBuilder.addStatement(
          statement,
          converterSpec.getField(),
          metaSpec.getRequestInfoBuilderName(),
          metaSpec.getBodyBuilderName());
    }
  }

  private void asyncHttpCallStatement(
      MethodSpec.Builder methodBuilder, ReturnInformation returnInformation) {

    String callbackName = returnInformation.getCallback().getSimpleName().toString();
    ConverterSpec converterSpec = registerResponseConvertersFields(returnInformation);

    methodBuilder.addStatement("$L.setConverter(this.$N)", callbackName, converterSpec.getField());
    methodBuilder.addStatement(
        "this.httpClient.callMethod($N.build(), $L.build(), $L)",
        metaSpec.getRequestInfoBuilderName(),
        metaSpec.getBodyBuilderName(),
        callbackName);
  }

  private void rxJava2HttpCallStatement(
      MethodSpec.Builder methodBuilder, ReturnInformation returnInformation) {

    String interfaceImplementationName =
        returnInformation
        .getMethodInformation()
        .getInterfaceImplementationInformation()
        .getImplementationName();

    TypeName returnTypeName = TypeName.get(returnInformation.getReturnType());
    TypeName rxJavaTypeName = TypeName.get(utils.getErasureType(returnInformation.getReturnType()));
    ParameterizedTypeName callableTypeName =
        ParameterizedTypeName.get(callableClassName, returnTypeName);

    if (utils.isSameType(returnInformation.getReturnType(), io.reactivex.Completable.class)) {

      TypeSpec anonymousCallable =
          TypeSpec.anonymousClassBuilder("")
          .addSuperinterface(callableTypeName)
          .addMethod(
              MethodSpec.methodBuilder("call")
              .addModifiers(Modifier.PUBLIC)
              .beginControlFlow("try")
              .addStatement(
                  "$L.this.httpClient.callMethod($N.build(), $L.build())",
                  interfaceImplementationName,
                  metaSpec.getRequestInfoBuilderName(),
                  metaSpec.getBodyBuilderName())
              .addStatement("return $T.complete()", rxJavaTypeName)
              .nextControlFlow("catch ($T e)", Exception.class)
              .addStatement("return $T.error(e)", rxJavaTypeName)
              .endControlFlow()
              .returns(returnTypeName)
              .build())
          .build();

      methodBuilder.addStatement("return $T.defer($L)", rxJavaTypeName, anonymousCallable);
    } else {
      String statement =
          (returnInformation.isHttpInfo())
          ? String.format(
              "return $T.just(%s.this.toHttpInfo("
              + "$L.this.$N, $L.this.httpClient.callMethod($N.build(), $L.build())))",
              interfaceImplementationName)
              : "return $T.just("
                  + "$L.this.$N.convert($L.this.httpClient.callMethod($N.build(), $L.build())))";
      ConverterSpec converterSpec = registerResponseConvertersFields(returnInformation);
      TypeSpec anonymousCallable =
          TypeSpec.anonymousClassBuilder("")
          .addSuperinterface(callableTypeName)
          .addMethod(
              MethodSpec.methodBuilder("call")
              .addModifiers(Modifier.PUBLIC)
              .beginControlFlow("try")
              .addStatement(
                  statement,
                  rxJavaTypeName,
                  interfaceImplementationName,
                  converterSpec.getField(),
                  interfaceImplementationName,
                  metaSpec.getRequestInfoBuilderName(),
                  metaSpec.getBodyBuilderName())
              .nextControlFlow("catch ($T e)", Exception.class)
              .addStatement("return $T.error(e)", rxJavaTypeName)
              .endControlFlow()
              .returns(returnTypeName)
              .build())
          .build();

      methodBuilder.addStatement("return $T.defer($L)", rxJavaTypeName, anonymousCallable);
    }
  }

  private void rxJava3HttpCallStatement(
      MethodSpec.Builder methodBuilder, ReturnInformation returnInformation) {

    String interfaceImplementationName =
        returnInformation
            .getMethodInformation()
            .getInterfaceImplementationInformation()
            .getImplementationName();

    TypeName rxJavaTypeName = TypeName.get(utils.getErasureType(returnInformation.getReturnType()));

    if (utils.isSameType(returnInformation.getReturnType(), Completable.class)) {

      Builder supplierGetBuilder = CodeBlock.builder();
      supplierGetBuilder.beginControlFlow("");
      supplierGetBuilder.addStatement(
          "$L.this.httpClient.callMethod($N.build(), $L.build())",
          interfaceImplementationName,
          metaSpec.getRequestInfoBuilderName(),
          metaSpec.getBodyBuilderName());
      supplierGetBuilder.addStatement("return $T.complete()", rxJavaTypeName);
      supplierGetBuilder.endControlFlow(")");
      CodeBlock supplierGet = supplierGetBuilder.build();
      methodBuilder.addCode("return $T.defer(() -> ", rxJavaTypeName);
      methodBuilder.addCode(supplierGet);

    } else {

      String statement =
          (returnInformation.isHttpInfo())
              ? String.format(
                  "return $T.defer(() -> $T.just(%s.this.toHttpInfo("
                  + "$L.this.$N, $L.this.httpClient.callMethod($N.build(), $L.build()))))",
                  interfaceImplementationName)
              : "return $T.defer(() -> $T.just("
                  + "$L.this.$N.convert($L.this.httpClient.callMethod($N.build(), $L.build()))))";

      ConverterSpec converterSpec = registerResponseConvertersFields(returnInformation);

      methodBuilder.addStatement(
          statement,
          rxJavaTypeName,
          rxJavaTypeName,
          interfaceImplementationName,
          converterSpec.getField(),
          interfaceImplementationName,
          metaSpec.getRequestInfoBuilderName(),
          metaSpec.getBodyBuilderName());
    }
  }

  private List<String> getConflictsNames(
      InterfaceImplementationInformation interfaceImplementationInformation) {

    List<String> conflictsNames = new ArrayList<>();
    for (MethodInformation methodInformation :
        interfaceImplementationInformation.getMethodsInformation()) {

      for (VariableElement va : methodInformation.getExecutableElement().getParameters()) {
        conflictsNames.add(va.getSimpleName().toString());
      }
    }

    return conflictsNames;
  }

  private TypeMirror getBodyFieldTypeMirror(TypeMirror bodyTypeMirror) {
    if (utils.isSubtype(bodyTypeMirror, Iterable.class)) {
      TypeMirror firstTypeArgument = utils.getFirstTypeArgument(bodyTypeMirror);
      return firstTypeArgument == null ? utils.getErasureType(bodyTypeMirror) : firstTypeArgument;
    } else if (utils.isArrayType(bodyTypeMirror)) {
      return utils.getTypeOfArray(bodyTypeMirror);
    } else {
      return bodyTypeMirror;
    }
  }
}
