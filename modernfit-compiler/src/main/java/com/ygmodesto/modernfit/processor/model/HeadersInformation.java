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

import com.ygmodesto.modernfit.annotations.Header;
import com.ygmodesto.modernfit.processor.ModernfitProcessorException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.VariableElement;

/**
 * Defines the information necessary to build the headers of a method.
 */
public class HeadersInformation {

  private MethodInformation methodInformation;

  private Map<String, String> headers;
  private List<AnnotationInformation> headersParameter;
  private List<VariableElement> headersMap;

  private HeadersInformation(Builder builder) throws ModernfitProcessorException {

    this.methodInformation = builder.methodInformation;
    this.headers = builder.headers == null ? new HashMap<>() : builder.headers;
    headersAsStringsToHeaders(headers, builder.headersAsStrings);
    this.headersParameter =
        builder.headersParameter == null ? Collections.emptyList() : builder.headersParameter;
    this.headersMap = builder.headersMap == null ? Collections.emptyList() : builder.headersMap;
  }

  public MethodInformation getMethodInformation() {
    return methodInformation;
  }

  public Map<String, String> getHeaders() {
    return headers == null ? Collections.emptyMap() : headers;
  }

  public List<AnnotationInformation> getHeadersParameter() {
    return headersParameter == null ? Collections.emptyList() : headersParameter;
  }

  public List<VariableElement> getHeadersMap() {
    return headersMap == null ? Collections.emptyList() : headersMap;
  }

  public boolean containsHeaders() {
    return (!headers.isEmpty() || !headersParameter.isEmpty() || !headersMap.isEmpty());
  }

  /**
   * Given a Map of headers saved as key-value and a list of Headers saved in Strings 
   * with the "key: value" format, process all the headers in the list and save them 
   * in the Map with the key-value format dividing them by the " : ".
   *
   * @param headers an initialized Map.
   * @param headersAsString list of headers.
   * @throws ModernfitProcessorException in the case of a malformed header.
   */
  public void headersAsStringsToHeaders(Map<String, String> headers, List<String> headersAsString)
      throws ModernfitProcessorException {

    if (headersAsString == null) {
      return;
    }

    for (String header : headersAsString) {
      String[] nameAndValue = header.split(":", 2);
      if (nameAndValue.length != 2) {
        throw new ModernfitProcessorException(
            "Malformed headers in @Headers annotation", methodInformation.getExecutableElement());
      }
      this.headers.put(nameAndValue[0], nameAndValue[1]);
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  /** 
   * Builder class for {@link HeadersInformation HeadersInformation}. 
   */
  public static final class Builder {
    
    protected static final String EMPTY_STRING = "";

    private MethodInformation methodInformation;

    private Map<String, String> headers;
    private List<String> headersAsStrings;
    private List<AnnotationInformation> headersParameter;
    private List<VariableElement> headersMap;

    public Builder addMethodInformation(MethodInformation methodInformation) {
      this.methodInformation = methodInformation;
      return this;
    }

    /**
     * Add a header to the builder from name and value. 
     */
    public Builder addHeader(String name, String value) {
      if (headers == null) {
        headers = new HashMap<String, String>();
      }

      headers.put(name, value);
      return this;
    }

    /**
     * Add to the builder a list of headers stored in a Map in key-value format.
     */
    public Builder addHeaders(Map<String, String> headers) {
      if (headers == null) {
        headers = new HashMap<String, String>();
      }
      this.headers.putAll(headers);
      return this;
    }

    /**
     * Add to the builder an array of headers in "key: value" format.  
     */
    public Builder addHeaders(String[] headers) {
      if (headersAsStrings == null) {
        headersAsStrings = new ArrayList<String>();
      }

      headersAsStrings.addAll(Arrays.asList(headers));

      return this;
    }

    /**
     * Add to the builder a variable that will add a header.  
     */
    public Builder addHeaderAsParameter(VariableElement variableElement) {

      Header header = variableElement.getAnnotation(Header.class);

      return addHeaderAsParameter(header, variableElement);
    }

    /**
     * Add to the builder a variable that will add a header from the Header annotation 
     * and the VariableElement annotated by it.
     */
    public Builder addHeaderAsParameter(Header header, VariableElement va) {
      if (headersParameter == null) {
        headersParameter = new ArrayList<AnnotationInformation>();
      }

      String headerName =
          (header.value().equals(EMPTY_STRING))
              ? va.getSimpleName().toString()
              : header.value();
      headersParameter.add(new AnnotationInformation(headerName, va));

      return this;
    }

    /**
     * Add to the builder a variable of type Map that will add a list of headers 
     * stored in a Map in key-value format.
     */
    public Builder addHeaderMap(VariableElement va) {
      if (headersMap == null) {
        headersMap = new ArrayList<VariableElement>();
      }

      headersMap.add(va);
      return this;
    }

    public HeadersInformation build() throws ModernfitProcessorException {
      return new HeadersInformation(this);
    }
  }
}
