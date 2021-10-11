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

import com.ygmodesto.modernfit.converters.Converter;

/**
 * Represents a discrete body (not Multipart) of an HTTP request for a data type T.
 */
public class OneObjectDiscreteBody<T> implements DiscreteBody {

  private Converter<T, BodyContent> converter;
  private String contentType;
  private byte[] content;

  private OneObjectDiscreteBody(Builder<T> builder) {

    this.converter = builder.converter;
    BodyContent bodyContent = this.converter.convert(builder.body);
    this.contentType =
        builder.contentType == null ? bodyContent.getContentType() : builder.contentType;
    this.content = bodyContent.getContent();
  }

  @Override
  public byte[] getContent() {
    return content;
  }

  @Override
  public String getContentType() {
    return contentType;
  }

  public static <T> Builder<T> builder() {
    return new Builder<T>();
  }

  /**
   * Builder class for {@link OneObjectDiscreteBody OneObjectDiscreteBody}.
   */
  public static final class Builder<T> {

    private Converter<T, BodyContent> converter;
    private String contentType;
    private T body;

    /**
     * Add a converter for the body object.
     */
    public Builder<T> addConverter(Converter<T, BodyContent> converter) {
      this.converter = converter;
      return this;
    }

    /**
     * Add the body of the request.
     *
     * @param body the body as type T.
     */
    public Builder<T> addBody(T body) {

      this.body = body;
      return this;
    }

    /**
     * Build a OneObjectDiscreteBody object.
     *
     * @return an OneObjectDiscreteBody object.
     */
    public OneObjectDiscreteBody<T> build() {
      if (converter == null) {
        throw new NullPointerException("converter == null");
      }

      return new OneObjectDiscreteBody<>(this);
    }
  }
}
