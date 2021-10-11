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

import com.fasterxml.jackson.databind.ObjectReader;
import com.ygmodesto.modernfit.services.ResponseContent;
import java.io.IOException;

/**
 * An HTTP response converter that uses Jackson to convert between a 
 * ResponseContent and T data type.
 */
public class JacksonResponseConverter<T> extends BaseResponseConverters
    implements Converter<ResponseContent, T> {

  private ObjectReader objectReader;

  public JacksonResponseConverter(ObjectReader objectReader) {
    this.objectReader = objectReader;
  }

  @Override
  public T convert(ResponseContent src) throws ModernfitConverterException {
    try {
      return objectReader.readValue(src.getContent());
    } catch (IOException e) {
      throw new ModernfitConverterException(e);
    }
  }
}
