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
import com.ygmodesto.modernfit.annotations.Header;
import com.ygmodesto.modernfit.annotations.HeaderMap;
import com.ygmodesto.modernfit.annotations.Headers;
import com.ygmodesto.modernfit.annotations.Modernfit;
import com.ygmodesto.modernfit.converters.JacksonConverterFactory;
import com.ygmodesto.modernfit.processor.server.EchoResponse;
import com.ygmodesto.modernfit.services.ConfigurationInterface;
import java.util.Map;

@Modernfit(value = "http://localhost:8080/api", converterFactory = JacksonConverterFactory.class)
public interface HeaderEchoResponseRepository extends ConfigurationInterface {

  @GET("/echo")
  EchoResponse headerStringValueFromAnnotation(@Header("DeviceUUID") String id);

  @GET("/echo")
  EchoResponse headerValueFromAnnotation(@Header("DeviceUUID") Long id);

  @GET("/echo")
  EchoResponse headerValueNameOfParameter(@Header Long deviceUUID);

  @GET("/echo")
  EchoResponse headerMapStringKeyStringValue(@HeaderMap Map<String, String> headers);

  @GET("/echo")
  EchoResponse headerMapObjectKeyObjectValue(@HeaderMap Map<Object, Object> headers);

  @GET("/echo")
  EchoResponse headerAndHeaderMap(@Header Long deviceUUID, @HeaderMap Map<String, String> headers);

  @Headers({"DeviceUUID: abcd-1234", "country: es"})
  @GET("/echo")
  EchoResponse headersAsMethodAnnotation();

  @GET("/echo")
  EchoResponse headersInAbstractInterfaceImpl();

  @Headers({"header1: value1", "header2: value2"})
  @GET("/echo")
  EchoResponse headersAnyway(
      @Header String country,
      @Header Long deviceUUID,
      @HeaderMap Map<String, String> headers,
      @HeaderMap Map<Object, Object> headersAsObject);
}
