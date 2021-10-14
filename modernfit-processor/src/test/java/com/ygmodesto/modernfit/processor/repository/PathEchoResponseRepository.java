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

package com.ygmodesto.modernfit.processor.repository;

import com.ygmodesto.modernfit.annotations.GET;
import com.ygmodesto.modernfit.annotations.Modernfit;
import com.ygmodesto.modernfit.annotations.Path;
import com.ygmodesto.modernfit.converters.JacksonConverterFactory;
import com.ygmodesto.modernfit.processor.server.EchoResponse;

@Modernfit(value = "http://localhost:8080/api", converterFactory = JacksonConverterFactory.class)
public interface PathEchoResponseRepository {

  @GET("/echo/{id}")
  EchoResponse pathValueFromAnnotation(@Path("id") Long userId);

  @GET("/echo/{userId}")
  EchoResponse pathValueNameOfParameter(@Path Long userId);

  @GET("/echo/{name}")
  EchoResponse pathValueFromParameterEncoded(@Path(encoded = true) String name);

  @GET("/echo/{name}")
  EchoResponse pathValueFromParameterNotEncoded(@Path(encoded = false) String name);
}
