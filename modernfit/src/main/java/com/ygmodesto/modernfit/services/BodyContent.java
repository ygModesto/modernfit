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

/**
 * Class that represents a Request Body.
 *
 * <p>The information that he maintains of the petition are the following 3.
 *
 * <ul>
 *   <li>{@code mediaType} as a String.
 *   <li>{@code charset} {@link Charset Charset} used in the content.
 *   <li>{@code content} request body represented as byte array.
 * </ul>
 */
public class BodyContent {

  private String mediaType;
  private Charset charset;
  private byte[] content;

  /**
   * Build a new BodyContent.
   *
   * @param mediaType as a String.
   * @param charset {@link Charset Charset} used in the content.
   * @param content request body represented as byte array.
   */
  public BodyContent(String mediaType, Charset charset, byte[] content) {

    this.mediaType = mediaType;
    this.charset = charset;
    this.content = content;
  }

  /**
   * Build a new BodyContent without charset.
   *
   * @param mediaType as a String.
   * @param content request body represented as byte array.
   */
  public BodyContent(String mediaType, byte[] content) {

    this.mediaType = mediaType;
    this.content = content;
  }

  /**
   * Returns the Content saved in the object.
   *
   * @return request body represented as byte array.
   */
  public byte[] getContent() {
    return content;
  }

  /**
   * Returns the mediaType stored in the object.
   *
   * @return mediaType as a String.
   */
  public String getMediaType() {
    return mediaType;
  }

  /**
   * Returns the Charset saved in the object.
   *
   * @return {@link Charset Charset} used in the content.
   */
  public Charset getCharset() {
    return charset;
  }

  /**
   * Returns the value of the Content-Type header.
   *
   * <pre><code>
   *
   * BodyContent bodyContentA =
   *   new BodyContent("application/json", StandardCharsets.UTF_8, content);
   *
   * bodyContentA.getContentType() returns "application/json;charset=utf-8"
   * ...
   * BodyContent bodyContentB = new BodyContent("application/json", content);
   *
   * bodyContentB.getContentType() returns "application/json"
   * </code></pre>
   *
   * @return the content type for this object.
   */
  public String getContentType() {
    return charset == null ? mediaType : mediaType + ";charset=" + charset.displayName();
  }
}
