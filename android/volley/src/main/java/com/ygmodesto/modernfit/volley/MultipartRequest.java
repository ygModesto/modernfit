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
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.ygmodesto.modernfit.services.MultipartBody;
import com.ygmodesto.modernfit.services.ResponseContent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Class for all Multipart network requests.
 */
public class MultipartRequest extends ResponseContentRequest {

    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";

    private final String boundary = "--------------------" + UUID.randomUUID().toString();

    private MultipartBody mMultipartBody;

    /**
     * Creates a new multipart request with the given method.
     *
     * @param method the request {@link Method} to use
     * @param url URL to fetch the string at
     * @param headers headers to send
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public MultipartRequest(int method, String url, Map<String, String> headers,
                            Response.Listener<ResponseContent> listener,
                            @Nullable Response.ErrorListener errorListener) {
        super(method, url, headers, listener, errorListener);
    }

    /**
     * Creates a new multipart request with the given method.
     *
     * @param method the request {@link Method} to use
     * @param url URL to fetch the string at
     * @param headers headers to send
     * @param multipartBody multipart body
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public MultipartRequest(int method, String url, Map<String, String> headers,
                            MultipartBody multipartBody,
                            Response.Listener<ResponseContent> listener,
                            @Nullable Response.ErrorListener errorListener) {
        super(method, url, headers, listener, errorListener);
        this.mMultipartBody = multipartBody;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return (mHeaders != null) ? mHeaders : super.getHeaders();
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(bos)) {

            // populate data byte payload
            List<Part> parts = getByteData();
            if (parts != null && parts.size() > 0) {
                for(Part data : parts) {
                    writePart(dos, data);
                }
            }

            // close multipart form data after text and file data
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Custom method handle data payload.
     *
     * @return List data parts
     * @throws AuthFailureError
     */
    protected List<Part> getByteData() throws AuthFailureError {
        List<Part> volleyParts = new ArrayList<>();

        for(com.ygmodesto.modernfit.services.Part part : mMultipartBody.getParts()) {
            volleyParts.add(Part.create(part.getName(), part.getFileName(), part.getContent(), part.getContentType()));
        }
        return volleyParts;
    }



    private void writePart(DataOutputStream dataOutputStream, Part part) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        String contentDisposition = part.getFileName() == null ?
                "Content-Disposition: form-data; name=\"" + part.getName() + "\"" + lineEnd :
                "Content-Disposition: form-data; name=\"" + part.getName() + "\"; filename=\"" + part.getFileName() + "\"" + lineEnd;
        dataOutputStream.writeBytes(contentDisposition);
        if (part.getContentType() != null && !part.getContentType().trim().isEmpty()) {
            dataOutputStream.writeBytes("Content-Type: " + part.getContentType() + lineEnd);
        }
        dataOutputStream.writeBytes(lineEnd);

        dataOutputStream.write(part.getContent());

        dataOutputStream.writeBytes(lineEnd);
    }



    public static final class Part {
        private String name;
        private String fileName;
        private byte[] content;
        private String contentType;


        public static Part create(String name, String fileName, byte[] data, String contentType){
            return new Part(name, fileName, data, contentType);
        }

        /**
         * Constructor with mime data type.
         *
         * @param name     label of data
         * @param data     byte data
         * @param contentType content-type data like "image/jpeg"
         */
        private Part(String name, String fileName, byte[] data, String contentType) {
            this.name = name;
            this.fileName = fileName;
            this.content = data;
            this.contentType = contentType;
        }


        public String getName() {
            return name;
        }


        /**
         * Getter file name.
         *
         * @return file name
         */
        public String getFileName() {
            return fileName;
        }

        /**
         * Getter content.
         *
         * @return byte file data
         */
        public byte[] getContent() {
            return content;
        }

        /**
         * Getter content type.
         *
         * @return contentType type
         */
        public String getContentType() {
            return contentType;
        }

    }
}
