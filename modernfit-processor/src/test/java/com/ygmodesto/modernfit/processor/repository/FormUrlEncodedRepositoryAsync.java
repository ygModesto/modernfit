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
import com.ygmodesto.modernfit.annotations.GET;
import com.ygmodesto.modernfit.annotations.Modernfit;
import com.ygmodesto.modernfit.annotations.POST;
import com.ygmodesto.modernfit.annotations.PUT;
import com.ygmodesto.modernfit.annotations.Path;
import com.ygmodesto.modernfit.converters.JacksonConverterFactory;
import com.ygmodesto.modernfit.processor.server.User;
import com.ygmodesto.modernfit.services.ResponseCallback;
import java.util.Collection;
import java.util.Map;

@Modernfit(value = "http://localhost:8080/api", converterFactory = JacksonConverterFactory.class)
public interface FormUrlEncodedRepositoryAsync {

  @FormUrlEncoded
  @GET("/user/{id}")
  void getUser(@Path Long id, ResponseCallback<User> responseCallback);

  @FormUrlEncoded
  @POST("/user/create")
  void createUser(@Field String name, @Field String login, ResponseCallback<User> responseCallback);

  @FormUrlEncoded
  @POST("/user/create")
  void createUser(@FieldMap Map<String, String> user, ResponseCallback<User> responseCallback);

  @FormUrlEncoded
  @PUT("/user/update/{id}")
  void updateUser(
      @Path Long id, @Field String name, @Field String login, ResponseCallback<User> responseCallback);

  @FormUrlEncoded
  @GET("/user/users")
  void getUserAll(ResponseCallback<Collection<User>> responseCallback);
}
