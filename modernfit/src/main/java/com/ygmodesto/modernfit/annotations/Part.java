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
 * Denotes a single part of a multi-part request.
 *
 * <p>The parameter type on which this annotation exists will be processed in one of three ways:
 *
 * <ul>
 *   <li>If the type is {@link com.ygmodesto.modernfit.services.TypedContent TypedContent} the value
 *       will be used directly with its content type. Provide the part name with the variable name
 *       or in the annotation (e.g., {@code @Part TypedContent foo}). If TypedContent.name is
 *       different from null, the name provided by the annotation or parameter name will be
 *       overwritten.
 *   <li>Other object types will be converted to an appropriate representation by using {@link
 *       com.ygmodesto.modernfit.converters.Converter Converter}. Provide the part name with the
 *       variable name or in the annotation (eg, multiparRequest(@Part Image foo) or
 *       multiparRequest(@Part ("foo") Image photo).
 * </ul>
 *
 * <p>Values may be {@code null} which will omit them from the request body.
 *
 * <pre><code>
 * &#64;Multipart
 * &#64;POST("/")
 * PostInfo example(
 *     &#64;Part String description,
 *     &#64;Part TypedContent image);
 * </code></pre>
 *
 * <p>Part parameters may not be {@code null}.
 */
@Documented
@Target(PARAMETER)
@Retention(RetentionPolicy.SOURCE)
public @interface Part {

  /**
   * The name of the part. Required for all parameter types except {@link
   * okhttp3.MultipartBody.Part}.
   */
  String value() default Constants.EMPTY_STRING;
}
