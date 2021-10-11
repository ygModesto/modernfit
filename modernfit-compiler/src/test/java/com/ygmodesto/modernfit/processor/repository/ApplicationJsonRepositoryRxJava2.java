/*
 * Copyright 2020 Yago Modesto González Diéguez
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ygmodesto.modernfit.processor.repository;

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
import com.ygmodesto.modernfit.services.HttpInfo;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.Collection;

@Modernfit(
    value = "http://localhost:8080/api",
    converterFactory = JacksonConverterFactory.class,
    client = ClientOkHttp.class,
    componentModel = ComponentModel.STANDALONE)
public interface ApplicationJsonRepositoryRxJava2 extends ConfigurationInterface {

  @GET("/user/{id}")
  Single<User> getUser(@Path Long id);

  @POST("/user/create")
  Single<User> createUser(@Body User user);

  @POST("/users/create")
  Observable<User> createUsers(@Body Collection<User> users);

  @PUT("/user/update/{id}")
  Single<User> updateUser(@Path Long id, @Body UpdateUserTO user);

  @GET("/user/users")
  Observable<User> getUserAll();

  @GET("/user/{id}")
  Single<HttpInfo<User>> getUserWithHttpInfo(@Path Long id);

  @PUT("/user/{id}")
  Single<HttpInfo<Void>> getVoidWithHttpInfo(@Path Long id, @Body UpdateUserTO user);
}
