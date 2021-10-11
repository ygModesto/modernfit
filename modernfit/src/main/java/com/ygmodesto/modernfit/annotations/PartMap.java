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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes name and value parts of a multi-part request.
 *
 * <p>Values of the map on which this annotation exists will be processed in one of two ways:
 *
 * <ul>
 *   <li>If the type is {@link com.ygmodesto.modernfit.services.TypedContent TypedContent} the value
 *       will be used directly with its content type.
 *   <li>Other object types will be converted to an appropriate representation by using {@link
 *       com.ygmodesto.modernfit.converters.Converter Converter}.
 * </ul>
 *
 * <pre><code>
 * &#64;Multipart
 * &#64;POST("/create")
 * User create(
 *     &#64;Part User user,
 *     &#64;PartMap Map&lt;String, TypedContent&gt; params);
 * </code></pre>
 *
 * <p>A {@code null} value for the map, as a key, or as a value is not allowed.
 *
 * @see Multipart
 * @see Part
 */
@Documented
@Target(PARAMETER)
@Retention(RetentionPolicy.SOURCE)
public @interface PartMap {}
