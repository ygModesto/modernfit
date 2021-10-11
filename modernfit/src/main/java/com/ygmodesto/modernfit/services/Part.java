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
 * Represents a Part of a Multipart request.
 */
public class Part {

  private String name;
  private String fileName;
  private String contentType;
  private byte[] content;

  public Part() {}

  /**
   * Build a Part from its fields.
   *
   * @param name The name of the field in the multipart form corresponding to this part.
   * @param fileName the fileName of the field in the multipart form corresponding to this part. 
   *        fileName can be null.
   * @param contentType the content-type in the multipart form corresponding to this part.
   * @param content the content in the multipart form corresponding to this part.
   */
  public Part(String name, String fileName, String contentType, byte[] content) {
    this.name = name;
    this.fileName = fileName;
    this.contentType = contentType;
    this.content = content;
  }

  /**
   * Build a Part from a name and {@link OneObjectDiscreteBody OneObjectDiscreteBody} object.
   *
   * @param name The name of the field in the multipart form corresponding to this part.
   * @param oneObjectDiscreteBody contentType and content encapsulated in a 
   *     {@link OneObjectDiscreteBody OneObjectDiscreteBody}.
   */
  public <T> Part(String name, OneObjectDiscreteBody<T> oneObjectDiscreteBody) {
    this.name = name;
    this.contentType = oneObjectDiscreteBody.getContentType();
    this.content = oneObjectDiscreteBody.getContent();
  }

  /**
   * Build a Part from a {@link TypedContent TypedContent} object.
   *
   * @param typedContent all fields encapsulated in a {@link TypedContent TypedContent}.
   */
  public Part(TypedContent typedContent) {
    this.name = typedContent.getName();
    this.fileName = typedContent.getFileName();
    this.contentType = typedContent.getContentType();
    this.content = typedContent.getContent();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public byte[] getContent() {
    return content;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }
}
