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
 * Named replacement in a URL path segment. Values are converted to strings using {@link
 * com.ygmodesto.modernfit.converters.Converter Converter} and then URL encoded.
 *
 * <p>Simple example:
 *
 * <pre><code>
 * &#64;GET("/image/{id}")
 * Response example(@Path int id);
 * </code></pre>
 *
 * <p>Calling with {@code foo.example(1)} yields {@code /image/1}.
 *
 * <p>Values are URL encoded by default. Disable with {@code encoded=true}.
 *
 * <pre><code>
 * &#64;GET("/user/{name}")
 * Response encoded(@Path String name);
 *
 * &#64;GET("/user/{name}")
 * Response notEncoded(@Path(encoded=true) String name);
 * </code></pre>
 *
 * <p>Calling {@code foo.encoded("John+Doe")} yields {@code /user/John%2BDoe} whereas {@code
 * foo.notEncoded("John+Doe")} yields {@code /user/John+Doe}.
 *
 * <p>Path parameters may not be {@code null}.
 */
@Documented
@Target(PARAMETER)
@Retention(RetentionPolicy.SOURCE)
public @interface Path {

  /**
   * Specify which part of the url will be replaced.
   * If value is ignored, value will get the parameter name as value.
   */
  String value() default Constants.EMPTY_STRING;

  /**
   * Specifies whether the argument value to the annotated method parameter is already URL encoded.
   */
  boolean encoded() default false;
}
