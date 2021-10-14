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
import com.ygmodesto.modernfit.annotations.PUT;
import com.ygmodesto.modernfit.annotations.Path;
import com.ygmodesto.modernfit.annotations.Url;
import com.ygmodesto.modernfit.converters.JacksonConverterFactory;
import com.ygmodesto.modernfit.processor.server.UpdateUserTO;
import com.ygmodesto.modernfit.processor.server.User;
import com.ygmodesto.modernfit.services.HttpInfo;
import java.util.Collection;

@Modernfit(value = "http://localhost:8080/api", converterFactory = JacksonConverterFactory.class)
public interface ApplicationJsonRepository {

  @GET("/user/{id}")
  User getUser(@Path Long id);

  @POST("/user/create")
  User createUser(@Body User user);

  @POST("/users/create")
  Collection<User> createUsers(@Body Collection<User> users);

  @PUT("/user/update/{id}")
  User updateUser(@Path Long id, @Body UpdateUserTO user);
  
  @PUT("/user/update/{id}")
  void updateUserWithoutResponse(@Path Long id, @Body UpdateUserTO user);

  @GET("/user/users")
  Collection<User> getUserAll();

  @GET("/user/{id}")
  User getUser(@Url String baseUrl, @Path Long id);
  
  @GET("/user/{id}")
  HttpInfo<User> getUserWithHttpInfo(@Url String baseUrl, @Path Long id);
  
  @PUT("/user/{id}")
  HttpInfo<Void> getVoidWithHttpInfo(@Path Long id, @Body UpdateUserTO user);
}
