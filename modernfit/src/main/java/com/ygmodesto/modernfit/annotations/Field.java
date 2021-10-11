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

import static java.lang.annotation.ElementType.PARAMETER;

import com.ygmodesto.modernfit.services.Constants;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Named pair for a form-encoded request.
 *
 * <p>Values are converted to strings using {@link com.ygmodesto.modernfit.converters.Converter
 * Converter} and then form URL encoded. {@code null} values are ignored. Passing a {@link
 * java.util.List List} or array will result in a field pair for each non-{@code null} item.
 *
 * <p>Simple Example:
 *
 * <pre><code>
 * &#64;FormUrlEncoded
 * &#64;POST("/new")
 * User createUser(
 *     &#64;Field String name,
 *     &#64;Field String occupation);
 * </code></pre>
 *
 * <p>Calling with {@code foo.createUser("Bob Smith", "President")} yields a request body of {@code
 * name=Bob+Smith&occupation=President}.
 *
 * <p>Array/Varargs Example:
 *
 * <pre><code>
 * &#64;FormUrlEncoded
 * &#64;POST("/list")
 * List&lt;User&gt; getUsersByNames(@Field("name") String... names);
 * </code></pre>
 *
 * <p>Calling with {@code foo.getUsersByNames("Bob Smith", "Jane Doe")} yields a request body of
 * {@code name=Bob+Smith&name=Jane+Doe}.
 *
 * @see FormUrlEncoded
 * @see FieldMap
 */
@Documented
@Target(PARAMETER)
@Retention(RetentionPolicy.SOURCE)
public @interface Field {

  /**
   * Specifies the key value of the field.
   * If value is ignored, value will get the parameter name as value.
   */
  String value() default Constants.EMPTY_STRING;

  /** 
   * Specifies whether the {@linkplain #value() name} and value 
   * are already URL encoded. 
   */
  boolean encoded() default false;
}
