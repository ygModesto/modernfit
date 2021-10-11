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

import java.util.Collection;
import com.ygmodesto.modernfit.annotations.Body;
import com.ygmodesto.modernfit.annotations.ComponentModel;
import com.ygmodesto.modernfit.annotations.GET;
import com.ygmodesto.modernfit.annotations.Modernfit;
import com.ygmodesto.modernfit.annotations.POST;
import com.ygmodesto.modernfit.annotations.PUT;
import com.ygmodesto.modernfit.annotations.Path;
import com.ygmodesto.modernfit.converters.JacksonConverterFactory;
import com.ygmodesto.modernfit.processor.server.UpdateUserTO;
import com.ygmodesto.modernfit.processor.server.User;
import com.ygmodesto.modernfit.services.ClientOkHttp;
import com.ygmodesto.modernfit.services.ConfigurationInterface;
import com.ygmodesto.modernfit.services.ResponseCallback;
import com.ygmodesto.modernfit.services.HttpResponseCallback;

@Modernfit(
    value = "http://localhost:8080/api",
    converterFactory = JacksonConverterFactory.class,
    client = ClientOkHttp.class,
    componentModel = ComponentModel.STANDALONE)
public interface ApplicationJsonRepositoryAsync extends ConfigurationInterface {

  @GET("/user/{id}")
  void getUser(@Path Long id, ResponseCallback<User> responseCallback);

  @POST("/user/create")
  void createUser(@Body User user, ResponseCallback<User> responseCallback);

  @POST("/users/create")
  void createUsers(@Body Collection<User> users, ResponseCallback<Collection<User>> responseCallback);

  @PUT("/user/update/{id}")
  void updateUser(@Path Long id, @Body UpdateUserTO user, ResponseCallback<User> responseCallback);
  
  @PUT("/user/update/{id}")
  void updateUserWithoutResponse(@Path Long id, @Body UpdateUserTO user, ResponseCallback<Void> responseCallback);

  @GET("/user/users")
  void getUserAll(ResponseCallback<Collection<User>> responseCallback);

  @GET("/user/{id}")
  void getUserWithHttpInfo(@Path Long id, HttpResponseCallback<User> responseCallback);

  @PUT("/user/{id}")
  void getVoidWithHttpInfo(@Path Long id, @Body UpdateUserTO user, HttpResponseCallback<Void> responseCallback);
}
