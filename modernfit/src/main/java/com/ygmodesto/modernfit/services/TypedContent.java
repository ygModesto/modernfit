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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Class define and customize all the fields of a part of a multipart request.
 */
public class TypedContent {

  private String name;
  private String contentType;
  private String fileName;
  private byte[] content;

  private TypedContent(String name, String contentType, String fileName, byte[] content) {
    this.name = name;
    this.contentType = contentType;
    this.fileName = fileName;
    this.content = content;
  }

  public String getName() {
    return name;
  }

  public String getContentType() {
    return contentType;
  }

  public String getFileName() {
    return fileName;
  }

  public byte[] getContent() {
    return content;
  }

  public static TypedContent create(
      String name, String contentType, String fileName, byte[] content) {
    return new TypedContent(name, contentType, fileName, content);
  }

  public static TypedContent create(String name, String contentType, File file) throws IOException {
    return new TypedContent(name, contentType, file.getName(), Files.readAllBytes(file.toPath()));
  }

  public static TypedContent create(String contentType, String fileName, byte[] content) {
    return new TypedContent(null, contentType, fileName, content);
  }

  public static TypedContent create(String contentType, File file) throws IOException {
    return new TypedContent(null, contentType, file.getName(), Files.readAllBytes(file.toPath()));
  }
}
