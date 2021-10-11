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

import com.ygmodesto.modernfit.services.BodyContent;
import com.ygmodesto.modernfit.services.CustomType;
import com.ygmodesto.modernfit.services.ResponseContent;

/**
 * Defines which methods a converter from an object type {@code <S>} to another {@code <T>} should
 * implement.
 *
 * @param <S> The source type.
 * @param <T> The target type.
 */
public interface Converter<S, T> {

  /**
   * Converts a variable of type {@code <S>} to one of type {@code <T>}.
   *
   * @param src the source variable to be converted.
   * @return a value of type {@code <T>}.
   * @throws ModernfitConverterException encapsulates an exception occurred in the process of
   *     conversion.
   */
  T convert(S src) throws ModernfitConverterException;

  /**
   * Define which methods a converter factory should have.
   */
  interface Factory {

    /**
     * Returns a {@link Converter} for converting type {@code <T>} to an HTTP request body, or null
     * if {@code type} cannot be handled by this factory. This is used to create converters for
     * types specified by {@link com.ygmodesto.modernfit.annotations.Body @Body}, {@link
     * com.ygmodesto.modernfit.annotations.Part @Part}, and {@link
     * com.ygmodesto.modernfit.annotations.PartMap @PartMap} values.
     *
     * @param <T> the type of origin you want to convert to an HTTP request body.
     * @param zombie just to do Overloading Method, this variable is not used.
     * @param customType information of the type &#60;T&#62; encapsulated in a {@link CustomType}.
     * @return A {@link Converter} for converting T to BodyContent
     */
    public <T> Converter<T, BodyContent> getRequestConverter(T zombie, CustomType<T> customType);

    /**
     * Returns a {@link Converter} for converting an HTTP response body to type {@code <T>}, or null
     * if {@code type} cannot be handled by this factory.
     *
     * @param <T> el tipo al que se debe convertir la HTTP response.
     * @param zombie solo para hacer Overloading Method, no se hace uso de esta variable.
     * @param customType informacion del tipo &#60;T&#62; encapsulada en un {@link CustomType}
     * @return A {@link Converter} for converting ResponseContent to {@code <T>}
     */
    public <T> Converter<ResponseContent, T> getResponseConverter(
        T zombie, CustomType<T> customType);

    public <T> Converter<T, String> getUrlConverter(T zombie, CustomType<T> customType);
  }
}
