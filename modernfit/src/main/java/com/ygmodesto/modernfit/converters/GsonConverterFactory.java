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
import com.google.gson.reflect.TypeToken;
import com.ygmodesto.modernfit.services.BodyContent;
import com.ygmodesto.modernfit.services.CustomType;
import com.ygmodesto.modernfit.services.ResponseContent;

/**
 * A {@linkplain Converter.Factory converter} that uses Gson to convert objects to JSON and JSON to
 * objects. It extends from the {@link BaseConverterFactory} class so it will create converters
 * that are not already covered in the extended class.
 */
public class GsonConverterFactory extends BaseConverterFactory implements Converter.Factory {

  private Gson gson;

  /**
   * Create an instance using a default {@link Gson} instance for conversion. Encoding to JSON and
   * decoding from JSON will use UTF-8.
   */
  public static GsonConverterFactory create() {
    return new GsonConverterFactory(new Gson());
  }

  /**
   * Create an instance using {@code gson} for conversion. Encoding to JSON and decoding from JSON
   * will use UTF-8.
   */
  public static GsonConverterFactory create(Gson gson) {
    return new GsonConverterFactory(gson);
  }

  private GsonConverterFactory(Gson gson) {
    this.gson = gson;
  }

  @Override
  public <T> Converter<T, BodyContent> getRequestConverter(T zombie, CustomType<T> customType) {

    TypeAdapter<T> typeAdapter = gson.getAdapter(TypeToken.get(customType.getRawClass()));
    return new GsonRequestConverter<T>(gson, typeAdapter);
  }

  @Override
  public <T> Converter<ResponseContent, T> getResponseConverter(
      T zombie, CustomType<T> customType) {

    TypeAdapter<T> typeAdapter = gson.getAdapter(TypeToken.get(customType.getRawClass()));
    return new GsonResponseConverter<T>(gson, typeAdapter);
  }
}
