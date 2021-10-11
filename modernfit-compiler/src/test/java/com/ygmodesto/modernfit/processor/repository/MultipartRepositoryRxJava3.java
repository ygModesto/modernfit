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
import com.ygmodesto.modernfit.annotations.Multipart;
import com.ygmodesto.modernfit.annotations.POST;
import com.ygmodesto.modernfit.annotations.PUT;
import com.ygmodesto.modernfit.annotations.Part;
import com.ygmodesto.modernfit.annotations.PartMap;
import com.ygmodesto.modernfit.annotations.Path;
import com.ygmodesto.modernfit.converters.JacksonConverterFactory;
import com.ygmodesto.modernfit.processor.server.User;
import com.ygmodesto.modernfit.services.TypedContent;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.io.File;
import java.util.Collection;
import java.util.Map;

@Modernfit(value = "http://localhost:8080/api", converterFactory = JacksonConverterFactory.class)
public interface MultipartRepositoryRxJava3 {

  @Multipart
  @GET("/user/{id}")
  Single<User> getUser(@Path Long id);

  @Multipart
  @POST("/user/create")
  Single<User> createUser(@Part User user);

  @Multipart
  @POST("/users/createHeavy")
  Observable<Collection<User>> createUsers(
      @Part Collection<User> users, @Part Collection<TypedContent> attacheds);

  @Multipart
  @POST("/users/createHeavy")
  Observable<Collection<User>> createUsers(@PartMap Map<String, User> users);

  @Multipart
  @PUT("/user/{id}/attacheds")
  Single<User> updateUserAttacheds(@Path Long id, @PartMap Map<String, TypedContent> attacheds);

  @Multipart
  @PUT("/user/update/{id}")
  Single<User> updateUser(@Path Long id, @Part User user, @Part File file);

  @Multipart
  @GET("/user/users")
  Observable<Collection<User>> getUserAll();
}
