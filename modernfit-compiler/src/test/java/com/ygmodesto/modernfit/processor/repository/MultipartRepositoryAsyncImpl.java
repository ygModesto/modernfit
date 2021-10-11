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
import com.ygmodesto.modernfit.services.HttpClient;
import com.ygmodesto.modernfit.services.HttpMethod;
import com.ygmodesto.modernfit.services.ModernfitException;
import com.ygmodesto.modernfit.services.MultipartBody;
import com.ygmodesto.modernfit.services.RequestInfo;
import com.ygmodesto.modernfit.services.ResponseCallback;
import com.ygmodesto.modernfit.services.ResponseContent;
import com.ygmodesto.modernfit.services.TypedContent;

import java.io.File;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.util.Collection;
import java.util.Map;

public class MultipartRepositoryAsyncImpl extends AbstractInterfaceImpl implements MultipartRepositoryAsync {
	
  private JacksonConverterFactory converterFactory;

  private Converter<Long, String> urlConverter0;

  private Converter<User, BodyContent> requestConverter0;

  private Converter<Collection<User>, BodyContent> requestConverter1;

  private Converter<File, BodyContent> requestConverter2;

  private Converter<ResponseContent, User> responseConverter0;

  private Converter<ResponseContent, Collection<User>> responseConverter1;
	
  private MultipartRepositoryAsyncImpl(Builder builder) {
		baseUrl = "http://localhost:8080/api";
	    httpClient = ( builder.httpClient == null) ? ClientOkHttp.create() : builder.httpClient;
	    converterFactory = ( builder.converterFactory == null) ? JacksonConverterFactory.create() : builder.converterFactory;
	    Long zombieurlConverter0 = null;
	    urlConverter0 = converterFactory.getUrlConverter(zombieurlConverter0, new CustomType<Long>() {});
	    User zombierequestConverter0 = null;
	    requestConverter0 = converterFactory.getRequestConverter(zombierequestConverter0, new CustomType<User>() {});
	    Collection<User> zombierequestConverter1 = null;
	    requestConverter1 = converterFactory.getRequestConverter(zombierequestConverter1, new CustomType<Collection<User>>() {});
	    File zombierequestConverter2 = null;
	    requestConverter2 = converterFactory.getRequestConverter(zombierequestConverter2, new CustomType<File>() {});
	    User zombieresponseConverter0 = null;
	    responseConverter0 = converterFactory.getResponseConverter(zombieresponseConverter0, new CustomType<User>() {});
	    Collection<User> zombieresponseConverter1 = null;
	    responseConverter1 = converterFactory.getResponseConverter(zombieresponseConverter1, new CustomType<Collection<User>>() {});
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public void getUser(Long id, ResponseCallback<User> callback) throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/user/");
    requestInfoBuilder.addUrlPathNotEncoded(id, this.urlConverter0);
    requestInfoBuilder.addHttpMethod(HttpMethod.GET);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final MultipartBody.Builder bodyBuilder = MultipartBody.builder();
    callback.setConverter(this.responseConverter0);
    this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build(), callback);
  }

  @Override
  public void createUser(User user, ResponseCallback<User> callback) throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/user/create");
    requestInfoBuilder.addHttpMethod(HttpMethod.POST);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final MultipartBody.Builder bodyBuilder = MultipartBody.builder();
    bodyBuilder.addPart("user", user, this.requestConverter0);
    callback.setConverter(this.responseConverter0);
    this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build(), callback);
  }

  @Override
  public void createUsers(Collection<User> users, Collection<TypedContent> attacheds,
      ResponseCallback<Collection<User>> callback) throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/users/createHeavy");
    requestInfoBuilder.addHttpMethod(HttpMethod.POST);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final MultipartBody.Builder bodyBuilder = MultipartBody.builder();
    bodyBuilder.addPart("users", users, this.requestConverter1);
    bodyBuilder.addPart("attacheds", attacheds);
    callback.setConverter(this.responseConverter1);
    this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build(), callback);
  }

  @Override
  public void createUsers(Map<String, User> users, ResponseCallback<Collection<User>> callback) throws
      ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/users/createHeavy");
    requestInfoBuilder.addHttpMethod(HttpMethod.POST);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final MultipartBody.Builder bodyBuilder = MultipartBody.builder();
    bodyBuilder.addPart(users, this.requestConverter0);
    callback.setConverter(this.responseConverter1);
    this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build(), callback);
  }

  @Override
  public void updateUserAttacheds(Long id, Map<String, TypedContent> attacheds,
      ResponseCallback<User> callback) throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/user/");
    requestInfoBuilder.addUrlPathNotEncoded(id, this.urlConverter0);
    requestInfoBuilder.addUrlPathAsConstant("/attacheds");
    requestInfoBuilder.addHttpMethod(HttpMethod.PUT);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final MultipartBody.Builder bodyBuilder = MultipartBody.builder();
    bodyBuilder.addPart(attacheds);
    callback.setConverter(this.responseConverter0);
    this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build(), callback);
  }

  @Override
  public void updateUser(Long id, User user, File file, ResponseCallback<User> callback) throws
      ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/user/update/");
    requestInfoBuilder.addUrlPathNotEncoded(id, this.urlConverter0);
    requestInfoBuilder.addHttpMethod(HttpMethod.PUT);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final MultipartBody.Builder bodyBuilder = MultipartBody.builder();
    bodyBuilder.addPart("user", user, this.requestConverter0);
    bodyBuilder.addPart("file", file, this.requestConverter2);
    callback.setConverter(this.responseConverter0);
    this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build(), callback);
  }

  @Override
  public void getUserAll(ResponseCallback<Collection<User>> callback) throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/user/users");
    requestInfoBuilder.addHttpMethod(HttpMethod.GET);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final MultipartBody.Builder bodyBuilder = MultipartBody.builder();
    callback.setConverter(this.responseConverter1);
    this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build(), callback);
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
    
    public MultipartRepositoryAsyncImpl build() {
      return new MultipartRepositoryAsyncImpl(this);
    }
  }
}
