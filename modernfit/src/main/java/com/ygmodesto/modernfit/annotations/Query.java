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
 * Query parameter appended to the URL.
 *
 * <p>Values are converted using {@link com.ygmodesto.modernfit.converters.Converter Converter} and
 * then URL encoded. {@code null} values are ignored. Passing a {@link java.util.List List} or array
 * will result in a query parameter for each non-{@code null} item.
 *
 * <p>Simple Example: *
 *
 * <pre><code>
 * &#64;GET("/friends")
 * Response friends(@Query int page);
 * </code></pre>
 *
 * <pre><code>
 * &#64;GET("/friends")
 * Response friends(@Query("page") int page);
 * </code></pre>
 *
 * <p>Calling with {@code foo.friends(1)} yields {@code /friends?page=1}.
 *
 * <p>Example with {@code null}:
 *
 * <pre><code>
 * &#64;GET("/friends")
 * Response friends(@Query("group") String group);
 * </code></pre>
 *
 * <p>Calling with {@code foo.friends(null)} yields {@code /friends}.
 *
 * <p>Array/Varargs Example:
 *
 * <pre><code>
 * &#64;GET("/friends")
 * Response friends(@Query("group") String... groups);
 * </code></pre>
 *
 * <p>Calling with {@code foo.friends("coworker", "bowling")} yields {@code
 * /friends?group=coworker&group=bowling}.
 *
 * <p>Parameter names and values are URL encoded by default. Specify {@link #encoded() encoded=true}
 * to change this behavior.
 *
 * <pre><code>
 * &#64;GET("/friends")
 * Response friends(@Query(value="group", encoded=true) String group);
 * </code></pre>
 *
 * <p>Calling with {@code foo.friends("foo+bar"))} yields {@code /friends?group=foo+bar}.
 *
 * @see QueryMap
 */
@Documented
@Target(PARAMETER)
@Retention(RetentionPolicy.SOURCE)
public @interface Query {
  /** The query parameter name. */
  String value() default Constants.EMPTY_STRING;

  /**
   * Specifies whether the parameter {@linkplain #value() name} and value are already URL encoded.
   */
  boolean encoded() default false;
}
