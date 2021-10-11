/*
 * Copyright 2020 Yago Modesto González Diéguez
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ygmodesto.modernfit.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Class that implements the {@link HttpClient HttpClient} interface using from the OkHttp library
 * through the {@link OkHttpClient OkHttpClient} object.
 */
public class ClientOkHttp implements HttpClient {

  private OkHttpClient okHttpClient;

  /**
   * Create an instance using a default {@link OkHttpClient OkHttpClient} instance for using as HTTP
   * client.
   */
  public static ClientOkHttp create() {
    return new ClientOkHttp(new OkHttpClient());
  }

  /**
   * Create an instance using a {@code okHttpClient} instance for using as HTTP client.
   *
   * @param okHttpClient a {@link OkHttpClient OkHttpClient} instance.
   */
  public static ClientOkHttp create(OkHttpClient okHttpClient) {
    return new ClientOkHttp(okHttpClient);
  }

  private ClientOkHttp(OkHttpClient okHttpClient) {
    this.okHttpClient = okHttpClient;
  }

  public void setOkHttpClient(OkHttpClient okHttpClient) {
    this.okHttpClient = okHttpClient;
  }

  // TODO change de Map<String, String> to accept duplicated keys.
  @Override
  public ResponseContent callMethod(RequestInfo requestInfo, DiscreteBody body)
      throws ModernfitException {

    Request request = prepareRequest(requestInfo.getHttpMethod(), requestInfo.getUrl(),
        requestInfo.getHeaders(), body.getContent(), body.getContentType());
    try (Response response = okHttpClient.newCall(request).execute()) {

      return toResponseContent(response);

    } catch (IOException e) {
      throw new ModernfitException(e);
    }
  }

  @Override
  public <T> void callMethod(RequestInfo requestInfo, DiscreteBody body,
      ResponseCallback<T> callback) throws ModernfitException {

    try {

      Request request = prepareRequest(requestInfo.getHttpMethod(), requestInfo.getUrl(),
          requestInfo.getHeaders(), body.getContent(), body.getContentType());

      okHttpClient.newCall(request).enqueue(toCallback(callback));

    } catch (Exception e) {
      throw new ModernfitException(e);
    }
  }

  @Override
  public ResponseContent callMethod(RequestInfo requestInfo, MultipartBody body)
      throws ModernfitException {


    Request request = prepareMultipartRequest(requestInfo.getHttpMethod(), requestInfo.getUrl(),
        requestInfo.getHeaders(), body);

    try (Response response = okHttpClient.newCall(request).execute()) {

      return toResponseContent(response);

    } catch (IOException e) {
      throw new ModernfitException(e);
    }
  }

  @Override
  public <T> void callMethod(RequestInfo requestInfo, MultipartBody body,
      ResponseCallback<T> callback) throws ModernfitException {

    try {

      Request request = prepareMultipartRequest(requestInfo.getHttpMethod(), requestInfo.getUrl(),
          requestInfo.getHeaders(), body);

      okHttpClient.newCall(request).enqueue(toCallback(callback));

    } catch (Exception e) {
      throw new ModernfitException(e);
    }
  }

  private Request prepareRequest(HttpMethod method, String url, Map<String, String> headers,
      byte[] body, String mediaTypeString) {

    MediaType mediaType = MediaType.parse(mediaTypeString);
    RequestBody requestBody = null;
    if (body != null) {
      requestBody = RequestBody.create(mediaType, body);
    }
    Request.Builder requestBuilder =
        new Request.Builder().url(url).method(method.name(), requestBody);

    if (headers != null) {
      requestBuilder.headers(Headers.of(headers));
    }
    return requestBuilder.build();
  }

  private Request prepareMultipartRequest(HttpMethod method, String url,
      Map<String, String> headers, MultipartBody multipartBody) {

    RequestBody requestBody = null;
    // TODO check multipartBody is not empty
    if (!multipartBody.isEmpty()) {

      okhttp3.MultipartBody.Builder multipartBuilder = new okhttp3.MultipartBody.Builder();
      multipartBuilder.setType(okhttp3.MultipartBody.FORM);

      for (Part part : multipartBody.getParts()) {
        multipartBuilder.addFormDataPart(part.getName(), part.getFileName(),
            RequestBody.create(MediaType.parse(part.getContentType()), part.getContent()));
      }

      requestBody = multipartBuilder.build();
    }
    Request.Builder requestBuilder =
        new Request.Builder().url(url).method(method.name(), requestBody);

    if (headers != null) {
      requestBuilder.headers(Headers.of(headers));
    }
    return requestBuilder.build();
  }


  private <T> Callback toCallback(ResponseCallback<T> responseCallback) {

    return new Callback() {

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        try {
          responseCallback.notifySuccess(toResponseContent(response));
        } catch (ModernfitException e) {
          responseCallback.notifyFailure(e);
        }
      }

      @Override
      public void onFailure(Call call, IOException e) {

        responseCallback.notifyFailure(new ModernfitException(e));
      }
    };
  }


  private ResponseContent toResponseContent(Response response) throws IOException {

    ResponseBody responseBody = response.body();
    MediaType mediaType = responseBody.contentType();
    Headers headers = response.headers();
    Set<String> names = headers.names();
    Map<String, String> headersMap = new HashMap<String, String>(names.size());

    for (String name : names) {
      headersMap.put(name, headers.get(name));
    }

    return mediaType == null
        ? new ResponseContent(response.code(), headersMap, null, null, responseBody.bytes())
        : new ResponseContent(response.code(), headersMap, mediaType.toString(),
            mediaType.charset(), responseBody.bytes());
  }
}
