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

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.ygmodesto.modernfit.services.DiscreteBody;
import com.ygmodesto.modernfit.services.ResponseContent;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for all discrete network requests (not Multipart).
 */
public class DiscreteRequest extends ResponseContentRequest {

    private DiscreteBody mBody;

    /**
     * Creates a new discrete request with the given method.
     *
     * @param method the request {@link Method} to use
     * @param url URL to fetch the string at
     * @param headers headers to send
     * @param body body of the request
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public DiscreteRequest(int method, String url, Map<String, String> headers, DiscreteBody body, Response.Listener<ResponseContent> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, headers, listener, errorListener);
        mBody = body;
    }

    /**
     * Creates a new discrete request with the given method.
     *
     * @param method the request {@link Method} to use
     * @param url URL to fetch the string at
     * @param headers headers to send
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public DiscreteRequest(int method, String url, Map<String, String> headers, Response.Listener<ResponseContent> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, headers, listener, errorListener);
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> defaultHeaders = new HashMap<String, String>(super.getHeaders());
        if (mHeaders != null) { defaultHeaders.putAll(mHeaders); }
        return defaultHeaders;
    }

    @Override
    public String getBodyContentType() {
        return mBody == null ? null : mBody.getContentType();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return mBody == null ? null : mBody.getContent();
    }

}
