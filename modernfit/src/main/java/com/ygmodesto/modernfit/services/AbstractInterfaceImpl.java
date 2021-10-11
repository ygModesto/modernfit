/*
 * Copyright 2020 Yago Modesto González Diéguez
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ygmodesto.modernfit.services;

import com.ygmodesto.modernfit.converters.Converter;
import java.util.Map;

/**
 * Abstract class extended by modernfit-generated classes that implement interfaces annotated with
 * {@link com.ygmodesto.modernfit.annotations.Modernfit @Modernfit}.
 */
public abstract class AbstractInterfaceImpl implements ConfigurationInterface {

  protected HttpClient httpClient;

  protected String baseUrl;
  protected Map<String, String> headers;

  /** Returns the set headers to the class. These headers are common to all methods. */
  @Override
  public Map<String, String> getHeaders() {
    return this.headers;
  }

  /** The headers that will be common to all the methods of the class are set. */
  @Override
  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  /**
   * Returns the baseUrl used by all methods in the class. A method can ignore this value using
   * {@link com.ygmodesto.modernfit.annotations.Url @Url}.
   */
  @Override
  public String getBaseUrl() {
    return baseUrl;
  }

  /**
   * Returns the baseUrl used by all methods in the class. A method can ignore this value using
   * {@link com.ygmodesto.modernfit.annotations.Url @Url}.
   */
  @Override
  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  protected <T> HttpInfo<T> toHttpInfo(Converter<ResponseContent, T> converter,
      ResponseContent responseContent) {
    T value = converter.convert(responseContent);
    return new HttpInfo<T>(responseContent.getCode(), responseContent.getHeaders(), value);
  }

}
