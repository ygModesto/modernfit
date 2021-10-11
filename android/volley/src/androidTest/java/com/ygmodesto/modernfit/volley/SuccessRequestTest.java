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

package com.ygmodesto.modernfit.volley;

import android.content.Context;

import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.ygmodesto.modernfit.converters.Converter;
import com.ygmodesto.modernfit.converters.GsonConverterFactory;
import com.ygmodesto.modernfit.services.BodyContent;
import com.ygmodesto.modernfit.services.ResponseCallback;
import com.ygmodesto.modernfit.services.CustomType;
import com.ygmodesto.modernfit.services.DiscreteBody;
import com.ygmodesto.modernfit.services.FormUrlEncodedBody;
import com.ygmodesto.modernfit.services.HttpClient;
import com.ygmodesto.modernfit.services.HttpMethod;
import com.ygmodesto.modernfit.services.ModernfitException;
import com.ygmodesto.modernfit.services.MultipartBody;
import com.ygmodesto.modernfit.services.OneObjectDiscreteBody;
import com.ygmodesto.modernfit.services.RequestInfo;
import com.ygmodesto.modernfit.services.ResponseContent;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

import io.reactivex.observers.TestObserver;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okhttp3.tls.internal.TlsUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


@RunWith(AndroidJUnit4.class)
public class SuccessRequestTest {

    public static final String CONTENT_TYPE_HEADER = "Content-Type";

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    public static final MediaType TEXT_PLAIN
            = MediaType.get("text/plain; charset=utf-8");

    private static MockWebServer mockWebServer;
    private static Dispatcher dispatcher;
    private static HttpClient clientVolley;
    private static Context appContext;
    private static GsonConverterFactory gsonConverterFactory;
    private static Gson gson = new Gson();

    @BeforeClass
    public static void setUp() throws Exception {

        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        AndroidModernfitContext.setContext(appContext);

        RequestQueue requestQueue = Volley.newRequestQueue(appContext, new HurlStack(null, TlsUtil.localhost().sslSocketFactory()));
        clientVolley = ClientVolley.create(requestQueue);

        gsonConverterFactory = GsonConverterFactory.create(new Gson());

        mockWebServer = new MockWebServer();
        mockWebServer.useHttps(TlsUtil.localhost().sslSocketFactory(), false);
        mockWebServer.start();
        initDispatcher(mockWebServer);
    }


    @Test
    public void syncGetVoidResponseTest() {

        HttpUrl baseUrl = mockWebServer.url("/api/void");
        HttpMethod httpMethod = HttpMethod.GET;
        String keyA="keyA";
        String valueA="valueA";
        String keyHeader1="header1";
        String valueHeader1="value1";

        Converter<Void, BodyContent> requestConverter1;
        Void zombierequestConverter1 = null;
        requestConverter1 = gsonConverterFactory.getRequestConverter(zombierequestConverter1, new CustomType<Void>() {});

        Converter<String, String> urlConverter1;
        String zombieurlConverter1 = null;
        urlConverter1 = gsonConverterFactory.getUrlConverter(zombieurlConverter1, new CustomType<String>() {});

        Converter<ResponseContent, Void> responseConverter1;
        Void zombieresponseConverter1 = null;
        responseConverter1 = gsonConverterFactory.getResponseConverter(zombieresponseConverter1, new CustomType<Void>() {});

        RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(baseUrl.toString());
        requestInfoBuilder.addHttpMethod(httpMethod);
        requestInfoBuilder.addHeader(keyHeader1, valueHeader1);
        requestInfoBuilder.addParameter(keyA, valueA, urlConverter1);
        final OneObjectDiscreteBody.Builder<Void> bodyBuilder = OneObjectDiscreteBody.builder();
        bodyBuilder.addConverter(requestConverter1);

        DiscreteBody discreteBody = bodyBuilder.build();


        ResponseContent responseContent = clientVolley.callMethod(requestInfoBuilder.build(), discreteBody);
        Void voidResponse = responseConverter1.convert(responseContent);

        assertNull(responseContent.getContentType());
        assertNull(responseContent.getCharset());
        assertEquals(0L, responseContent.getContent().length);
    }


    
    @Test
    public void syncGetTest() {

        HttpUrl baseUrl = mockWebServer.url("/api/echo");
        HttpMethod httpMethod = HttpMethod.GET;
        String keyA="keyA";
        String valueA="valueA";
        String keyHeader1="header1";
        String valueHeader1="value1";

        Converter<Void, BodyContent> requestConverter1;
        Void zombierequestConverter1 = null;
        requestConverter1 = gsonConverterFactory.getRequestConverter(zombierequestConverter1, new CustomType<Void>() {});

        Converter<String, String> urlConverter1;
        String zombieurlConverter1 = null;
        urlConverter1 = gsonConverterFactory.getUrlConverter(zombieurlConverter1, new CustomType<String>() {});

        Converter<ResponseContent, EchoResponse> responseConverter1;
        EchoResponse zombieresponseConverter1 = null;
        responseConverter1 = gsonConverterFactory.getResponseConverter(zombieresponseConverter1, new CustomType<EchoResponse>() {});

        RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(baseUrl.toString());
        requestInfoBuilder.addHttpMethod(httpMethod);
        requestInfoBuilder.addHeader(keyHeader1, valueHeader1);
        requestInfoBuilder.addParameter(keyA, valueA, urlConverter1);
        final OneObjectDiscreteBody.Builder<Void> bodyBuilder = OneObjectDiscreteBody.builder();
        bodyBuilder.addConverter(requestConverter1);

        DiscreteBody discreteBody = bodyBuilder.build();


        ResponseContent responseContent = clientVolley.callMethod(requestInfoBuilder.build(), discreteBody);
        EchoResponse echoResponse = responseConverter1.convert(responseContent);

        assertEquals("application/json", responseContent.getContentType());
        assertEquals(StandardCharsets.UTF_8, responseContent.getCharset());

        assertEquals("/api/echo", echoResponse.getUrl());
        assertNull(echoResponse.getContentType());
        assertEquals("value1", echoResponse.getHeaders().get("header1").get(0));
        assertNull(echoResponse.getBody());
        assertEquals(httpMethod.name(), echoResponse.getMethod());
        assertEquals(keyA+"="+valueA, echoResponse.getQueryString());
    }

    
    @Test
    public void syncPostTextPlainTest() {

        HttpUrl baseUrl = mockWebServer.url("/api/echo");
        HttpMethod httpMethod = HttpMethod.POST;
        Long bodyLong = 5L;

        Converter<Long, BodyContent> requestConverter1;
        Long zombierequestConverter1 = null;
        requestConverter1 = gsonConverterFactory.getRequestConverter(zombierequestConverter1, new CustomType<Long>() {});


        Converter<ResponseContent, EchoResponse> responseConverter1;
        EchoResponse zombieresponseConverter1 = null;
        responseConverter1 = gsonConverterFactory.getResponseConverter(zombieresponseConverter1, new CustomType<EchoResponse>() {});

        RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(baseUrl.toString());
        requestInfoBuilder.addHttpMethod(httpMethod);
        final OneObjectDiscreteBody.Builder<Long> bodyBuilder = OneObjectDiscreteBody.builder();
        bodyBuilder.addConverter(requestConverter1);

        bodyBuilder.addBody(bodyLong);
        DiscreteBody discreteBody = bodyBuilder.build();


        ResponseContent responseContent = clientVolley.callMethod(requestInfoBuilder.build(), discreteBody);
        EchoResponse echoResponse = responseConverter1.convert(responseContent);

        assertEquals("application/json", responseContent.getContentType());
        assertEquals(StandardCharsets.UTF_8, responseContent.getCharset());

        assertEquals("/api/echo", echoResponse.getUrl());
        assertEquals("text/plain;charset=utf-8", echoResponse.getContentType().toLowerCase());
        assertEquals(gson.toJson(bodyLong), echoResponse.getBody());
        assertEquals(httpMethod.name(), echoResponse.getMethod());
    }

    
    @Test
    public void syncGetResponseTextPlainTest() {

        HttpUrl baseUrl = mockWebServer.url("/api/scalar");
        HttpMethod httpMethod = HttpMethod.GET;

        Converter<Void, BodyContent> requestConverter1;
        Void zombierequestConverter1 = null;
        requestConverter1 = gsonConverterFactory.getRequestConverter(zombierequestConverter1, new CustomType<Void>() {});

        Converter<ResponseContent, Long> responseConverter1;
        Long zombieresponseConverter1 = null;
        responseConverter1 = gsonConverterFactory.getResponseConverter(zombieresponseConverter1, new CustomType<Long>() {});

        RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(baseUrl.toString());
        requestInfoBuilder.addHttpMethod(httpMethod);

        final OneObjectDiscreteBody.Builder<Void> bodyBuilder = OneObjectDiscreteBody.builder();
        bodyBuilder.addConverter(requestConverter1);
        DiscreteBody discreteBody = bodyBuilder.build();

        ResponseContent responseContent = clientVolley.callMethod(requestInfoBuilder.build(), discreteBody);
        Long longResponse = responseConverter1.convert(responseContent);

        assertEquals("text/plain", responseContent.getContentType());
        assertEquals(StandardCharsets.UTF_8, responseContent.getCharset());

        assertEquals(Long.valueOf(5L), longResponse);
    }

    
    @Test
    public void syncPostApplicationJsonTest() {

        HttpUrl baseUrl = mockWebServer.url("/api/echo");
        HttpMethod httpMethod = HttpMethod.POST;
        ModelTO modelTO = new ModelTO(5L, "name", "@login");
        String keyA="keyA";
        String valueA="valueA";
        String keyHeader1="header1";
        String valueHeader1="value1";

        Converter<ModelTO, BodyContent> requestConverter1;
        ModelTO zombierequestConverter1 = null;
        requestConverter1 = gsonConverterFactory.getRequestConverter(zombierequestConverter1, new CustomType<ModelTO>() {});

        Converter<String, String> urlConverter1;
        String zombieurlConverter1 = null;
        urlConverter1 = gsonConverterFactory.getUrlConverter(zombieurlConverter1, new CustomType<String>() {});

        Converter<ResponseContent, EchoResponse> responseConverter1;
        EchoResponse zombieresponseConverter1 = null;
        responseConverter1 = gsonConverterFactory.getResponseConverter(zombieresponseConverter1, new CustomType<EchoResponse>() {});

        RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(baseUrl.toString());
        requestInfoBuilder.addHttpMethod(httpMethod);
        requestInfoBuilder.addHeader(keyHeader1, valueHeader1);
        requestInfoBuilder.addParameter(keyA, valueA, urlConverter1);
        final OneObjectDiscreteBody.Builder<ModelTO> bodyBuilder = OneObjectDiscreteBody.builder();
        bodyBuilder.addConverter(requestConverter1);

        bodyBuilder.addBody(modelTO);
        DiscreteBody discreteBody = bodyBuilder.build();


        ResponseContent responseContent = clientVolley.callMethod(requestInfoBuilder.build(), discreteBody);
        EchoResponse echoResponse = responseConverter1.convert(responseContent);

        assertEquals("application/json", responseContent.getContentType());
        assertEquals(StandardCharsets.UTF_8, responseContent.getCharset());

        assertEquals("/api/echo", echoResponse.getUrl());
        assertEquals("application/json;charset=utf-8", echoResponse.getContentType().toLowerCase());
        assertEquals("value1", echoResponse.getHeaders().get("header1").get(0));
        assertEquals(gson.toJson(modelTO), echoResponse.getBody());
        assertEquals(httpMethod.name(), echoResponse.getMethod());
        assertEquals(keyA+"="+valueA, echoResponse.getQueryString());
    }


    
    @Test
    public void sync404PostApplicationJsonTest() {

        HttpUrl baseUrl = mockWebServer.url("/api/404/echo");
        HttpMethod httpMethod = HttpMethod.POST;
        ModelTO modelTO = new ModelTO(5L, "name", "@login");
        String keyA="keyA";
        String valueA="valueA";
        String keyHeader1="header1";
        String valueHeader1="value1";

        Converter<ModelTO, BodyContent> requestConverter1;
        ModelTO zombierequestConverter1 = null;
        requestConverter1 = gsonConverterFactory.getRequestConverter(zombierequestConverter1, new CustomType<ModelTO>() {});

        Converter<String, String> urlConverter1;
        String zombieurlConverter1 = null;
        urlConverter1 = gsonConverterFactory.getUrlConverter(zombieurlConverter1, new CustomType<String>() {});

        Converter<ResponseContent, EchoResponse> responseConverter1;
        EchoResponse zombieresponseConverter1 = null;
        responseConverter1 = gsonConverterFactory.getResponseConverter(zombieresponseConverter1, new CustomType<EchoResponse>() {});

        RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(baseUrl.toString());
        requestInfoBuilder.addHttpMethod(httpMethod);
        requestInfoBuilder.addHeader(keyHeader1, valueHeader1);
        requestInfoBuilder.addParameter(keyA, valueA, urlConverter1);
        final OneObjectDiscreteBody.Builder<ModelTO> bodyBuilder = OneObjectDiscreteBody.builder();
        bodyBuilder.addConverter(requestConverter1);

        bodyBuilder.addBody(modelTO);
        DiscreteBody discreteBody = bodyBuilder.build();


        ResponseContent responseContent = clientVolley.callMethod(requestInfoBuilder.build(), discreteBody);
        EchoResponse echoResponse = responseConverter1.convert(responseContent);

        assertEquals("application/json", responseContent.getContentType());
        assertEquals(StandardCharsets.UTF_8, responseContent.getCharset());

        assertEquals("/api/404/echo", echoResponse.getUrl());
        assertEquals("application/json;charset=utf-8", echoResponse.getContentType().toLowerCase());
        assertEquals("value1", echoResponse.getHeaders().get("header1").get(0));
        assertEquals(gson.toJson(modelTO), echoResponse.getBody());
        assertEquals(httpMethod.name(), echoResponse.getMethod());
        assertEquals(keyA+"="+valueA, echoResponse.getQueryString());
    }


    
    @Test
    public void sync500PostApplicationJsonTest() {

        HttpUrl baseUrl = mockWebServer.url("/api/500/echo");
        HttpMethod httpMethod = HttpMethod.POST;
        ModelTO modelTO = new ModelTO(5L, "name", "@login");
        String keyA="keyA";
        String valueA="valueA";
        String keyHeader1="header1";
        String valueHeader1="value1";

        Converter<ModelTO, BodyContent> requestConverter1;
        ModelTO zombierequestConverter1 = null;
        requestConverter1 = gsonConverterFactory.getRequestConverter(zombierequestConverter1, new CustomType<ModelTO>() {});

        Converter<String, String> urlConverter1;
        String zombieurlConverter1 = null;
        urlConverter1 = gsonConverterFactory.getUrlConverter(zombieurlConverter1, new CustomType<String>() {});

        Converter<ResponseContent, EchoResponse> responseConverter1;
        EchoResponse zombieresponseConverter1 = null;
        responseConverter1 = gsonConverterFactory.getResponseConverter(zombieresponseConverter1, new CustomType<EchoResponse>() {});

        RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(baseUrl.toString());
        requestInfoBuilder.addHttpMethod(httpMethod);
        requestInfoBuilder.addHeader(keyHeader1, valueHeader1);
        requestInfoBuilder.addParameter(keyA, valueA, urlConverter1);
        final OneObjectDiscreteBody.Builder<ModelTO> bodyBuilder = OneObjectDiscreteBody.builder();
        bodyBuilder.addConverter(requestConverter1);

        bodyBuilder.addBody(modelTO);
        DiscreteBody discreteBody = bodyBuilder.build();


        ResponseContent responseContent = clientVolley.callMethod(requestInfoBuilder.build(), discreteBody);
        EchoResponse echoResponse = responseConverter1.convert(responseContent);

        assertEquals("application/json", responseContent.getContentType());
        assertEquals(StandardCharsets.UTF_8, responseContent.getCharset());

        assertEquals("/api/500/echo", echoResponse.getUrl());
        assertEquals("application/json;charset=utf-8", echoResponse.getContentType().toLowerCase());
        assertEquals("value1", echoResponse.getHeaders().get("header1").get(0));
        assertEquals(gson.toJson(modelTO), echoResponse.getBody());
        assertEquals(httpMethod.name(), echoResponse.getMethod());
        assertEquals(keyA+"="+valueA, echoResponse.getQueryString());
    }


    
    @Test
    public void syncPostFormUrlEncodedTest() {

        HttpUrl baseUrl = mockWebServer.url("/api/echo");
        HttpMethod httpMethod = HttpMethod.POST;
        String keyA="keyA";
        String valueA="valueA";
        String param1="param1";
        String value1="value1";
        String param2="param2";
        String value2="value2";
        String keyHeader1="header1";
        String valueHeader1="value1";

        Converter<String, String> urlConverter1;
        String zombieurlConverter1 = null;
        urlConverter1 = gsonConverterFactory.getUrlConverter(zombieurlConverter1, new CustomType<String>() {});

        Converter<String, BodyContent> requestConverter2;
        String zombierequestConverter2 = null;
        requestConverter2 = gsonConverterFactory.getRequestConverter(zombierequestConverter2, new CustomType<String>() {});



        Converter<ResponseContent, EchoResponse> responseConverter1;
        EchoResponse zombieresponseConverter1 = null;
        responseConverter1 = gsonConverterFactory.getResponseConverter(zombieresponseConverter1, new CustomType<EchoResponse>() {});

        RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(baseUrl.toString());
        requestInfoBuilder.addHttpMethod(httpMethod);
        requestInfoBuilder.addHeader(keyHeader1, valueHeader1);
        requestInfoBuilder.addParameter(keyA, valueA, urlConverter1);
        final FormUrlEncodedBody.Builder bodyBuilder = FormUrlEncodedBody.builder();
        bodyBuilder.addField(param1,value1, requestConverter2);
        bodyBuilder.addField(param2,value2, requestConverter2);

        DiscreteBody discreteBody = bodyBuilder.build();


        ResponseContent responseContent = clientVolley.callMethod(requestInfoBuilder.build(), discreteBody);
        EchoResponse echoResponse = responseConverter1.convert(responseContent);

        assertEquals("application/json", responseContent.getContentType());
        assertEquals(StandardCharsets.UTF_8, responseContent.getCharset());

        assertEquals("/api/echo", echoResponse.getUrl());
        assertEquals("application/x-www-form-urlencoded", echoResponse.getContentType().toLowerCase());
        assertEquals("value1", echoResponse.getHeaders().get("header1").get(0));
        assertEquals(param1+"="+value1+"&"+param2+"="+value2, echoResponse.getBody());
        assertEquals(httpMethod.name(), echoResponse.getMethod());
        assertEquals(keyA+"="+valueA, echoResponse.getQueryString());
    }



    @Test
    public void asyncGetTest() throws ExecutionException, InterruptedException {

        HttpUrl baseUrl = mockWebServer.url("/api/echo");
        HttpMethod httpMethod = HttpMethod.GET;
        String keyA="keyA";
        String valueA="valueA";
        String keyHeader1="header1";
        String valueHeader1="value1";

        Converter<Void, BodyContent> requestConverter1;
        Void zombierequestConverter1 = null;
        requestConverter1 = gsonConverterFactory.getRequestConverter(zombierequestConverter1, new CustomType<Void>() {});

        Converter<String, String> urlConverter1;
        String zombierequestConverter2 = null;
        urlConverter1 = gsonConverterFactory.getUrlConverter(zombierequestConverter2, new CustomType<String>() {});

        Converter<ResponseContent, EchoResponse> responseConverter1;
        EchoResponse zombieresponseConverter1 = null;
        responseConverter1 = gsonConverterFactory.getResponseConverter(zombieresponseConverter1, new CustomType<EchoResponse>() {});

        RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(baseUrl.toString());
        requestInfoBuilder.addHttpMethod(httpMethod);
        requestInfoBuilder.addHeader(keyHeader1, valueHeader1);
        requestInfoBuilder.addParameter(keyA, valueA, urlConverter1);
        final OneObjectDiscreteBody.Builder<Void> bodyBuilder = OneObjectDiscreteBody.builder();
        bodyBuilder.addConverter(requestConverter1);
        DiscreteBody discreteBody = bodyBuilder.build();



        ListenableFuture<EchoResponse> future = CallbackToFutureAdapter.getFuture(completer -> {
            ResponseCallback<EchoResponse> responseCallback = new ResponseCallback<EchoResponse>() {


                @Override
                public void onSuccess(EchoResponse echoResponse) {
                    completer.set(echoResponse);
                }

                @Override
                public void onFailure(ModernfitException e) {
                    completer.setException(e);
                }
            };
            responseCallback.setConverter(responseConverter1);


            clientVolley.callMethod(requestInfoBuilder.build(), discreteBody, responseCallback);
            // This value is used only for debug purposes: it will be used in toString()
            // of returned future or error cases.
            return "responseCallbackFuture";
        });

        EchoResponse echoResponse = future.get();
        assertEquals("/api/echo", echoResponse.getUrl());
        assertNull(echoResponse.getContentType());
        assertEquals("value1", echoResponse.getHeaders().get("header1").get(0));
        assertNull(echoResponse.getBody());
        assertEquals(httpMethod.name(), echoResponse.getMethod());
        assertEquals(keyA+"="+valueA, echoResponse.getQueryString());
    }


    
    @Test
    public void asyncPostApplicationJsonTest() throws ExecutionException, InterruptedException {

        HttpUrl baseUrl = mockWebServer.url("/api/echo");
        HttpMethod httpMethod = HttpMethod.POST;
        ModelTO modelTO = new ModelTO(5L, "name", "@login");
        String keyA="keyA";
        String valueA="valueA";
        String keyHeader1="header1";
        String valueHeader1="value1";

        Converter<ModelTO, BodyContent> requestConverter1;
        ModelTO zombierequestConverter1 = null;
        requestConverter1 = gsonConverterFactory.getRequestConverter(zombierequestConverter1, new CustomType<ModelTO>() {});

        Converter<String, String> urlConverter1;
        String zombieurlConverter1 = null;
        urlConverter1 = gsonConverterFactory.getUrlConverter(zombieurlConverter1, new CustomType<String>() {});

        Converter<ResponseContent, EchoResponse> responseConverter1;
        EchoResponse zombieresponseConverter1 = null;
        responseConverter1 = gsonConverterFactory.getResponseConverter(zombieresponseConverter1, new CustomType<EchoResponse>() {});

        RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(baseUrl.toString());
        requestInfoBuilder.addHttpMethod(httpMethod);
        requestInfoBuilder.addHeader(keyHeader1, valueHeader1);
        requestInfoBuilder.addParameter(keyA, valueA, urlConverter1);
        final OneObjectDiscreteBody.Builder<ModelTO> bodyBuilder = OneObjectDiscreteBody.builder();
        bodyBuilder.addConverter(requestConverter1);

        bodyBuilder.addBody(modelTO);
        DiscreteBody discreteBody = bodyBuilder.build();


        ListenableFuture<EchoResponse> future = CallbackToFutureAdapter.getFuture(completer -> {
            ResponseCallback<EchoResponse> responseCallback = new ResponseCallback<EchoResponse>() {


                @Override
                public void onSuccess(EchoResponse echoResponse) {
                    completer.set(echoResponse);
                }

                @Override
                public void onFailure(ModernfitException e) {
                    completer.setException(e);
                }
            };
            responseCallback.setConverter(responseConverter1);


            clientVolley.callMethod(requestInfoBuilder.build(), discreteBody, responseCallback);
            // This value is used only for debug purposes: it will be used in toString()
            // of returned future or error cases.
            return "responseCallbackFuture";
        });

        EchoResponse echoResponse = future.get();

        assertEquals("/api/echo", echoResponse.getUrl());
        assertEquals("application/json;charset=utf-8", echoResponse.getContentType().toLowerCase());
        assertEquals("value1", echoResponse.getHeaders().get("header1").get(0));
        assertEquals(gson.toJson(modelTO), echoResponse.getBody());
        assertEquals(httpMethod.name(), echoResponse.getMethod());
        assertEquals(keyA+"="+valueA, echoResponse.getQueryString());
    }




    
    @Test
    public void asyncPostFormUrlEncodedTest() throws ExecutionException, InterruptedException {

        HttpUrl baseUrl = mockWebServer.url("/api/echo");
        HttpMethod httpMethod = HttpMethod.POST;
        String keyA="keyA";
        String valueA="valueA";
        String param1="param1";
        String value1="value1";
        String param2="param2";
        String value2="value2";
        String keyHeader1="header1";
        String valueHeader1="value1";


        Converter<String, String> urlConverter1;
        String zombieurlConverter1 = null;
        urlConverter1 = gsonConverterFactory.getUrlConverter(zombieurlConverter1, new CustomType<String>() {});

        Converter<String, BodyContent> requestConverter2;
        String zombierequestConverter2 = null;
        requestConverter2 = gsonConverterFactory.getRequestConverter(zombierequestConverter2, new CustomType<String>() {});



        Converter<ResponseContent, EchoResponse> responseConverter1;
        EchoResponse zombieresponseConverter1 = null;
        responseConverter1 = gsonConverterFactory.getResponseConverter(zombieresponseConverter1, new CustomType<EchoResponse>() {});

        RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(baseUrl.toString());
        requestInfoBuilder.addHttpMethod(httpMethod);
        requestInfoBuilder.addHeader(keyHeader1, valueHeader1);
        requestInfoBuilder.addParameter(keyA, valueA, urlConverter1);
        final FormUrlEncodedBody.Builder bodyBuilder = FormUrlEncodedBody.builder();
        bodyBuilder.addField(param1,value1, requestConverter2);
        bodyBuilder.addField(param2,value2, requestConverter2);

        DiscreteBody discreteBody = bodyBuilder.build();


        ListenableFuture<EchoResponse> future = CallbackToFutureAdapter.getFuture(completer -> {
            ResponseCallback<EchoResponse> responseCallback = new ResponseCallback<EchoResponse>() {


                @Override
                public void onSuccess(EchoResponse echoResponse) {
                    completer.set(echoResponse);
                }

                @Override
                public void onFailure(ModernfitException e) {
                    completer.setException(e);
                }
            };
            responseCallback.setConverter(responseConverter1);


            clientVolley.callMethod(requestInfoBuilder.build(), discreteBody, responseCallback);
            // This value is used only for debug purposes: it will be used in toString()
            // of returned future or error cases.
            return "responseCallbackFuture";
        });

        EchoResponse echoResponse = future.get();


        assertEquals("/api/echo", echoResponse.getUrl());
        assertEquals("application/x-www-form-urlencoded", echoResponse.getContentType().toLowerCase());
        assertEquals("value1", echoResponse.getHeaders().get("header1").get(0));
        assertEquals(param1+"="+value1+"&"+param2+"="+value2, echoResponse.getBody());
        assertEquals(httpMethod.name(), echoResponse.getMethod());
        assertEquals(keyA+"="+valueA, echoResponse.getQueryString());
    }


    
//    @Test
//    public void syncGetMultipartTest() throws IOException, ModernfitCallException {
//        try{
//
//            HttpUrl baseUrl = mockWebServer.url("/api/multipart/echo");
//            HttpMethod httpMethod = HttpMethod.GET;
//            ModelTO modelTOA = new ModelTO(5L, "nameA", "@loginA");
//            ModelTO modelTOB = new ModelTO(6L, "nameB", "@loginB");
//            String keyA="keyA";
//            String valueA="valueA";
//            String keyHeader1="header1";
//            String valueHeader1="value1";
//
//            Converter<Void, BodyContent> requestConverter1;
//            Void zombierequestConverter1 = null;
//            requestConverter1 = gsonConverterFactory.getRequestConverter(zombierequestConverter1, new CustomType<Void>() {});
//
//            Converter<String, String> urlConverter1;
//            String zombieurlConverter1 = null;
//            urlConverter1 = gsonConverterFactory.getUrlConverter(zombieurlConverter1, new CustomType<String>() {});
//
//            Converter<ResponseContent, MultipartEchoResponse> responseConverter1;
//            MultipartEchoResponse zombieresponseConverter1 = null;
//            responseConverter1 = gsonConverterFactory.getResponseConverter(zombieresponseConverter1, new CustomType<MultipartEchoResponse>() {});
//
//            RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(baseUrl.toString());
//            requestInfoBuilder.addHttpMethod(httpMethod);
//            requestInfoBuilder.addHeader(keyHeader1, valueHeader1);
//            requestInfoBuilder.addParameter(keyA, valueA, requestConverter2);
//
//            MultipartBody.Builder multipartBodyBuilder = MultipartBody.builder();
//
//            ResponseContent responseContent = clientVolley.callMethod(requestInfoBuilder.build(), multipartBodyBuilder.build());
//            MultipartEchoResponse echoResponse = responseConverter1.convert(responseContent);
//
//            assertEquals("/api/multipart/echo", echoResponse.getUrl());
//            assertEquals("multipart/form-data;boundary=", echoResponse.getContentType().toLowerCase().substring(0, 29));
//            assertEquals("value1", echoResponse.getHeaders().get("header1").get(0));
//            assertNull(echoResponse.getParts());
//            assertEquals(httpMethod.name(), echoResponse.getMethod());
//            assertEquals(keyA+"="+valueA, echoResponse.getQueryString());
//        }finally {
//            dispatcher.shutdown();
//            mockWebServer.shutdown();
//        }
//    }

    
    @Test
    public void syncPostMultipartTest() {

        HttpUrl baseUrl = mockWebServer.url("/api/multipart/echo");
        HttpMethod httpMethod = HttpMethod.POST;
        ModelTO modelTOA = new ModelTO(5L, "nameA", "@loginA");
        ModelTO modelTOB = new ModelTO(6L, "nameB", "@loginB");
        String keyA="keyA";
        String valueA="valueA";
        String keyHeader1="header1";
        String valueHeader1="value1";

        Converter<ModelTO, BodyContent> requestConverter1;
        ModelTO zombierequestConverter1 = null;
        requestConverter1 = gsonConverterFactory.getRequestConverter(zombierequestConverter1, new CustomType<ModelTO>() {});

        Converter<String, String> urlConverter1;
        String zombieurlConverter1 = null;
        urlConverter1 = gsonConverterFactory.getUrlConverter(zombieurlConverter1, new CustomType<String>() {});

        Converter<ResponseContent, MultipartEchoResponse> responseConverter1;
        MultipartEchoResponse zombieresponseConverter1 = null;
        responseConverter1 = gsonConverterFactory.getResponseConverter(zombieresponseConverter1, new CustomType<MultipartEchoResponse>() {});

        RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(baseUrl.toString());
        requestInfoBuilder.addHttpMethod(httpMethod);
        requestInfoBuilder.addHeader(keyHeader1, valueHeader1);
        requestInfoBuilder.addParameter(keyA, valueA, urlConverter1);

        MultipartBody.Builder multipartBodyBuilder = MultipartBody.builder();
        multipartBodyBuilder.addPart("partA", modelTOA, requestConverter1);
        multipartBodyBuilder.addPart("partB", modelTOB, requestConverter1);


        ResponseContent responseContent = clientVolley.callMethod(requestInfoBuilder.build(), multipartBodyBuilder.build());
        MultipartEchoResponse echoResponse = responseConverter1.convert(responseContent);


        assertEquals("/api/multipart/echo", echoResponse.getUrl());
        assertEquals("multipart/form-data;boundary=", echoResponse.getContentType().toLowerCase().substring(0, 29));
        assertEquals("value1", echoResponse.getHeaders().get("header1").get(0));
        assertEquals(gson.toJson(modelTOA), echoResponse.getParts().get(0));
        assertEquals(gson.toJson(modelTOB), echoResponse.getParts().get(1));
        assertEquals(httpMethod.name(), echoResponse.getMethod());
        assertEquals(keyA+"="+valueA, echoResponse.getQueryString());
    }

    
    @Test
    public void asyncPostMultipartTest() throws ExecutionException, InterruptedException {

        HttpUrl baseUrl = mockWebServer.url("/api/multipart/echo");
        HttpMethod httpMethod = HttpMethod.POST;
        ModelTO modelTOA = new ModelTO(5L, "nameA", "@loginA");
        ModelTO modelTOB = new ModelTO(6L, "nameB", "@loginB");
        String keyA="keyA";
        String valueA="valueA";
        String keyHeader1="header1";
        String valueHeader1="value1";

        Converter<ModelTO, BodyContent> requestConverter1;
        ModelTO zombierequestConverter1 = null;
        requestConverter1 = gsonConverterFactory.getRequestConverter(zombierequestConverter1, new CustomType<ModelTO>() {});

        Converter<String, String> urlConverter1;
        String zombieurlConverter1 = null;
        urlConverter1 = gsonConverterFactory.getUrlConverter(zombieurlConverter1, new CustomType<String>() {});

        Converter<ResponseContent, MultipartEchoResponse> responseConverter1;
        MultipartEchoResponse zombieresponseConverter1 = null;
        responseConverter1 = gsonConverterFactory.getResponseConverter(zombieresponseConverter1, new CustomType<MultipartEchoResponse>() {});

        RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(baseUrl.toString());
        requestInfoBuilder.addHttpMethod(httpMethod);
        requestInfoBuilder.addHeader(keyHeader1, valueHeader1);
        requestInfoBuilder.addParameter(keyA, valueA, urlConverter1);

        MultipartBody.Builder multipartBodyBuilder = MultipartBody.builder();
        multipartBodyBuilder.addPart("partA", modelTOA, requestConverter1);
        multipartBodyBuilder.addPart("partB", modelTOB, requestConverter1);
        MultipartBody multipartBody = multipartBodyBuilder.build();

        ListenableFuture<MultipartEchoResponse> future = CallbackToFutureAdapter.getFuture(completer -> {
            ResponseCallback<MultipartEchoResponse> responseCallback = new ResponseCallback<MultipartEchoResponse>() {


                @Override
                public void onSuccess(MultipartEchoResponse echoResponse) {
                    completer.set(echoResponse);
                }

                @Override
                public void onFailure(ModernfitException e) {
                    completer.setException(e);
                }
            };
            responseCallback.setConverter(responseConverter1);


            clientVolley.callMethod(requestInfoBuilder.build(), multipartBody, responseCallback);
            // This value is used only for debug purposes: it will be used in toString()
            // of returned future or error cases.
            return "responseCallbackFuture";
        });

        MultipartEchoResponse echoResponse = future.get();

        assertEquals("/api/multipart/echo", echoResponse.getUrl());
        assertEquals("multipart/form-data;boundary=", echoResponse.getContentType().toLowerCase().substring(0, 29));
        assertEquals("value1", echoResponse.getHeaders().get("header1").get(0));
        assertEquals(gson.toJson(modelTOA), echoResponse.getParts().get(0));
        assertEquals(gson.toJson(modelTOB), echoResponse.getParts().get(1));
        assertEquals(httpMethod.name(), echoResponse.getMethod());
        assertEquals(keyA+"="+valueA, echoResponse.getQueryString());
    }


    
    @Test
    public void rxPostApplicationJsonTest() throws IOException {

        TestObserver<EchoResponse> observer = new TestObserver<EchoResponse>();
        HttpUrl baseUrl = mockWebServer.url("/api/echo");
        HttpMethod httpMethod = HttpMethod.POST;
        ModelTO modelTO = new ModelTO(5L, "name", "@login");
        String keyA="keyA";
        String valueA="valueA";
        String keyHeader1="header1";
        String valueHeader1="value1";

        Converter<ModelTO, BodyContent> requestConverter1;
        ModelTO zombierequestConverter1 = null;
        requestConverter1 = gsonConverterFactory.getRequestConverter(zombierequestConverter1, new CustomType<ModelTO>() {});

        Converter<String, String> urlConverter1;
        String zombierequestConverter2 = null;
        urlConverter1 = gsonConverterFactory.getUrlConverter(zombierequestConverter2, new CustomType<String>() {});

        Converter<ResponseContent, EchoResponse> responseConverter1;
        EchoResponse zombieresponseConverter1 = null;
        responseConverter1 = gsonConverterFactory.getResponseConverter(zombieresponseConverter1, new CustomType<EchoResponse>() {});

        RequestInfo.Builder requestInfoBuilder = RequestInfo.baseUrl(baseUrl.toString());
        requestInfoBuilder.addHttpMethod(httpMethod);
        requestInfoBuilder.addHeader(keyHeader1, valueHeader1);
        requestInfoBuilder.addParameter(keyA, valueA, urlConverter1);
        final OneObjectDiscreteBody.Builder<ModelTO> bodyBuilder = OneObjectDiscreteBody.builder();
        bodyBuilder.addConverter(requestConverter1);

        bodyBuilder.addBody(modelTO);
        DiscreteBody discreteBody = bodyBuilder.build();


        ResponseContent responseContent = clientVolley.callMethod(requestInfoBuilder.build(), discreteBody);
        EchoResponse echoResponse = responseConverter1.convert(responseContent);


        assertEquals("/api/echo", echoResponse.getUrl());
        assertEquals("application/json;charset=utf-8", echoResponse.getContentType().toLowerCase());
        assertEquals("value1", echoResponse.getHeaders().get("header1").get(0));
        assertEquals(gson.toJson(modelTO), echoResponse.getBody());
        assertEquals(httpMethod.name(), echoResponse.getMethod());
        assertEquals(keyA+"="+valueA, echoResponse.getQueryString());
    }



    public static void initDispatcher(MockWebServer mockWebServer) {
        dispatcher = new Dispatcher() {

            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {

                HttpUrl httpUrl = request.getRequestUrl();
                MockResponse mockResponse = new MockResponse();

                if (httpUrl.encodedPath().equals("/api/void")) {
                    return mockResponse.setResponseCode(200);
                }if (httpUrl.encodedPath().equals("/api/scalar")) {
                    return mockResponse.setResponseCode(200)
                            .setHeader(CONTENT_TYPE_HEADER, TEXT_PLAIN.toString())
                            .setBody("5");
                }else if (httpUrl.encodedPath().equals("/api/echo")) {
                    return mockResponse.setResponseCode(200)
                            .setHeader(CONTENT_TYPE_HEADER, JSON.toString())
                            .setBody(gson.toJson(echoRequest(request)));
                }else if (httpUrl.encodedPath().equals("/api/multipart/echo")) {
                    return mockResponse.setResponseCode(200)
                            .setHeader(CONTENT_TYPE_HEADER, JSON.toString())
                            .setBody(gson.toJson(multipartEchoRequest(request)));
                }else if (httpUrl.encodedPath().equals("/api/404/echo")) {
                    return mockResponse.setResponseCode(404)
                            .setHeader(CONTENT_TYPE_HEADER, JSON.toString())
                            .setBody(gson.toJson(echoRequest(request)));
                }else if (httpUrl.encodedPath().equals("/api/500/echo")) {
                    return mockResponse.setResponseCode(500)
                            .setHeader(CONTENT_TYPE_HEADER, JSON.toString())
                            .setBody(gson.toJson(echoRequest(request)));
                }
                return new MockResponse().setResponseCode(404);
                //TODO test errores
            }
        };

        mockWebServer.setDispatcher(dispatcher);
    }

    public static EchoResponse echoRequest(RecordedRequest request) {

        HttpUrl httpUrl = request.getRequestUrl();
        EchoResponse echoResponse = new EchoResponse.Builder()
                .addHeaders(request.getHeaders().toMultimap())
                .addBody(request.getBodySize() == 0L ? null : request.getBody().readUtf8())
                .addUrl(httpUrl.encodedPath())
                .addMethod(request.getMethod())
                .addContentType(request.getHeader(CONTENT_TYPE_HEADER))
                .addQueryString(httpUrl.encodedQuery()).build();


        return echoResponse;


    }


    public static MultipartEchoResponse multipartEchoRequest(RecordedRequest request) {

        HttpUrl httpUrl = request.getRequestUrl();
        MultipartEchoResponse echoResponse = new MultipartEchoResponse();
        echoResponse.setHeaders(request.getHeaders().toMultimap());
        String body = request.getBodySize() == 0L ? null : request.getBody().readUtf8();
        echoResponse.setPartsAsString(body);
        echoResponse.setUrl(httpUrl.encodedPath());
        echoResponse.setMethod(request.getMethod());
        echoResponse.setContentType(request.getHeader(CONTENT_TYPE_HEADER));

        echoResponse.setQueryString(httpUrl.encodedQuery());


        return echoResponse;


    }

}
