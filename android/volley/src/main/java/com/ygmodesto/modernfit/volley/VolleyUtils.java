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

import com.android.volley.Header;
import com.android.volley.NetworkResponse;
import com.ygmodesto.modernfit.services.ModernfitException;
import com.ygmodesto.modernfit.services.ResponseContent;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class.
 */
public class VolleyUtils {

    static final String HEADER_CONTENT_TYPE = "Content-Type";

    public static Map<String, List<String>> toMultimap(List<Header> headers){

        if (headers == null) {
            return Collections.emptyMap();
        }

        Map<String, List<String>> values = new HashMap<String, List<String>>();
        for (Header header : headers){
            List<String> aux = values.get(header.getName());
            if (aux == null){
                aux = new ArrayList<String>(2);
                values.put(header.getName(), aux);
            }
            aux.add(header.getValue());
        }

        return values;
    }

    /**
     * Convert NetworkResponse to ResponseContent.
     */
    public static ResponseContent toResponseContent(NetworkResponse networkResponse){

        if (networkResponse == null) {
            throw new ModernfitException("networkResponse == null");
        }

        String contentType = networkResponse.headers.get(HEADER_CONTENT_TYPE);
        String type = null;
        Charset charset = null;
        if (contentType != null) {
            String[] params = contentType.split(";", 0);
            type = params[0].trim();
            for (int i = 1; i < params.length; i++) {
                String[] pair = params[i].trim().split("=", 0);
                if (pair.length == 2) {
                    if (pair[0].equals("charset")) {
                        charset = Charset.forName(pair[1]);
                    }
                }
            }
        }
        return new ResponseContent(networkResponse.statusCode,
                networkResponse.headers, type, charset, networkResponse.data);
    }


}
