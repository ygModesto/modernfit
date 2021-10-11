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
import com.ygmodesto.modernfit.services.ResponseContent;
import com.ygmodesto.modernfit.services.TypedContent;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.io.File;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.util.Collection;
import java.util.Map;

public class MultipartRepositoryRxJava3Impl extends AbstractInterfaceImpl implements MultipartRepositoryRxJava3 {
	
  private JacksonConverterFactory converterFactory;

  private Converter<Long, String> urlConverter0;

  private Converter<User, BodyContent> requestConverter0;

  private Converter<Collection<User>, BodyContent> requestConverter1;

  private Converter<File, BodyContent> requestConverter2;

  private Converter<ResponseContent, User> responseConverter0;

  private Converter<ResponseContent, Collection<User>> responseConverter1;
	
  private MultipartRepositoryRxJava3Impl(Builder builder) {
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
  public Single<User> getUser(Long id) throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/user/");
	requestInfoBuilder.addUrlPathNotEncoded(id, this.urlConverter0);
    requestInfoBuilder.addHttpMethod(HttpMethod.GET);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final MultipartBody.Builder bodyBuilder = MultipartBody.builder();
    return Single.defer(() -> Single.just(MultipartRepositoryRxJava3Impl.this.responseConverter0.convert(MultipartRepositoryRxJava3Impl.this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()))));
  }

  @Override
  public Single<User> createUser(User user) throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/user/create");
    requestInfoBuilder.addHttpMethod(HttpMethod.POST);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final MultipartBody.Builder bodyBuilder = MultipartBody.builder();
    bodyBuilder.addPart("user", user, this.requestConverter0);
    return Single.defer(() -> Single.just(MultipartRepositoryRxJava3Impl.this.responseConverter0.convert(MultipartRepositoryRxJava3Impl.this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()))));
  }

  @Override
  public Observable<Collection<User>> createUsers(Collection<User> users,
      Collection<TypedContent> attacheds) throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/users/createHeavy");
    requestInfoBuilder.addHttpMethod(HttpMethod.POST);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final MultipartBody.Builder bodyBuilder = MultipartBody.builder();
    bodyBuilder.addPart("users", users, this.requestConverter1);
    bodyBuilder.addPart("attacheds", attacheds);
    return Observable.defer(() -> Observable.just(MultipartRepositoryRxJava3Impl.this.responseConverter1.convert(MultipartRepositoryRxJava3Impl.this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()))));
  }

  @Override
  public Observable<Collection<User>> createUsers(Map<String, User> users) throws
      ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/users/createHeavy");
    requestInfoBuilder.addHttpMethod(HttpMethod.POST);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final MultipartBody.Builder bodyBuilder = MultipartBody.builder();
    bodyBuilder.addPart(users, this.requestConverter0);
    return Observable.defer(() -> Observable.just(MultipartRepositoryRxJava3Impl.this.responseConverter1.convert(MultipartRepositoryRxJava3Impl.this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()))));
  }

  @Override
  public Single<User> updateUserAttacheds(Long id, Map<String, TypedContent> attacheds) throws
      ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/user/");
	requestInfoBuilder.addUrlPathNotEncoded(id, this.urlConverter0);
	requestInfoBuilder.addUrlPathAsConstant("/attacheds");
    requestInfoBuilder.addHttpMethod(HttpMethod.PUT);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final MultipartBody.Builder bodyBuilder = MultipartBody.builder();
    bodyBuilder.addPart(attacheds);
    return Single.defer(() -> Single.just(MultipartRepositoryRxJava3Impl.this.responseConverter0.convert(MultipartRepositoryRxJava3Impl.this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()))));
  }

  @Override
  public Single<User> updateUser(Long id, User user, File file) throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/user/update/");
	requestInfoBuilder.addUrlPathNotEncoded(id, this.urlConverter0);
    requestInfoBuilder.addHttpMethod(HttpMethod.PUT);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final MultipartBody.Builder bodyBuilder = MultipartBody.builder();
    bodyBuilder.addPart("user", user, this.requestConverter0);
    bodyBuilder.addPart("file", file, this.requestConverter2);
    return Single.defer(() -> Single.just(MultipartRepositoryRxJava3Impl.this.responseConverter0.convert(MultipartRepositoryRxJava3Impl.this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()))));
  }

  @Override
  public Observable<Collection<User>> getUserAll() throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/user/users");
    requestInfoBuilder.addHttpMethod(HttpMethod.GET);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final MultipartBody.Builder bodyBuilder = MultipartBody.builder();
    return Observable.defer(() -> Observable.just(MultipartRepositoryRxJava3Impl.this.responseConverter1.convert(MultipartRepositoryRxJava3Impl.this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()))));
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

    public MultipartRepositoryRxJava3Impl build() {
      return new MultipartRepositoryRxJava3Impl(this);
    }
  }
}
