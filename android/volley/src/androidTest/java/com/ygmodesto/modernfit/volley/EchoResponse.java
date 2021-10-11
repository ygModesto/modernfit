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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EchoResponse {


    private String url;
    private String method;
    private String contentType;
    private Map<String, List<String>>  parameters;
    private String queryString;
    private Map<String, List<String>> headers;
    private String body;


    private EchoResponse(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.contentType = builder.contentType;
        this.parameters = builder.parameters;
        this.queryString = builder.queryString;
        this.headers = builder.headers;
        this.body = builder.body;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Map<String, List<String>> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, List<String>> parameters) {
        this.parameters = parameters;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public void setParametersAsArray(Map<String, String[]> parameters) {
        if (parameters == null) { return ; }

        this.parameters = new HashMap<String, List<String>>();
        for(Map.Entry<String, String[]> entry : parameters.entrySet()) {
            this.parameters.put(entry.getKey(), Arrays.asList(entry.getValue()));
        }

    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "EchoResponse{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", contentType='" + contentType + '\'' +
                ", parameters=" + parameters +
                ", queryString='" + queryString + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }

    public static class Builder {

        private String url;
        private String method;
        private String contentType;
        private Map<String, List<String>>  parameters = new HashMap<>();
        private String queryString;
        private Map<String, List<String>> headers = new HashMap<>();
        private String body;

        public Builder addUrl(String url){
            this.url = url;
            return this;
        }

        public Builder addMethod(String method){
            this.method = method;
            return this;
        }

        public Builder addContentType(String contentType){
            this.contentType = contentType;
            return this;
        }

        public Builder addParameter(String parameterKey, String parameterValue){

            List<String> values = this.parameters.get(parameterKey);
            if (values != null){
                this.parameters.get(parameterKey).add(parameterValue);
            }else{
                values = new ArrayList<>();
                values.add(parameterValue);
                this.parameters.put(parameterKey, values);
            }
            return this;
        }

        public Builder addParameters(Map<String, List<String>> parameters){
            this.parameters.putAll(parameters);
            return this;
        }

        public Builder addQueryString(String queryString){
            this.queryString = queryString;
            return this;
        }

        public Builder addHeader(String headerKey, String headerValue){

            List<String> values = this.headers.get(headerKey);
            if (values != null){
                this.headers.get(headerKey).add(headerValue);
            }else{
                values = new ArrayList<>();
                values.add(headerValue);
                this.headers.put(headerKey, values);
            }
            return this;
        }

        public Builder addHeaders(Map<String, List<String>> headers){
            this.headers.putAll(headers);
            return this;
        }

        public Builder addBody(String body){
            this.body = body;
            return this;
        }


        public EchoResponse build(){
            return new EchoResponse(this);
        }
    }
}
