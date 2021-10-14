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

import com.ygmodesto.modernfit.annotations.Body;
import com.ygmodesto.modernfit.annotations.GET;
import com.ygmodesto.modernfit.annotations.Modernfit;
import com.ygmodesto.modernfit.annotations.POST;
import com.ygmodesto.modernfit.converters.JacksonConverterFactory;

@Modernfit(value = "http://localhost:8080/api", converterFactory = JacksonConverterFactory.class)
public interface ScalarEchoResponseRepository {

  @GET("/echo/scalar/long")
  Long getEchoLong();

  @POST("/echo/scalar/string")
  Long postEchoLong(@Body Long value);

  @POST("/echo/scalar/string")
  long postEchoLongUnboxed(@Body long value);

  @GET("/echo/scalar/string")
  String getEchoString();

  @POST("/echo/scalar/string")
  String postEchoString(@Body String value);
}
