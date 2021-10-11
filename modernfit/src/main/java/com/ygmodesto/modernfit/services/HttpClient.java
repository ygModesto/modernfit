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

package com.ygmodesto.modernfit.services;

/**
 * Methods to be implemented by any HTTP client that you want to use with Modernfit. 
 *
 */
public interface HttpClient {

  /**
   * Making an HTTP request synchronously.
   *
   * @param requestInfo define the URL, headers and http method.
   * @param body defines the request body of the request.
   * @return the response of the request in an object {@link ResponseContent ResponseContent}.
   * @throws ModernfitException in case an unexpected error occurs.
   */
  public ResponseContent callMethod(RequestInfo requestInfo, DiscreteBody body)
      throws ModernfitException;

  /**
   * Performing an HTTP request asynchronously using {@link ResponseCallback
   * ResponseCallback}.
   *
   * @param <T> the type of the object to expect in the response.
   * @param requestInfo define the URL, headers and http method.
   * @param body defines the request body of the request.
   * @param callback of type {@link ResponseCallback ResponseCallback} to handle the response.
   */
  public <T> void callMethod(
      RequestInfo requestInfo, DiscreteBody body, ResponseCallback<T> callback);

  /**
   * Synchronous execution of a multipart HTTP request.
   *
   * @param requestInfo define the URL, headers and http method.
   * @param body defines the request body of the multipart request.
   * @return the response of the request in an object {@link ResponseContent ResponseContent}.
   * @throws ModernfitException in case an unexpected error occurs.
   */
  public ResponseContent callMethod(RequestInfo requestInfo, MultipartBody body)
      throws ModernfitException;

  /**
   * Performing a multipart HTTP request asynchronously using
   * {@link ResponseCallback ResponseCallback}.
   *
   * @param <T> the type of the object to expect in the response.
   * @param requestInfo define the URL, headers and http method.
   * @param body defines the request body of the multipart request.
   * @param callback of type {@link ResponseCallback ResponseCallback} to handle the response.
   */
  public <T> void callMethod(
      RequestInfo requestInfo, MultipartBody body, ResponseCallback<T> callback);
}
