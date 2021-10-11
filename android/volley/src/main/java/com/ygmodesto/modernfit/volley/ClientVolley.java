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


import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.ygmodesto.modernfit.services.ResponseCallback;
import com.ygmodesto.modernfit.services.DiscreteBody;
import com.ygmodesto.modernfit.services.HttpClient;
import com.ygmodesto.modernfit.services.HttpMethod;
import com.ygmodesto.modernfit.services.ModernfitException;
import com.ygmodesto.modernfit.services.MultipartBody;
import com.ygmodesto.modernfit.services.RequestInfo;
import com.ygmodesto.modernfit.services.ResponseContent;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/** Class that implements the {@link HttpClient} interface using
 * from the Volley library through the {@link RequestQueue} object.
 */
public class ClientVolley implements HttpClient {

    private RequestQueue mRequestQueue;

    /**
     * Create an instance using a default {@link RequestQueue} instance for using as HTTP
     * client.
     */
    public static ClientVolley create() {
        return new ClientVolley(Volley.newRequestQueue(AndroidModernfitContext.getContext()));
    }

    /**
     * Create an instance using a {@code RequestQueue} instance for using as HTTP client.
     *
     * @param requestQueue a {@link RequestQueue} instance.
     */
    public static ClientVolley create(RequestQueue requestQueue) {
        return new ClientVolley(requestQueue);
    }

    private ClientVolley(RequestQueue requestQueue){
        this.mRequestQueue = requestQueue;
    }


    public void setQueue(RequestQueue requestQueue) {
        this.mRequestQueue = requestQueue;
    }

    @Override
    public ResponseContent callMethod(RequestInfo requestInfo, final DiscreteBody body) throws ModernfitException {

        RequestFuture<ResponseContent> future = RequestFuture.newFuture();
        DiscreteRequest discreteRequest = createRequest(requestInfo.getHttpMethod(), requestInfo.getUrl(), requestInfo.getHeaders(), body, future, future);

        // If you want to be able to cancel the request:
        //future.setRequest(queue.add(stringRequest));
        mRequestQueue.add(discreteRequest);

        try {
            ResponseContent response = future.get();

            return response;
        } catch (InterruptedException e) {
            throw new ModernfitException(e);
        } catch (ExecutionException e) {

            VolleyError error;
            if (e.getCause() instanceof VolleyError){
                error = VolleyError.class.cast(e.getCause());
            }else {
                throw new ModernfitException(e.getCause());
            }

            return VolleyUtils.toResponseContent(error.networkResponse);
        }

    }


    @Override
    public <T> void callMethod(RequestInfo requestInfo, final DiscreteBody body, final ResponseCallback<T> callback) {

        // Request a string response from the provided URL.
        VolleyListener<T> volleyListener = new VolleyListener<T>(callback);
        //Request.Method and HttpMethod coincide in the ordinal
        DiscreteRequest discreteRequest = createRequest(requestInfo.getHttpMethod(), requestInfo.getUrl(), requestInfo.getHeaders(), body, volleyListener, volleyListener);

        mRequestQueue.add(discreteRequest);

    }

    @Override
    public ResponseContent callMethod(RequestInfo requestInfo, final MultipartBody body) throws ModernfitException {

        RequestFuture<ResponseContent> future = RequestFuture.newFuture();

        MultipartRequest multipartRequest = createVolleyMultipartRequest(requestInfo.getHttpMethod(), requestInfo.getUrl(), requestInfo.getHeaders(), body, future, future);

        // If you want to be able to cancel the request:
        //future.setRequest(queue.add(multipartRequest));
        mRequestQueue.add(multipartRequest);

        try {
            return future.get();
        } catch (InterruptedException e) {
            throw new ModernfitException(e);
        } catch (ExecutionException e) {
            VolleyError error;
            if (e.getCause() instanceof VolleyError){
                error = VolleyError.class.cast(e.getCause());
            }else {
                throw new ModernfitException(e.getCause());
            }

            return VolleyUtils.toResponseContent(error.networkResponse);
        }

    }

    @Override
    public <T> void callMethod(RequestInfo requestInfo, final MultipartBody body, final ResponseCallback<T> callback) {

        VolleyListener<T> volleyListener = new VolleyListener<T>(callback);

        MultipartRequest multipartRequest = createVolleyMultipartRequest(requestInfo.getHttpMethod(), requestInfo.getUrl(), requestInfo.getHeaders(),  body, volleyListener, volleyListener);

        mRequestQueue.add(multipartRequest);
    }


    private DiscreteRequest createRequest(HttpMethod method, String url,
                                          final Map<String, String> headers,
                                          final DiscreteBody body,
                                          Response.Listener<ResponseContent> listener,
                                          @Nullable Response.ErrorListener errorListener){

        return new DiscreteRequest(method.ordinal(), url, headers, body, listener, errorListener);

    }

    private MultipartRequest createVolleyMultipartRequest(HttpMethod method, String url,
                                                          final Map<String, String> headers,
                                                          final MultipartBody body,
                                                          Response.Listener<ResponseContent> listener,
                                                          @Nullable Response.ErrorListener errorListener){

        return new MultipartRequest(method.ordinal(), url, headers, body, listener, errorListener);
    }

}
