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

import com.ygmodesto.modernfit.annotations.Body;
import com.ygmodesto.modernfit.annotations.DELETE;
import com.ygmodesto.modernfit.annotations.Field;
import com.ygmodesto.modernfit.annotations.FieldMap;
import com.ygmodesto.modernfit.annotations.FormUrlEncoded;
import com.ygmodesto.modernfit.annotations.GET;
import com.ygmodesto.modernfit.annotations.HEAD;
import com.ygmodesto.modernfit.annotations.Header;
import com.ygmodesto.modernfit.annotations.HeaderMap;
import com.ygmodesto.modernfit.annotations.Headers;
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
import com.ygmodesto.modernfit.processor.ModernfitProcessorException;
import com.ygmodesto.modernfit.processor.Utils;
import com.ygmodesto.modernfit.services.HttpMethod;
import com.ygmodesto.modernfit.services.ResponseCallback;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

/**
 * Defines all the information necessary to generate a method that performs an HTTP request.
 */
public class MethodInformation {

  private Utils utils = Utils.getInstance();

  private InterfaceImplementationInformation interfaceImplementationInformation;

  private HttpMethod httpMethod;
  private String charset;

  private ExecutableElement executableElement;
  private String methodName;

  private AbstractBodyInformation bodyInformation;
  private UrlInformation urlInformation;
  private HeadersInformation headerInformation;
  private ReturnInformation returnInformation;

  private MethodInformation(Builder builder) throws ModernfitProcessorException {

    this.interfaceImplementationInformation = builder.interfaceImplementationInformation;
    this.executableElement = builder.executableElement;

    assert (interfaceImplementationInformation != null)
        : "interfaceImplementationInformation == null";
    assert (executableElement != null) : "executableElement == null";

    final AbstractBodyInformation.Builder builderBody;
    final UrlInformation.Builder builderUrl = UrlInformation.builder();
    final HeadersInformation.Builder builderHeaders = HeadersInformation.builder();
    final ReturnInformation.Builder builderReturn = ReturnInformation.builder();

    methodName = executableElement.getSimpleName().toString();

    builderBody = extractBodyType(executableElement);
    extractHeaders(builderHeaders);
    extractBodyInfo(builderUrl);
    builderUrl.addBaseUrl(builder.baseUrl);
    extractParametersInfo(builderBody, builderUrl, builderHeaders, builderReturn);

    builderReturn.addReturnType(executableElement.getReturnType());

    builderBody.addMethodInformation(this);
    this.bodyInformation = builderBody.build();
    builderUrl.addMethodInformation(this);
    this.urlInformation = builderUrl.build();
    builderHeaders.addMethodInformation(this);
    this.headerInformation = builderHeaders.build();
    builderReturn.addMethodInformation(this);
    this.returnInformation = builderReturn.build();
  }

  public InterfaceImplementationInformation getInterfaceImplementationInformation() {
    return interfaceImplementationInformation;
  }

  public String getMethodName() {
    return methodName;
  }

  public HttpMethod getHttpMethod() {
    return httpMethod;
  }

  public String getCharset() {
    return charset;
  }

  public UrlInformation getUrlInformation() {
    return urlInformation;
  }

  public HeadersInformation getHeaderInformation() {
    return headerInformation;
  }

  public ReturnInformation getReturnInformation() {
    return returnInformation;
  }

  public ExecutableElement getExecutableElement() {
    return executableElement;
  }

  public AbstractBodyInformation getBodyInformation() {
    return bodyInformation;
  }

  private AbstractBodyInformation.Builder extractBodyType(ExecutableElement executableElement)
      throws ModernfitProcessorException {
    FormUrlEncoded formUrlEncoded = executableElement.getAnnotation(FormUrlEncoded.class);
    Multipart multipart = executableElement.getAnnotation(Multipart.class);
    if ((formUrlEncoded != null) && (multipart != null)) {
      throw new ModernfitProcessorException(
          "@FormUlrEconded and @Multipart not supported together", executableElement);
    } else if (formUrlEncoded != null) {
      return FormUrlEncodedBodyInformation.builder();
    } else if (multipart != null) {
      return MultipartBodyInformation.builder();
    } else {
      return DiscreteBodyInformation.builder();
    }
  }

  private void extractBodyInfo(UrlInformation.Builder builderUrl)
      throws ModernfitProcessorException {

    final GET get = executableElement.getAnnotation(GET.class);
    final POST post = executableElement.getAnnotation(POST.class);
    final PUT put = executableElement.getAnnotation(PUT.class);
    final DELETE delete = executableElement.getAnnotation(DELETE.class);
    final PATCH patch = executableElement.getAnnotation(PATCH.class);
    final OPTIONS options = executableElement.getAnnotation(OPTIONS.class);
    final HEAD head = executableElement.getAnnotation(HEAD.class);

    if (get != null) {
      httpMethod = HttpMethod.GET;
      builderUrl.addMethodUrl(get.value());
    }

    if (notNullNotProcessYet(post, HttpMethod.POST, httpMethod)) {
      httpMethod = HttpMethod.POST;
      builderUrl.addMethodUrl(post.value());
    }

    if (notNullNotProcessYet(put, HttpMethod.PUT, httpMethod)) {
      httpMethod = HttpMethod.PUT;
      builderUrl.addMethodUrl(put.value());
    }

    if (notNullNotProcessYet(delete, HttpMethod.DELETE, httpMethod)) {
      httpMethod = HttpMethod.DELETE;
      builderUrl.addMethodUrl(delete.value());
    }

    if (notNullNotProcessYet(patch, HttpMethod.PATCH, httpMethod)) {
      httpMethod = HttpMethod.PATCH;
      builderUrl.addMethodUrl(patch.value());
    }

    if (notNullNotProcessYet(options, HttpMethod.OPTIONS, httpMethod)) {
      httpMethod = HttpMethod.OPTIONS;
      builderUrl.addMethodUrl(options.value());
    }

    if (notNullNotProcessYet(head, HttpMethod.HEAD, httpMethod)) {
      httpMethod = HttpMethod.HEAD;
      builderUrl.addMethodUrl(head.value());
    }

    if (httpMethod == null) {
      throw new ModernfitProcessorException("Method not annotated", executableElement);
    }
  }

  private boolean notNullNotProcessYet(
      Object annotation, HttpMethod annotationHttpMethod, HttpMethod httpMethod)
      throws ModernfitProcessorException {

    if ((annotation != null) && (httpMethod == null)) {
      return true;
    } else if ((annotation != null) && (httpMethod != null)) {
      throw new ModernfitProcessorException(
          String.format(
              "@%s and @%s founds, method can only contains one", annotationHttpMethod, httpMethod),
          executableElement);
    }

    return false;
  }

  private void extractHeaders(HeadersInformation.Builder builderHeaders)
      throws ModernfitProcessorException {
    Headers headers = executableElement.getAnnotation(Headers.class);
    if (headers != null) {
      builderHeaders.addHeaders(headers.value());
    }
  }

  private void extractParametersInfo(
      final AbstractBodyInformation.Builder builderBody,
      final UrlInformation.Builder builderUrl,
      final HeadersInformation.Builder builderHeaders,
      final ReturnInformation.Builder builderReturn) {

    for (VariableElement va : executableElement.getParameters()) {
      Header headerAnnotation;
      Field fieldAnnotation;
      FieldMap fieldMapAnnotation;
      Part partAnnotation;
      QueryMap queryMapAnnotation;
      Path pathAnnotation;
      Query queryAnnotation;

      if ((pathAnnotation = va.getAnnotation(Path.class)) != null) {
        builderUrl.addPath(pathAnnotation, va);
      } else if ((queryAnnotation = va.getAnnotation(Query.class)) != null) {
        builderUrl.addQuery(queryAnnotation, va);
      } else if ((queryMapAnnotation = va.getAnnotation(QueryMap.class)) != null) {
        builderUrl.addQueryMap(queryMapAnnotation, va);
      } else if (va.getAnnotation(Body.class) != null) {
        builderBody.addBody(va);
      } else if ((headerAnnotation = va.getAnnotation(Header.class)) != null) {
        builderHeaders.addHeaderAsParameter(headerAnnotation, va);
      } else if (va.getAnnotation(HeaderMap.class) != null) {
        builderHeaders.addHeaderMap(va);
      } else if ((fieldAnnotation = va.getAnnotation(Field.class)) != null) {
        builderBody.addField(fieldAnnotation, va);
      } else if ((fieldMapAnnotation = va.getAnnotation(FieldMap.class)) != null) {
        builderBody.addFieldMap(fieldMapAnnotation, va);
      } else if ((partAnnotation = va.getAnnotation(Part.class)) != null) {
        builderBody.addPart(partAnnotation, va);
      } else if (va.getAnnotation(PartMap.class) != null) {
        builderBody.addPartMap(va);
      } else if (va.getAnnotation(Url.class) != null) {
        builderUrl.addParameterUrl(va);
      } else if (utils.isSubtype(va.asType(), ResponseCallback.class)) {
        builderReturn.addCallback(va);
      }
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  /** 
   * Builder class for {@link MethodInformation MethodInformation}. 
   */
  public static final class Builder {

    private InterfaceImplementationInformation interfaceImplementationInformation;

    private ExecutableElement executableElement;
    private String baseUrl;

    public Builder addInterfaceImplementationInformation(
        InterfaceImplementationInformation interfaceImplementationInformation) {
      this.interfaceImplementationInformation = interfaceImplementationInformation;
      return this;
    }

    public Builder addExecutableElement(ExecutableElement executableElement) {
      this.executableElement = executableElement;
      return this;
    }

    public Builder addBaseUrl(String baseUrl) {
      this.baseUrl = baseUrl;
      return this;
    }

    public MethodInformation build() throws ModernfitProcessorException {

      return new MethodInformation(this);
    }
  }
}
