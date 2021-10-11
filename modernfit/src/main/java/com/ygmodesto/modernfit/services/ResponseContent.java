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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Represents an http response.
 */
public class ResponseContent {

  private int code;
  private Map<String, String> headers;
  private String contentType;
  private Charset charset;
  private byte[] content;

  /**
   * Build an object ResponseContent from its fields.
   *
   * @param code HTTP code of the response.
   * @param headers HTTP headers of the response.
   * @param contentType HTTP Content-Type of the response.
   * @param charset of the response.
   * @param content of the response.
   */
  public ResponseContent(
      int code, Map<String, String> headers, String contentType, Charset charset, byte[] content) {

    this.code = code;
    this.headers = headers;
    this.contentType = contentType;
    this.charset = (contentType != null) && (charset == null) ? StandardCharsets.UTF_8 : charset;
    this.content = content;
  }

  public int getCode() {
    return code;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }
  
  public byte[] getContent() {
    return content;
  }

  public String getContentType() {
    return contentType;
  }

  public Charset getCharset() {
    return charset;
  }

  public String getContentAsString() {
    return new String(content, charset);
  }
}
