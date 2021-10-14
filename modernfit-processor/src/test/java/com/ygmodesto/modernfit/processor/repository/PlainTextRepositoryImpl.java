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
import com.ygmodesto.modernfit.services.AbstractInterfaceImpl;
import com.ygmodesto.modernfit.services.BodyContent;
import com.ygmodesto.modernfit.services.ClientOkHttp;
import com.ygmodesto.modernfit.services.CustomType;
import com.ygmodesto.modernfit.services.HttpClient;
import com.ygmodesto.modernfit.services.HttpMethod;
import com.ygmodesto.modernfit.services.ModernfitException;
import com.ygmodesto.modernfit.services.OneObjectDiscreteBody;
import com.ygmodesto.modernfit.services.RequestInfo;
import com.ygmodesto.modernfit.services.ResponseContent;

import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.Void;

public class PlainTextRepositoryImpl extends AbstractInterfaceImpl implements PlainTextRepository {

	private JacksonConverterFactory converterFactory;

    private Converter<Long, String> urlConverter0;
    	  
	private Converter<Void, BodyContent> requestConverter0;
	
	private Converter<Long, BodyContent> requestConverter1;
	  
	  private Converter<String, BodyContent> requestConverter2;
	  
	  private Converter<ResponseContent, Long> responseConverter0;
	  
	  private Converter<ResponseContent, String> responseConverter1;
	
	private PlainTextRepositoryImpl(Builder builder) {
		baseUrl = "http://localhost:8080/api";
		httpClient = ( builder.httpClient == null) ? ClientOkHttp.create() : builder.httpClient;
		converterFactory = ( builder.converterFactory == null) ? JacksonConverterFactory.create() : builder.converterFactory;
        Long zombieurlConverter0 = null;
        urlConverter0 = converterFactory.getUrlConverter(zombieurlConverter0, new CustomType<Long>() {});
	    Void zombierequestConverter0 = null;
	    requestConverter0 = converterFactory.getRequestConverter(zombierequestConverter0, new CustomType<Void>() {});
	    Long zombierequestConverter1 = null;
	    requestConverter1 = converterFactory.getRequestConverter(zombierequestConverter1, new CustomType<Long>() {});
	    String zombierequestConverter2 = null;
	    requestConverter2 = converterFactory.getRequestConverter(zombierequestConverter2, new CustomType<String>() {});
	    Long zombieresponseConverter0 = null;
	    responseConverter0 = converterFactory.getResponseConverter(zombieresponseConverter0, new CustomType<Long>() {});
	    String zombieresponseConverter1 = null;
	    responseConverter1 = converterFactory.getResponseConverter(zombieresponseConverter1, new CustomType<String>() {});
	}



	public static Builder builder() {
		return new Builder();
	}

	@Override
	public Long getLongResponse(Long id) throws ModernfitException {

		final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
		requestInfoBuilder.addUrlPathAsConstant("/user/");
		requestInfoBuilder.addUrlPathNotEncoded(id, this.urlConverter0);
		requestInfoBuilder.addUrlPathAsConstant("/credit");
		requestInfoBuilder.addHttpMethod(HttpMethod.GET);
		requestInfoBuilder.addHeaders(this.getHeaders());
		final OneObjectDiscreteBody.Builder<Void> bodyBuilder = OneObjectDiscreteBody.builder();
		bodyBuilder.addConverter(this.requestConverter0);
		return this.responseConverter0.convert(this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()));
	}

	
	@Override
	public Long postLongRequestAndLongResponse(Long id, Long body) throws ModernfitException {
		
		final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
		requestInfoBuilder.addUrlPathAsConstant("/user/");
		requestInfoBuilder.addUrlPathNotEncoded(id, this.urlConverter0);
		requestInfoBuilder.addUrlPathAsConstant("/credit");
		requestInfoBuilder.addHttpMethod(HttpMethod.POST);
		requestInfoBuilder.addHeaders(this.getHeaders());
		final OneObjectDiscreteBody.Builder<Long> bodyBuilder = OneObjectDiscreteBody.builder();
		bodyBuilder.addConverter(this.requestConverter1);
		bodyBuilder.addBody(body);
		return this.responseConverter0.convert(this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()));
	}
	

	
	@Override
	public long postLongUnboxedRequestAndLongUnboxedResponse(Long id, long body) throws ModernfitException {

		final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
		requestInfoBuilder.addUrlPathAsConstant("/user/");
		requestInfoBuilder.addUrlPathNotEncoded(id, this.urlConverter0);
		requestInfoBuilder.addUrlPathAsConstant("/credit");
		requestInfoBuilder.addHttpMethod(HttpMethod.POST);
		requestInfoBuilder.addHeaders(this.getHeaders());
		final OneObjectDiscreteBody.Builder<Long> bodyBuilder = OneObjectDiscreteBody.builder();
		bodyBuilder.addConverter(this.requestConverter1);
		bodyBuilder.addBody(body);
		return this.responseConverter0.convert(this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()));
	}



	@Override
	public String getStringResponse(Long id) throws ModernfitException {

		final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
		requestInfoBuilder.addUrlPathAsConstant("/user/");
		requestInfoBuilder.addUrlPathNotEncoded(id, this.urlConverter0);
		requestInfoBuilder.addUrlPathAsConstant("/key");
		requestInfoBuilder.addHttpMethod(HttpMethod.GET);
		requestInfoBuilder.addHeaders(this.getHeaders());
		final OneObjectDiscreteBody.Builder<Void> bodyBuilder = OneObjectDiscreteBody.builder();
		bodyBuilder.addConverter(this.requestConverter0);
		return this.responseConverter1.convert(this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()));
	}
	

	@Override
	public String postStringRequestAndStringResponse(Long id, String address) throws ModernfitException {
		
		final RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(this.baseUrl);
		requestInfoBuilder.addUrlPathAsConstant("/user/");
		requestInfoBuilder.addUrlPathNotEncoded(id, this.urlConverter0);
		requestInfoBuilder.addUrlPathAsConstant("/create/address");
		requestInfoBuilder.addHttpMethod(HttpMethod.POST);
		requestInfoBuilder.addHeaders(this.getHeaders());
		final OneObjectDiscreteBody.Builder<String> bodyBuilder = OneObjectDiscreteBody.builder();
		bodyBuilder.addConverter(this.requestConverter2);
		bodyBuilder.addBody(address);
		return this.responseConverter1.convert(this.httpClient.callMethod(requestInfoBuilder.build(), bodyBuilder.build()));
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

		public PlainTextRepositoryImpl build() {
			return new PlainTextRepositoryImpl(this);
		}
	}

}
