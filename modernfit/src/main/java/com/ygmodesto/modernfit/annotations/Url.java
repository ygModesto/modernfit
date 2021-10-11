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
 * Define the request URL using a parameter. Override the URL defined by {@link
 * com.ygmodesto.modernfit.annotations.Modernfit#value() @Modernfit.value()} and {@link
 * com.ygmodesto.modernfit.annotations.GET#value() @GET.value()}, {@link
 * com.ygmodesto.modernfit.annotations.POST#value() @POST.value()}, {@link
 * com.ygmodesto.modernfit.annotations.PUT#value() @PUT.value()}, {@link
 * com.ygmodesto.modernfit.annotations.OPTIONS#value() @OPTIONS.value()}, {@link
 * com.ygmodesto.modernfit.annotations.DELETE#value() @DELETE.value()}, {@link
 * com.ygmodesto.modernfit.annotations.HEAD#value() @HEAD.value()} or {@link
 * com.ygmodesto.modernfit.annotations.PATCH#value() @PATCH.value()} annotations
 *
 * <pre><code>
 * &#64;GET
 * List&lt;Foo&gt; list(@Url String url);
 * </code></pre>
 *
 * <p>See {@linkplain com.ygmodesto.modernfit.annotations.Modernfit#value() base URL} for details of
 * how this is resolved against a base URL to create the full endpoint URL.
 */
@Documented
@Target(PARAMETER)
@Retention(RetentionPolicy.SOURCE)
public @interface Url {}
