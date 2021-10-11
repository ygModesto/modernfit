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

import java.io.File;
import java.util.Collection;
import java.util.Map;

import com.ygmodesto.modernfit.annotations.GET;
import com.ygmodesto.modernfit.annotations.Modernfit;
import com.ygmodesto.modernfit.annotations.Multipart;
import com.ygmodesto.modernfit.annotations.POST;
import com.ygmodesto.modernfit.annotations.PUT;
import com.ygmodesto.modernfit.annotations.Part;
import com.ygmodesto.modernfit.annotations.PartMap;
import com.ygmodesto.modernfit.annotations.Path;
import com.ygmodesto.modernfit.converters.JacksonConverterFactory;
import com.ygmodesto.modernfit.processor.server.User;
import com.ygmodesto.modernfit.services.ResponseCallback;
import com.ygmodesto.modernfit.services.TypedContent;


@Modernfit(value = "http://localhost:8080/api", converterFactory = JacksonConverterFactory.class)
public interface MultipartRepositoryAsync {


  @Multipart
  @GET("/user/{id}")
  void getUser(@Path Long id, ResponseCallback<User> callback);

  @Multipart
  @POST("/user/create")
  void createUser(@Part User user, ResponseCallback<User> callback);

  @Multipart
  @POST("/users/createHeavy")
  void createUsers(@Part Collection<User> users, @Part Collection<TypedContent> attacheds, ResponseCallback<Collection<User>> callback);

  @Multipart
  @POST("/users/createHeavy")
  void createUsers(@PartMap Map<String, User> users, ResponseCallback<Collection<User>> callback);

  @Multipart
  @PUT("/user/{id}/attacheds")
  void updateUserAttacheds(@Path Long id, @PartMap Map<String, TypedContent> attacheds, ResponseCallback<User> callback);

  @Multipart
  @PUT("/user/update/{id}")
  void updateUser(@Path Long id, @Part User user, @Part File file, ResponseCallback<User> callback);

  @Multipart
  @GET("/user/users")
  void getUserAll(ResponseCallback<Collection<User>> callback);

}
