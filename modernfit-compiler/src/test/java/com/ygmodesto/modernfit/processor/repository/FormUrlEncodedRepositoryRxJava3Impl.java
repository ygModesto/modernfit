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

import com.ygmodesto.modernfit.converters.Converter;
import com.ygmodesto.modernfit.converters.JacksonConverterFactory;
import com.ygmodesto.modernfit.processor.server.User;
import com.ygmodesto.modernfit.services.AbstractInterfaceImpl;
import com.ygmodesto.modernfit.services.BodyContent;
import com.ygmodesto.modernfit.services.ClientOkHttp;
import com.ygmodesto.modernfit.services.CustomType;
import com.ygmodesto.modernfit.services.FormUrlEncodedBody;
import com.ygmodesto.modernfit.services.HttpClient;
import com.ygmodesto.modernfit.services.HttpMethod;
import com.ygmodesto.modernfit.services.ModernfitException;
import com.ygmodesto.modernfit.services.RequestInfo;
import com.ygmodesto.modernfit.services.ResponseContent;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.util.Map;

public class FormUrlEncodedRepositoryRxJava3Impl extends AbstractInterfaceImpl implements FormUrlEncodedRepositoryRxJava3 {
	

  private JacksonConverterFactory converterFactory;
	
  private Converter<Long, String> urlConverter0;
	
  private Converter<String, BodyContent> requestConverter0;
	  
  private Converter<ResponseContent, User> responseConverter0;
	  
  private FormUrlEncodedRepositoryRxJava3Impl(Builder builder) {
		baseUrl = "http://localhost:8080/api";
	    httpClient = ( builder.httpClient == null) ? ClientOkHttp.create() : builder.httpClient;
	    converterFactory = ( builder.converterFactory == null) ? JacksonConverterFactory.create() : builder.converterFactory;
	    Long zombieurlConverter0 = null;
	    urlConverter0 = converterFactory.getUrlConverter(zombieurlConverter0, new CustomType<Long>() {});
	    String zombierequestConverter0 = null;
	    requestConverter0 = converterFactory.getRequestConverter(zombierequestConverter0, new CustomType<String>() {});
	    User zombieresponseConverter0 = null;
	    responseConverter0 = converterFactory.getResponseConverter(zombieresponseConverter0, new CustomType<User>() {});
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public Single<User> getUser(Long id) throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/user/");
	requestInfoBuilder.addUrlPathNotEncoded(id, this.urlConverter0);
    requestInfoBuilder.addHttpMethod(HttpMethod.GET);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final FormUrlEncodedBody.Builder bodyBuilder = FormUrlEncodedBody.builder();
    return Single.defer(() -> Single.just(FormUrlEncodedRepositoryRxJava3Impl.this.responseConverter0.convert(FormUrlEncodedRepositoryRxJava3Impl.this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()))));
  }

  @Override
  public Single<User> createUser(String name, String login) throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/user/create");
    requestInfoBuilder.addHttpMethod(HttpMethod.POST);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final FormUrlEncodedBody.Builder bodyBuilder = FormUrlEncodedBody.builder();
    bodyBuilder.addFieldNotEncoded("name", name, this.requestConverter0);
    bodyBuilder.addFieldNotEncoded("login", login, this.requestConverter0);
    return Single.defer(() -> Single.just(FormUrlEncodedRepositoryRxJava3Impl.this.responseConverter0.convert(FormUrlEncodedRepositoryRxJava3Impl.this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()))));
  }

  @Override
  public Single<User> createUser(Map<String, String> user) throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/user/create");
    requestInfoBuilder.addHttpMethod(HttpMethod.POST);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final FormUrlEncodedBody.Builder bodyBuilder = FormUrlEncodedBody.builder();
    bodyBuilder.addFieldMapNotEncoded(user, this.requestConverter0);
    return Single.defer(() -> Single.just(FormUrlEncodedRepositoryRxJava3Impl.this.responseConverter0.convert(FormUrlEncodedRepositoryRxJava3Impl.this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()))));
  }

  @Override
  public Single<User> updateUser(Long id, String name, String login) throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/user/update/");
	requestInfoBuilder.addUrlPathNotEncoded(id, this.urlConverter0);
    requestInfoBuilder.addHttpMethod(HttpMethod.PUT);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final FormUrlEncodedBody.Builder bodyBuilder = FormUrlEncodedBody.builder();
    bodyBuilder.addFieldNotEncoded("name", name, this.requestConverter0);
    bodyBuilder.addFieldNotEncoded("login", login, this.requestConverter0);
    return Single.defer(() -> Single.just(FormUrlEncodedRepositoryRxJava3Impl.this.responseConverter0.convert(FormUrlEncodedRepositoryRxJava3Impl.this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()))));
  }

  @Override
  public Observable<User> getUserAll() throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/user/users");
    requestInfoBuilder.addHttpMethod(HttpMethod.GET);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final FormUrlEncodedBody.Builder bodyBuilder = FormUrlEncodedBody.builder();
    return Observable.defer(() -> Observable.just(FormUrlEncodedRepositoryRxJava3Impl.this.responseConverter0.convert(FormUrlEncodedRepositoryRxJava3Impl.this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()))));
  }
  
  public static class Builder {
    private HttpClient httpClient;

    private JacksonConverterFactory converterFactory;

    public Builder addHttpClient(ClientOkHttp httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    public Builder addConverterFactory(JacksonConverterFactory converterFactory) {
      this.converterFactory = converterFactory;
      return this;
    }

    public FormUrlEncodedRepositoryRxJava3Impl build() {
      return new FormUrlEncodedRepositoryRxJava3Impl(this);
    }
  }
}
