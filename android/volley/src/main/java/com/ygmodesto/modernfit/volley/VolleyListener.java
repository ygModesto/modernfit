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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ygmodesto.modernfit.services.ResponseCallback;
import com.ygmodesto.modernfit.services.ModernfitException;
import com.ygmodesto.modernfit.services.ResponseContent;

/**
 * Encapsulates a parsed response for delivery through a ResponseCallback.
 *
 * @param <T> Parsed type of this response
 */
public class VolleyListener<T> implements Response.Listener<ResponseContent>, Response.ErrorListener {

    private ResponseCallback<T> callback;

    public VolleyListener(ResponseCallback<T> callback){
        this.callback = callback;
    }

    @Override
    public void onResponse(ResponseContent response) {
        callback.notifySuccess(response);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        callback.notifyFailure(new ModernfitException(error));
    }
}
