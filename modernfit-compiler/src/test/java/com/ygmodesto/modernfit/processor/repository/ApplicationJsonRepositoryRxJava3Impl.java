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
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.Void;
import java.util.Collection;


public class ApplicationJsonRepositoryRxJava3Impl extends AbstractInterfaceImpl implements ApplicationJsonRepositoryRxJava3 {
	
  private JacksonConverterFactory converterFactory;

  private Converter<Long, String> urlConverter0;

  private Converter<Void, BodyContent> requestConverter0;

  private Converter<User, BodyContent> requestConverter1;

  private Converter<Collection<User>, BodyContent> requestConverter2;

  private Converter<UpdateUserTO, BodyContent> requestConverter3;

  private Converter<ResponseContent, User> responseConverter0;

  private Converter<ResponseContent, Void> responseConverter1;
	
  private ApplicationJsonRepositoryRxJava3Impl(Builder builder) {
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
    Void zombieresponseConverter1 = null;
    responseConverter1 = converterFactory.getResponseConverter(zombieresponseConverter1, new CustomType<Void>() {});
  }

  public static Builder builder() {
    return new Builder();
  }
  
  @Override
  public Completable updateUser(Long id) throws ModernfitException {
    final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
    requestInfoBuilder.addUrlPathAsConstant("/user/");
    requestInfoBuilder.addUrlPathNotEncoded(id, this.urlConverter0);
    requestInfoBuilder.addHttpMethod(HttpMethod.PUT);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final OneObjectDiscreteBody.Builder<Void> bodyBuilder = OneObjectDiscreteBody.builder();
    bodyBuilder.addConverter(this.requestConverter0);
    return Completable.defer(() ->  {
      ApplicationJsonRepositoryRxJava3Impl.this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build());
      return Completable.complete();
    });
  }

  @Override
  public Single<User> getUser(Long id) throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/user/");
	requestInfoBuilder.addUrlPathNotEncoded(id, this.urlConverter0);
    requestInfoBuilder.addHttpMethod(HttpMethod.GET);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final OneObjectDiscreteBody.Builder<Void> bodyBuilder = OneObjectDiscreteBody.builder();
    bodyBuilder.addConverter(this.requestConverter0);
    return Single.defer(() -> Single.just(ApplicationJsonRepositoryRxJava3Impl.this.responseConverter0.convert(ApplicationJsonRepositoryRxJava3Impl.this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()))));
  }

  @Override
  public Single<User> createUser(User user) throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/user/create");
    requestInfoBuilder.addHttpMethod(HttpMethod.POST);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final OneObjectDiscreteBody.Builder<User> bodyBuilder = OneObjectDiscreteBody.builder();
    bodyBuilder.addConverter(this.requestConverter1);
    bodyBuilder.addBody(user);
    return Single.defer(() -> Single.just(ApplicationJsonRepositoryRxJava3Impl.this.responseConverter0.convert(ApplicationJsonRepositoryRxJava3Impl.this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()))));
  }

  @Override
  public Observable<User> createUsers(Collection<User> users) throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/users/create");
    requestInfoBuilder.addHttpMethod(HttpMethod.POST);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final OneObjectDiscreteBody.Builder<Collection<User>> bodyBuilder = OneObjectDiscreteBody.builder();
    bodyBuilder.addConverter(this.requestConverter2);
    bodyBuilder.addBody(users);
    return Observable.defer(() -> Observable.just(ApplicationJsonRepositoryRxJava3Impl.this.responseConverter0.convert(ApplicationJsonRepositoryRxJava3Impl.this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()))));
  }

  @Override
  public Single<User> updateUser(Long id, UpdateUserTO user) throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/user/update/");
	requestInfoBuilder.addUrlPathNotEncoded(id, this.urlConverter0);
    requestInfoBuilder.addHttpMethod(HttpMethod.PUT);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final OneObjectDiscreteBody.Builder<UpdateUserTO> bodyBuilder = OneObjectDiscreteBody.builder();
    bodyBuilder.addConverter(this.requestConverter3);
    bodyBuilder.addBody(user);
    return Single.defer(() -> Single.just(ApplicationJsonRepositoryRxJava3Impl.this.responseConverter0.convert(ApplicationJsonRepositoryRxJava3Impl.this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()))));
  }

  @Override
  public Observable<User> getUserAll() throws ModernfitException {
	final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
	requestInfoBuilder.addUrlPathAsConstant("/user/users");
    requestInfoBuilder.addHttpMethod(HttpMethod.GET);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final OneObjectDiscreteBody.Builder<Void> bodyBuilder = OneObjectDiscreteBody.builder();
    bodyBuilder.addConverter(this.requestConverter0);
    return Observable.defer(() -> Observable.just(ApplicationJsonRepositoryRxJava3Impl.this.responseConverter0.convert(ApplicationJsonRepositoryRxJava3Impl.this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()))));
  }

  @Override
  public Single<HttpInfo<User>> getUserWithHttpInfo(Long id) throws ModernfitException {
    final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
    requestInfoBuilder.addUrlPathAsConstant("/user/");
    requestInfoBuilder.addUrlPathNotEncoded(id, this.urlConverter0);
    requestInfoBuilder.addHttpMethod(HttpMethod.GET);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final OneObjectDiscreteBody.Builder<Void> bodyBuilder = OneObjectDiscreteBody.builder();
    bodyBuilder.addConverter(this.requestConverter0);
    return Single.defer(() -> Single.just(ApplicationJsonRepositoryRxJava3Impl.this.toHttpInfo(ApplicationJsonRepositoryRxJava3Impl.this.responseConverter0, ApplicationJsonRepositoryRxJava3Impl.this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()))));
  }

  @Override
  public Single<HttpInfo<Void>> getVoidWithHttpInfo(Long id, UpdateUserTO user) throws ModernfitException {
    final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
    requestInfoBuilder.addUrlPathAsConstant("/user/");
    requestInfoBuilder.addUrlPathNotEncoded(id, this.urlConverter0);
    requestInfoBuilder.addHttpMethod(HttpMethod.PUT);
    requestInfoBuilder.addHeaders(this.getHeaders());
    final OneObjectDiscreteBody.Builder<UpdateUserTO> bodyBuilder = OneObjectDiscreteBody.builder();
    bodyBuilder.addConverter(this.requestConverter3);
    bodyBuilder.addBody(user);
    return Single.defer(() -> Single.just(ApplicationJsonRepositoryRxJava3Impl.this.toHttpInfo(ApplicationJsonRepositoryRxJava3Impl.this.responseConverter1, ApplicationJsonRepositoryRxJava3Impl.this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()))));
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

    public ApplicationJsonRepositoryRxJava3Impl build() {
      return new ApplicationJsonRepositoryRxJava3Impl(this);
    }
  }
}
