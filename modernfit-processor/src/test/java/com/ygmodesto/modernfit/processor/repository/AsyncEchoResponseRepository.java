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
import com.ygmodesto.modernfit.annotations.DELETE;
import com.ygmodesto.modernfit.annotations.Field;
import com.ygmodesto.modernfit.annotations.FieldMap;
import com.ygmodesto.modernfit.annotations.FormUrlEncoded;
import com.ygmodesto.modernfit.annotations.GET;
import com.ygmodesto.modernfit.annotations.HEAD;
import com.ygmodesto.modernfit.annotations.Modernfit;
import com.ygmodesto.modernfit.annotations.Multipart;
import com.ygmodesto.modernfit.annotations.OPTIONS;
import com.ygmodesto.modernfit.annotations.PATCH;
import com.ygmodesto.modernfit.annotations.POST;
import com.ygmodesto.modernfit.annotations.PUT;
import com.ygmodesto.modernfit.annotations.Part;
import com.ygmodesto.modernfit.annotations.PartMap;
import com.ygmodesto.modernfit.converters.JacksonConverterFactory;
import com.ygmodesto.modernfit.processor.server.EchoResponse;
import com.ygmodesto.modernfit.processor.server.ModelTO;
import com.ygmodesto.modernfit.processor.server.MultipartEchoResponse;
import com.ygmodesto.modernfit.services.ResponseCallback;
import com.ygmodesto.modernfit.services.TypedContent;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Modernfit(value = "http://localhost:8080/api", converterFactory = JacksonConverterFactory.class)
public interface AsyncEchoResponseRepository {

  @GET("/echo")
  void getEcho(ResponseCallback<EchoResponse> responseCallback);

  @HEAD("/echo/head")
  void headEcho(ResponseCallback<Void> responseCallback);

  @DELETE("/echo")
  void deleteEcho(ResponseCallback<EchoResponse> responseCallback);

  @OPTIONS("/echo/options")
  void optionsEcho(ResponseCallback<Void> responseCallback);

  @POST("/echo")
  void postEchoWithBody(@Body ModelTO modelTO, ResponseCallback<EchoResponse> responseCallback);

  @PUT("/echo")
  void putEchoWithBody(@Body ModelTO modelTO, ResponseCallback<EchoResponse> responseCallback);

  @OPTIONS("/echo/options")
  void optionsEchoWithBody(@Body ModelTO modelTO, ResponseCallback<Void> responseCallback);

  @PATCH("/echo")
  void patchEchoWithBody(@Body ModelTO modelTO, ResponseCallback<EchoResponse> responseCallback);

  @FormUrlEncoded
  @POST("/echo")
  void fieldsAndQueryMap(
      @Field Long userId,
      @Field List<Long> userIds,
      @FieldMap Map<String, String> fieldMap,
      ResponseCallback<EchoResponse> responseCallback);

  @Multipart
  @POST("/multipart/echo")
  void partsAndPartMapAndTypedContent(
      @Part Long partObjectA,
      @Part("partListA") List<Long> partListA,
      @PartMap Map<String, Collection<Long>> partMap,
      @Part("fileParts") Collection<TypedContent> typedContents,
      ResponseCallback<MultipartEchoResponse> responseCallback);
}
