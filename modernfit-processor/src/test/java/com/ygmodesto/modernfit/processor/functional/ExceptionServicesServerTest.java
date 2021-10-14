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

package com.ygmodesto.modernfit.processor.functional;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.ygmodesto.modernfit.annotations.GET;
import com.ygmodesto.modernfit.annotations.Modernfit;
import com.ygmodesto.modernfit.annotations.Multipart;
import com.ygmodesto.modernfit.converters.Converter;
import com.ygmodesto.modernfit.converters.JacksonConverterFactory;
import com.ygmodesto.modernfit.services.BodyContent;
import com.ygmodesto.modernfit.services.CustomType;
import com.ygmodesto.modernfit.services.DiscreteBody;
import com.ygmodesto.modernfit.services.HttpClient;
import com.ygmodesto.modernfit.services.ModernfitException;
import com.ygmodesto.modernfit.services.MultipartBody;
import com.ygmodesto.modernfit.services.RequestInfo;
import com.ygmodesto.modernfit.services.ResponseCallback;
import com.ygmodesto.modernfit.services.ResponseContent;
import java.util.concurrent.CompletableFuture;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ExceptionServicesServerTest extends AbstractFunctionalTest {

  private static ExceptionClientHttpRepository exceptionClientHttpRepository;
  private static ExceptionConverterRepository exceptionConverterRepository;

  @BeforeClass
  public static void setUp() throws Exception {
    exceptionClientHttpRepository =
        util(ExceptionServicesServerTest.class, ExceptionClientHttpRepository.class, "Impl");
    exceptionConverterRepository =
        util(ExceptionServicesServerTest.class, ExceptionConverterRepository.class, "Impl");
  }

  private ResponseCallback<Void> getFutureCallback(CompletableFuture<Void> future) {

    return new ResponseCallback<Void>() {
      @Override
      public void onSuccess(Void response) throws ModernfitException {
        future.complete(response);
      }

      @Override
      public void onFailure(ModernfitException e) {
        future.completeExceptionally(e);
      }
    };
  }

  @Test
  public void clientHttpSyncDiscreteBodyLaunchModernfitExceptionTest() throws Exception {

    ModernfitException e =
        assertThrows(ModernfitException.class, exceptionClientHttpRepository::getSyncDiscreteBody);

    assertThat(e).hasMessageThat().contains("Launch for test from sync DiscreteBody");
  }
  
  @Test
  public void clientHttpAsyncDiscreteBodyLaunchModernfitExceptionTest() throws Exception {

    CompletableFuture<Void> future = new CompletableFuture<>();
    
    ModernfitException e =
        assertThrows(ModernfitException.class, () -> exceptionClientHttpRepository.getAsyncDiscreteBody(getFutureCallback(future)));

    assertThat(e).hasMessageThat().contains("Launch for test from async DiscreteBody");
  }

  @Test
  public void clientHttpSyncMultipartLaunchModernfitExceptionTest() throws Exception {

    ModernfitException e =
        assertThrows(ModernfitException.class, exceptionClientHttpRepository::getSyncMultipart);

    assertThat(e).hasMessageThat().contains("Launch for test from sync Multipart");
  }

  @Test
  public void clientHttpAsyncMultipartLaunchModernfitExceptionTest() throws Exception {

    CompletableFuture<Void> future = new CompletableFuture<>();

    ModernfitException e =
        assertThrows(
            ModernfitException.class,
            () -> exceptionClientHttpRepository.getAsyncMultipart(getFutureCallback(future)));

    assertThat(e).hasMessageThat().contains("Launch for test from async Multipart");
  }

  @Test
  public void converterLaunchNullPointerExceptionTest() throws Exception {

    assertThrows(NullPointerException.class, exceptionConverterRepository::getEchoLong);
  }

  public static class ExceptionConverterFactory implements Converter.Factory {

    public static ExceptionConverterFactory create() {
      return new ExceptionConverterFactory();
    }

    @Override
    public <T> Converter<T, BodyContent> getRequestConverter(T zombie, CustomType<T> customType) {
      return null;
    }

    @Override
    public <T> Converter<ResponseContent, T> getResponseConverter(
        T zombie, CustomType<T> customType) {

      return null;
    }

    @Override
    public <T> Converter<T, String> getUrlConverter(T zombie, CustomType<T> customType) {
      return null;
    }
  }

  public static class ExceptionClientHttp implements HttpClient {

    public static ExceptionClientHttp create() {
      return new ExceptionClientHttp();
    }

    @Override
    public ResponseContent callMethod(RequestInfo requestInfo, DiscreteBody body)
        throws ModernfitException {

      throw new ModernfitException("Launch for test from sync DiscreteBody");
    }

    @Override
    public <T> void callMethod(
        RequestInfo requestInfo, DiscreteBody body, ResponseCallback<T> callback) {

      throw new ModernfitException("Launch for test from async DiscreteBody");
    }

    @Override
    public ResponseContent callMethod(RequestInfo requestInfo, MultipartBody body)
        throws ModernfitException {

      throw new ModernfitException("Launch for test from sync Multipart");
    }

    @Override
    public <T> void callMethod(
        RequestInfo requestInfo, MultipartBody body, ResponseCallback<T> callback) {

      throw new ModernfitException("Launch for test from async Multipart");
    }
  }

  @Modernfit(
      value = "http://localhost:8080/api",
      client = ExceptionClientHttp.class,
      converterFactory = JacksonConverterFactory.class)
  public interface ExceptionClientHttpRepository {

    @GET("/echo/scalar/long")
    Long getSyncDiscreteBody();

    @GET("/echo/scalar/long")
    void getAsyncDiscreteBody(ResponseCallback<Void> responseCallback);

    @GET("/echo/scalar/long")
    @Multipart
    Long getSyncMultipart();

    @GET("/echo/scalar/long")
    @Multipart
    void getAsyncMultipart(ResponseCallback<Void> responseCallback);
  }

  @Modernfit(
      value = "http://localhost:8080/api",
      converterFactory = ExceptionConverterFactory.class)
  public interface ExceptionConverterRepository {

    @GET("/echo/scalar/long")
    Long getEchoLong();
  }
}
