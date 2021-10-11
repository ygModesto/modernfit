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
import com.google.gson.stream.JsonReader;
import com.ygmodesto.modernfit.services.ResponseContent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * An HTTP response converter that uses Gson to convert between a ResponseContent and T data type.
 */
public class GsonResponseConverter<T> extends BaseRequestConverters
    implements Converter<ResponseContent, T> {

  private Gson gson;
  private TypeAdapter<T> typeAdapter;

  public GsonResponseConverter(Gson gson, TypeAdapter<T> typeAdapter) {
    this.gson = gson;
    this.typeAdapter = typeAdapter;
  }

  @Override
  public T convert(ResponseContent src) throws ModernfitConverterException {

    try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(src.getContent());
        Reader reader = new InputStreamReader(byteArrayInputStream);
        JsonReader jsonReader = gson.newJsonReader(reader)) {

      return typeAdapter.read(jsonReader);
    } catch (IOException e) {
      throw new ModernfitConverterException(e);
    }
  }
}
