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

package com.ygmodesto.modernfit.annotations;

import com.ygmodesto.modernfit.converters.Converter;
import com.ygmodesto.modernfit.services.ClientOkHttp;
import com.ygmodesto.modernfit.services.Constants;
import com.ygmodesto.modernfit.services.HttpClient;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to indicate that it is an interface to which modernfit must generate an implementation
 * in addition to configuring certain parameters common to all the interface's methods.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Modernfit {

  /** Specifies the base URL that will be common to all interface methods. */
  String value() default Constants.EMPTY_STRING;

  /**
   * Specifies the suffix that will be concatenated to the name of the interface to name the
   * implementation. Default value: Impl.
   */
  String suffix() default Constants.IMPL_SUFFIX;

  /**
   * Sets which Http Client will be used in the deployment. Default value {@link ClientOkHttp
   * ClientOkHttp}.
   */
  Class<? extends HttpClient> client() default ClientOkHttp.class;

  /** Sets a converter factory for serialization and deserialization of objects. */
  Class<? extends Converter.Factory> converterFactory();

  /**
   * Sets the component model that indicates how the implementation will be instantiated.
   *
   * <p>Supported values are:
   *
   * <ul>
   *   <li>ComponentModel.STANDALONE:
   * </ul>
   */
  ComponentModel componentModel() default ComponentModel.STANDALONE;
}
