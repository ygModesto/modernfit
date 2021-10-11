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

import com.ygmodesto.modernfit.annotations.Modernfit;
import com.ygmodesto.modernfit.annotations.Multipart;
import com.ygmodesto.modernfit.annotations.POST;
import com.ygmodesto.modernfit.annotations.PUT;
import com.ygmodesto.modernfit.annotations.Part;
import com.ygmodesto.modernfit.annotations.PartMap;
import com.ygmodesto.modernfit.converters.JacksonConverterFactory;
import com.ygmodesto.modernfit.processor.server.ModelTO;
import com.ygmodesto.modernfit.processor.server.MultipartEchoResponse;
import com.ygmodesto.modernfit.services.TypedContent;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Modernfit(
    value = "http://localhost:8080/api/multipart",
    converterFactory = JacksonConverterFactory.class)
public interface MultipartEchoResponseRepository {

  @Multipart
  @POST("/echo")
  MultipartEchoResponse partValueFromAnnotation(@Part("partObjectA") Long id);

  @Multipart
  @POST("/echo")
  MultipartEchoResponse partValueNameOfParameter(@Part Long partObjectA);

  @Multipart
  @POST("/echo")
  MultipartEchoResponse partList(@Part List<Long> partListA);

  @Multipart
  @POST("/echo")
  MultipartEchoResponse partArray(@Part Long[] partListA);

  @Multipart
  @POST("/echo")
  MultipartEchoResponse partMapStringKeyLongValue(@PartMap Map<String, Long> partMap);

  @Multipart
  @POST("/echo")
  MultipartEchoResponse partsAndPartMap(
      @Part Long partObjectA,
      @Part("partListA") List<Long> partListA,
      @PartMap Map<String, Collection<Long>> partMap);

  @Multipart
  @PUT("/echo")
  MultipartEchoResponse partValueFromAnnotationObject(@Part("partObjectA") ModelTO modelTO);

  @Multipart
  @PUT("/echo")
  MultipartEchoResponse partValueFromAnnotationTypedContent(
      @Part("fileParts") TypedContent typedContent);

  @Multipart
  @PUT("/echo")
  MultipartEchoResponse partValueFromAnnotationListTypedContent(
      @Part("fileParts") Collection<TypedContent> typedContents);

  @Multipart
  @PUT("/echo")
  MultipartEchoResponse partValueFromAnnotationArrayTypedContent(
      @Part("fileParts") TypedContent[] typedContents);

  @Multipart
  @PUT("/echo")
  MultipartEchoResponse partValueFromAnnotationPartMapTypedContent(
      @PartMap Map<String, TypedContent> typedContents);

  @Multipart
  @POST("/echo")
  MultipartEchoResponse partsAndPartMapAndTypedContent(
      @Part Long partObjectA,
      @Part("partListA") List<Long> partListA,
      @PartMap Map<String, Collection<Long>> partMap,
      @Part("fileParts") Collection<TypedContent> typedContents);
}
