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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ygmodesto.modernfit.services.BodyContent;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * An HTTP request converter that uses Jackson to convert between a T data type and BodyContet.
 */
public class JacksonRequestConverter<T> extends BaseRequestConverters
    implements Converter<T, BodyContent> {

  private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
  private static final String MEDIA_TYPE = "application/json";

  private ObjectWriter objectWriter;

  public JacksonRequestConverter(ObjectWriter objectWriter) {
    this.objectWriter = objectWriter;
  }

  @Override
  public BodyContent convert(T src) throws ModernfitConverterException {
    try {
      return new BodyContent(MEDIA_TYPE, DEFAULT_CHARSET, objectWriter.writeValueAsBytes(src));
    } catch (JsonProcessingException e) {
      throw new ModernfitConverterException(e);
    }
  }
}
