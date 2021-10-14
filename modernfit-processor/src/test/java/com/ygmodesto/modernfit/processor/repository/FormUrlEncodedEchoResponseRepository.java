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

import com.ygmodesto.modernfit.annotations.Field;
import com.ygmodesto.modernfit.annotations.FieldMap;
import com.ygmodesto.modernfit.annotations.FormUrlEncoded;
import com.ygmodesto.modernfit.annotations.Modernfit;
import com.ygmodesto.modernfit.annotations.POST;
import com.ygmodesto.modernfit.converters.JacksonConverterFactory;
import com.ygmodesto.modernfit.processor.server.EchoResponse;
import java.util.List;
import java.util.Map;

@Modernfit(value = "http://localhost:8080/api", converterFactory = JacksonConverterFactory.class)
public interface FormUrlEncodedEchoResponseRepository {

  @FormUrlEncoded
  @POST("/echo")
  EchoResponse fieldValueFromAnnotation(@Field("userId") Long id);

  @FormUrlEncoded
  @POST("/echo")
  EchoResponse fieldValueNameOfParameter(@Field Long userId);

  @FormUrlEncoded
  @POST("/echo")
  EchoResponse fieldValueNameOfParameterEncoded(@Field(encoded = true) String notes);

  @FormUrlEncoded
  @POST("/echo")
  EchoResponse fieldValueNameOfParameterNotEncoded(@Field(encoded = false) String notes);

  @FormUrlEncoded
  @POST("/echo")
  EchoResponse fieldList(@Field List<Long> userIds);

  @FormUrlEncoded
  @POST("/echo")
  EchoResponse fieldArray(@Field Long[] userIds);

  @FormUrlEncoded
  @POST("/echo")
  EchoResponse fieldMapStringKeyStringValueNotEncoded(@FieldMap Map<String, String> fieldMap);

  @FormUrlEncoded
  @POST("/echo")
  EchoResponse fieldMapStringKeyStringValueEncoded(
      @FieldMap(encoded = true) Map<String, String> fieldMap);

  @FormUrlEncoded
  @POST("/echo")
  EchoResponse fieldMapStringKeyLongValue(@FieldMap Map<String, Long> fieldMap);

  @FormUrlEncoded
  @POST("/echo")
  EchoResponse fieldsAndQueryMap(
      @Field Long userId, @Field List<Long> userIds, @FieldMap Map<String, String> fieldMap);
}
