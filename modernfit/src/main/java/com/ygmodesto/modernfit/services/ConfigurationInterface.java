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

package com.ygmodesto.modernfit.services;

import java.util.Map;

/**
 * Extending this interface by a class annotated with 
 * {@link com.ygmodesto.modernfit.annotations.Modernfit @Modernfit} 
 * will have access to get and set the common headers and the base url casa.
 */
public interface ConfigurationInterface {

  /** Returns the set headers to the class. These headers are common to all methods. */
  Map<String, String> getHeaders();

  /** The headers that will be common to all the methods of the class are set.. */
  void setHeaders(Map<String, String> headers);

  /**
   * Returns the baseUrl used by all methods in the class. A method can ignore this
   * value using {@link com.ygmodesto.modernfit.annotations.Url @Url}.
   */
  String getBaseUrl();

  /**
   * Set the {@code baseUrl} that will be used by all the methods of the class. A method can
   * ignore this value using {@link com.ygmodesto.modernfit.annotations.Url @Url}.
   */
  void setBaseUrl(String baseUrl);
}
