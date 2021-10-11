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

package com.ygmodesto.modernfit.converters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import com.ygmodesto.modernfit.services.BodyContent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * An HTTP request converter that uses Gson to convert between a T data type and BodyContet.
 */
public class GsonRequestConverter<T> extends BaseRequestConverters
    implements Converter<T, BodyContent> {

  private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
  private static final String MEDIA_TYPE = "application/json";

  private Gson gson;
  private TypeAdapter<T> typeAdapter;

  public GsonRequestConverter(Gson gson, TypeAdapter<T> typeAdapter) {
    this.gson = gson;
    this.typeAdapter = typeAdapter;
  }

  @Override
  public BodyContent convert(T src) throws ModernfitConverterException {
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(byteArrayOutputStream, DEFAULT_CHARSET);
        JsonWriter jsonWriter = gson.newJsonWriter(writer)) {
      typeAdapter.write(jsonWriter, src);
      jsonWriter.close();

      return new BodyContent(MEDIA_TYPE, DEFAULT_CHARSET, byteArrayOutputStream.toByteArray());
    } catch (IOException e) {
      throw new ModernfitConverterException(e);
    }
  }
}
