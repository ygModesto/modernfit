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
import com.ygmodesto.modernfit.services.TypedContent;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Modernfit(value = "http://localhost:8080/api", converterFactory = JacksonConverterFactory.class)
public interface RxJava3EchoResponseRepository {

  @GET("/echo")
  Single<EchoResponse> getEcho();

  @HEAD("/echo/head")
  Completable headEcho();

  @DELETE("/echo")
  Single<EchoResponse> deleteEcho();

  @OPTIONS("/echo/options")
  Completable optionsEcho();

  @POST("/echo")
  Single<EchoResponse> postEchoWithBody(@Body ModelTO modelTO);

  @PUT("/echo")
  Single<EchoResponse> putEchoWithBody(@Body ModelTO modelTO);

  @OPTIONS("/echo/options")
  Completable optionsEchoWithBody(@Body ModelTO modelTO);

  @PATCH("/echo")
  Single<EchoResponse> patchEchoWithBody(@Body ModelTO modelTO);

  @FormUrlEncoded
  @POST("/echo")
  Single<EchoResponse> fieldsAndQueryMap(
      @Field Long userId, @Field List<Long> userIds, @FieldMap Map<String, String> fieldMap);

  @Multipart
  @POST("/multipart/echo")
  Single<MultipartEchoResponse> partsAndPartMapAndTypedContent(
      @Part Long partObjectA,
      @Part("partListA") List<Long> partList,
      @PartMap Map<String, Collection<Long>> partMap,
      @Part("fileParts") Collection<TypedContent> typedContents);
}
