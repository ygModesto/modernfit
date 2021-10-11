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

import androidx.annotation.GuardedBy;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.ygmodesto.modernfit.services.DiscreteBody;
import com.ygmodesto.modernfit.services.ResponseContent;

import java.util.HashMap;
import java.util.Map;

/**
 * Common properties for all network requests.
 */
public abstract class ResponseContentRequest extends Request<ResponseContent> {


    /** Lock to guard mListener as it is cleared on cancel() and read on delivery. */
    protected final Object mLock = new Object();

    @Nullable
    @GuardedBy("mLock")
    protected Response.Listener<ResponseContent> mListener;

    protected Map<String, String> mHeaders;


    /**
     * Creates a new request with the given method.
     *
     * @param method the request {@link Method} to use
     * @param url URL to fetch the string at
     * @param headers headers to send
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public ResponseContentRequest(int method, String url, Map<String, String> headers, Response.Listener<ResponseContent> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
        mHeaders = headers;
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> defaultHeaders = new HashMap<String, String>(super.getHeaders());
        if (mHeaders != null) { defaultHeaders.putAll(mHeaders); }
        return defaultHeaders;
    }


    @Override
    protected Response<ResponseContent> parseNetworkResponse(NetworkResponse response) {

        return Response.success(VolleyUtils.toResponseContent(response), HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(ResponseContent response) {
        Response.Listener<ResponseContent> listener;
        synchronized (mLock) {
            listener = mListener;
        }
        if (listener != null) {
            listener.onResponse(response);
        }
    }

}
