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
import com.ygmodesto.modernfit.annotations.Query;
import com.ygmodesto.modernfit.annotations.QueryMap;
import com.ygmodesto.modernfit.converters.JacksonConverterFactory;
import com.ygmodesto.modernfit.processor.server.EchoResponse;
import java.util.List;
import java.util.Map;

@Modernfit(value = "http://localhost:8080/api", converterFactory = JacksonConverterFactory.class)
public interface QueryEchoResponseRepository {

  @GET("/echo")
  EchoResponse queryValueFromAnnotation(@Query("userId") Long id);

  @GET("/echo")
  EchoResponse queryValueNameOfParameter(@Query Long userId);

  @GET("/echo")
  EchoResponse queryValueNameOfParameterEncoded(@Query(encoded = true) String notes);

  @GET("/echo")
  EchoResponse queryValueNameOfParameterNotEncoded(@Query(encoded = false) String notes);

  @GET("/echo")
  EchoResponse queryList(@Query List<Long> userIds);

  @GET("/echo")
  EchoResponse queryArray(@Query Long[] userIds);

  @GET("/echo")
  EchoResponse queryMapStringKeyStringValueNotEncoded(@QueryMap Map<String, String> query);

  @GET("/echo")
  EchoResponse queryMapStringKeyStringValueEncoded(
      @QueryMap(encoded = true) Map<String, String> query);

  @GET("/echo")
  EchoResponse queryMapStringKeyLongValue(@QueryMap Map<String, Long> query);

  @GET("/echo")
  EchoResponse querysAndQueryMap(
      @Query Long userId, @Query List<Long> userIds, @QueryMap Map<String, String> queryMap);
}
