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
import com.ygmodesto.modernfit.processor.server.UpdateUserTO;
import com.ygmodesto.modernfit.processor.server.User;
import com.ygmodesto.modernfit.services.AbstractInterfaceImpl;
import com.ygmodesto.modernfit.services.BodyContent;
import com.ygmodesto.modernfit.services.ClientOkHttp;
import com.ygmodesto.modernfit.services.CustomType;
import com.ygmodesto.modernfit.services.HttpClient;
import com.ygmodesto.modernfit.services.HttpInfo;
import com.ygmodesto.modernfit.services.HttpMethod;
import com.ygmodesto.modernfit.services.ModernfitException;
import com.ygmodesto.modernfit.services.OneObjectDiscreteBody;
import com.ygmodesto.modernfit.services.RequestInfo;
import com.ygmodesto.modernfit.services.ResponseContent;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.Void;
import java.util.Collection;

public class ApplicationJsonRepositoryImpl extends AbstractInterfaceImpl implements ApplicationJsonRepository {
  private JacksonConverterFactory converterFactory;

  private Converter<Long, String> urlConverter0;
  
  private Converter<Void, BodyContent> requestConverter0;

  private Converter<User, BodyContent> requestConverter1;

  private Converter<Collection<User>, BodyContent> requestConverter2;

  private Converter<UpdateUserTO, BodyContent> requestConverter3;

  private Converter<ResponseContent, User> responseConverter0;

  private Converter<ResponseContent, Collection<User>> responseConverter1;

  private Converter<ResponseContent, Void> responseConverter2;

  private ApplicationJsonRepositoryImpl(Builder builder) {
	baseUrl = "http://localhost:8080/api";
    httpClient = ( builder.httpClient == null) ? ClientOkHttp.create() : builder.httpClient;
    converterFactory = ( builder.converterFactory == null) ? JacksonConverterFactory.create() : builder.converterFactory;
    Long zombieurlConverter0 = null;
    urlConverter0 = converterFactory.getUrlConverter(zombieurlConverter0, new CustomType<Long>() {});
    Void zombierequestConverter0 = null;
    requestConverter0 = converterFactory.getRequestConverter(zombierequestConverter0, new CustomType<Void>() {});
    User zombierequestConverter1 = null;
    requestConverter1 = converterFactory.getRequestConverter(zombierequestConverter1, new CustomType<User>() {});
    Collection<User> zombierequestConverter2 = null;
    requestConverter2 = converterFactory.getRequestConverter(zombierequestConverter2, new CustomType<Collection<User>>() {});
    UpdateUserTO zombierequestConverter3 = null;
    requestConverter3 = converterFactory.getRequestConverter(zombierequestConverter3, new CustomType<UpdateUserTO>() {});
    User zombieresponseConverter0 = null;
    responseConverter0 = converterFactory.getResponseConverter(zombieresponseConverter0, new CustomType<User>() {});
    Collection<User> zombieresponseConverter1 = null;
    responseConverter1 = converterFactory.getResponseConverter(zombieresponseConverter1, new CustomType<Collection<User>>() {});
    Void zombieresponseConverter2 = null;
    responseConverter2 = converterFactory.getResponseConverter(zombieresponseConverter2, new CustomType<Void>() {});
  }
  
  

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public User getUser(Long id) throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/user/");
    requestInfoBuilder.addUrlPathNotEncoded(id, this.urlConverter0);
    requestInfoBuilder.addHttpMethod(HttpMethod.GET);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final OneObjectDiscreteBody.Builder<Void> bodyBuilder = OneObjectDiscreteBody.builder();
    bodyBuilder.addConverter(this.requestConverter0);
    return this.responseConverter0.convert(this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()));
  }

  @Override
  public User createUser(User user) throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/user/create");
    requestInfoBuilder.addHttpMethod(HttpMethod.POST);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final OneObjectDiscreteBody.Builder<User> bodyBuilder = OneObjectDiscreteBody.builder();
    bodyBuilder.addConverter(this.requestConverter1);
    bodyBuilder.addBody(user);
    return this.responseConverter0.convert(this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()));
  }

  @Override
  public Collection<User> createUsers(Collection<User> users) throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/users/create");
    requestInfoBuilder.addHttpMethod(HttpMethod.POST);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final OneObjectDiscreteBody.Builder<Collection<User>> bodyBuilder = OneObjectDiscreteBody.builder();
    bodyBuilder.addConverter(this.requestConverter2);
    bodyBuilder.addBody(users);
    return this.responseConverter1.convert(this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()));
  }

  @Override
  public User updateUser(Long id, UpdateUserTO user) throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/user/update/");
    requestInfoBuilder.addUrlPathNotEncoded(id, this.urlConverter0);
    requestInfoBuilder.addHttpMethod(HttpMethod.PUT);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final OneObjectDiscreteBody.Builder<UpdateUserTO> bodyBuilder = OneObjectDiscreteBody.builder();
    bodyBuilder.addConverter(this.requestConverter3);
    bodyBuilder.addBody(user);
    return this.responseConverter0.convert(this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()));
  }

  @Override
  public void updateUserWithoutResponse(Long id, UpdateUserTO user) throws ModernfitException {
    final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
    requestInfoBuilder.addUrlPathAsConstant("/user/update/");
    requestInfoBuilder.addUrlPathNotEncoded(id, this.urlConverter0);
    requestInfoBuilder.addHttpMethod(HttpMethod.PUT);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final OneObjectDiscreteBody.Builder<UpdateUserTO> bodyBuilder = OneObjectDiscreteBody.builder();
    bodyBuilder.addConverter(this.requestConverter3);
    bodyBuilder.addBody(user);
    this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build());
  }

  @Override
  public Collection<User> getUserAll() throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/user/users");
    requestInfoBuilder.addHttpMethod(HttpMethod.GET);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final OneObjectDiscreteBody.Builder<Void> bodyBuilder = OneObjectDiscreteBody.builder();
    bodyBuilder.addConverter(this.requestConverter0);
    return this.responseConverter1.convert(this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()));
  }
  
  @Override
  public User getUser(String baseUrl, Long id) throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/user/");
	requestInfoBuilder.addUrlPathNotEncoded(id, this.urlConverter0);
    requestInfoBuilder.addHttpMethod(HttpMethod.GET);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final OneObjectDiscreteBody.Builder<Void> bodyBuilder = OneObjectDiscreteBody.builder();
    bodyBuilder.addConverter(this.requestConverter0);
	return this.responseConverter0.convert(this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()));
  }
  
  @Override
  public HttpInfo<User> getUserWithHttpInfo(String baseUrl, Long id) throws ModernfitException {
    final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(baseUrl);
    requestInfoBuilder.addUrlPathAsConstant("/user/");
    requestInfoBuilder.addUrlPathNotEncoded(id, this.urlConverter0);
    requestInfoBuilder.addHttpMethod(HttpMethod.GET);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final OneObjectDiscreteBody.Builder<Void> bodyBuilder = OneObjectDiscreteBody.builder();
    bodyBuilder.addConverter(this.requestConverter0);
    return this.toHttpInfo(this.responseConverter0, this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()));
  }
  
  @Override
  public HttpInfo<Void> getVoidWithHttpInfo(Long id, UpdateUserTO user) throws ModernfitException {
    final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
    requestInfoBuilder.addUrlPathAsConstant("/user/");
    requestInfoBuilder.addUrlPathNotEncoded(id, this.urlConverter0);
    requestInfoBuilder.addHttpMethod(HttpMethod.PUT);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final OneObjectDiscreteBody.Builder<UpdateUserTO> bodyBuilder = OneObjectDiscreteBody.builder();
    bodyBuilder.addConverter(this.requestConverter3);
    bodyBuilder.addBody(user);
    return this.toHttpInfo(this.responseConverter2, this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()));
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

    public ApplicationJsonRepositoryImpl build() {
      return new ApplicationJsonRepositoryImpl(this);
    }
  }
}
