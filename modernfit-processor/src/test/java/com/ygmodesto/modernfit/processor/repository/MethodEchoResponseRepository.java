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
import com.ygmodesto.modernfit.annotations.GET;
import com.ygmodesto.modernfit.annotations.HEAD;
import com.ygmodesto.modernfit.annotations.Modernfit;
import com.ygmodesto.modernfit.annotations.OPTIONS;
import com.ygmodesto.modernfit.annotations.PATCH;
import com.ygmodesto.modernfit.annotations.POST;
import com.ygmodesto.modernfit.annotations.PUT;
import com.ygmodesto.modernfit.converters.JacksonConverterFactory;
import com.ygmodesto.modernfit.processor.server.EchoResponse;
import com.ygmodesto.modernfit.processor.server.ModelTO;

@Modernfit(value = "http://localhost:8080/api", converterFactory = JacksonConverterFactory.class)
public interface MethodEchoResponseRepository {

  @GET("/echo")
  EchoResponse getEcho();

  @HEAD("/echo/head")
  void headEcho();

  @DELETE("/echo")
  EchoResponse deleteEcho();

  @OPTIONS("/echo/options")
  void optionsEcho();

  @PATCH("/echo")
  EchoResponse patchEcho(@Body ModelTO modelTO);

  @POST("/echo")
  EchoResponse postEcho(@Body ModelTO modelTO);

  @PUT("/echo")
  EchoResponse putEcho(@Body ModelTO modelTO);

  @OPTIONS("/echo/options")
  void optionsEchoWithBody(@Body ModelTO modelTO);

  @POST("/404/echo")
  EchoResponse post404Echo(@Body ModelTO modelTO);

  @POST("/500/echo")
  EchoResponse post500Echo(@Body ModelTO modelTO);
}
