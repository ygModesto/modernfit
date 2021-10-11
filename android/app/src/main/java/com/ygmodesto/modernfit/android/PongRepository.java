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

package com.ygmodesto.modernfit.android;




import com.ygmodesto.modernfit.annotations.Body;
import com.ygmodesto.modernfit.annotations.ComponentModel;
import com.ygmodesto.modernfit.annotations.GET;
import com.ygmodesto.modernfit.annotations.Headers;
import com.ygmodesto.modernfit.annotations.Modernfit;
import com.ygmodesto.modernfit.annotations.POST;
import com.ygmodesto.modernfit.converters.GsonConverterFactory;
import com.ygmodesto.modernfit.services.ResponseCallback;
import com.ygmodesto.modernfit.volley.ClientVolley;


/**
 * The rest service returns in the response body what has been sent to it in the x-pong-response header.
 */
@Modernfit(value = BuildConfig.BASE_URL, converterFactory = GsonConverterFactory.class, client = ClientVolley.class, componentModel = ComponentModel.STANDALONE)
public interface PongRepository {


	@GET("/pong")
	@Headers("x-pong-response: {\"id\": 3,\"age\": 23, \"name\": \"Lamar\", \"login\": \"@lamar\"}")
	void getPongResponse(ResponseCallback<User> responseCallback);


	@POST("/pong")
	@Headers("x-pong-response: {\"id\": 3,\"age\": 23, \"name\": \"Lamar\", \"login\": \"@lamar\"}")
	void postPongResponse(@Body UpdateUserTO updateUserTO, ResponseCallback<User> responseCallback);
}
