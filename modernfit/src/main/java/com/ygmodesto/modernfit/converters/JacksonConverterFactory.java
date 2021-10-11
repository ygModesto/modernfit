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

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ygmodesto.modernfit.services.BodyContent;
import com.ygmodesto.modernfit.services.CustomType;
import com.ygmodesto.modernfit.services.ResponseContent;

/**
 * A {@linkplain Converter.Factory converter} that uses ObjectMapper to convert objects to JSON and
 * JSON to objects. It extends from the {@link BaseConverterFactory} class so it will create
 * converters that are not already covered in the extended class.
 */
public class JacksonConverterFactory extends BaseConverterFactory implements Converter.Factory {

  private ObjectMapper objectMapper;

  /**
   * Create an instance using a default {@link ObjectMapper} instance for conversion. Encoding to
   * JSON and decoding from JSON will use UTF-8.
   */
  public static JacksonConverterFactory create() {
    return new JacksonConverterFactory(new ObjectMapper());
  }

  /**
   * Create an instance using {@code objectMapper} for conversion. Encoding to JSON and decoding
   * from JSON will use UTF-8.
   */
  public static JacksonConverterFactory create(ObjectMapper objectMapper) {
    return new JacksonConverterFactory(objectMapper);
  }

  private JacksonConverterFactory(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public <T> Converter<T, BodyContent> getRequestConverter(T zombie, CustomType<T> customType) {

    JavaType javaType = objectMapper.getTypeFactory().constructType(customType.getType());
    ObjectWriter objectWriter = objectMapper.writerFor(javaType);
    return new JacksonRequestConverter<T>(objectWriter);
  }

  @Override
  public <T> Converter<ResponseContent, T> getResponseConverter(
      T zombie, CustomType<T> customType) {

    JavaType javaType = objectMapper.getTypeFactory().constructType(customType.getType());
    ObjectReader objectReader = objectMapper.readerFor(javaType);
    return new JacksonResponseConverter<T>(objectReader);
  }
}
