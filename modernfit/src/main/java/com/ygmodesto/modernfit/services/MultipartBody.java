/*
 * Copyright 2020 Yago Modesto González Diéguez
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ygmodesto.modernfit.services;

import com.ygmodesto.modernfit.converters.Converter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Multipart type HTTP request body. Composed of several {@link Part Part}.
 */
public class MultipartBody {

  private List<Part> parts;

  private MultipartBody(Builder builder) {

    this.parts = builder.parts;
  }

  public List<Part> getParts() {
    return parts;
  }

  public boolean isEmpty() {
    return this.parts.isEmpty();
  }

  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder class for {@link MultipartBody MultipartBody}.
   */
  public static final class Builder {

    private List<Part> parts = new ArrayList<>();

    /**
     * Add to the builder a part identified by field. In addition, a converter is passed to convert
     * the type T of value to a part of Multipart. Used for parameters annotated with
     * {@link Part @Part}.
     *
     * @param <T> the type of data to send.
     * @param field value with which the part will be identified.
     * @param value to send.
     * @param converter to convert the value.
     */
    public <T> Builder addPart(String field, T value, Converter<T, BodyContent> converter) {

      if (value == null) {
        return this;
      }

      parts.add(new Part(field,
          OneObjectDiscreteBody.<T>builder().addBody(value).addConverter(converter).build()));

      return this;
    }

    /**
     * Add to the builder the parts of type T from the Map parts. In addition, a converter is passed
     * to convert the type T of value to a part of Multipart. Used for parameters annotated with
     * {@link com.ygmodesto.modernfit.annotations.PartMap @PartMap}
     *
     * @param <T> the type of data to send.
     * @param parts The Part to send encapsulated in a Map.
     * @param converter to convert the parts.
     */
    public <T> Builder addPart(Map<String, T> parts, Converter<T, BodyContent> converter) {

      if (parts == null) {
        return this;
      }

      for (Map.Entry<String, T> part : parts.entrySet()) {
        addPart(part.getKey(), part.getValue(), converter);
      }

      return this;
    }

    /**
     * Add to the builder a part of type {@link TypedContent TypedContent} identified by field. The
     * Part is built in the {@link TypedContent TypedContent} data type. Used for parameters
     * annotated with {@link Part @Part} that are of type TypedContet.
     *
     * @param field value with which the part will be identified.
     * @param typedContent part content encoded as TypedContet.
     */
    public Builder addPart(String field, TypedContent typedContent) {

      if (typedContent == null) {
        return this;
      }

      String name = typedContent.getName() == null ? field : typedContent.getName();
      parts.add(new Part(name, typedContent.getFileName(), typedContent.getContentType(),
          typedContent.getContent()));

      return this;
    }

    /**
     * Add to the builder several parts of type {@link TypedContent TypedContent} identified by
     * field. Parts are built in the {@link TypedContent TypedContent} data type and grouped into an
     * Iterable. Used for parameters annotated with {@link Part @Part} that are of type TypedContet.
     *
     * @param field value with which the part will be identified.
     * @param typedContents parts content encoded as TypedContet.
     */
    public Builder addPart(String field, Iterable<? extends TypedContent> typedContents) {

      if (typedContents == null) {
        return this;
      }

      for (TypedContent typedContent : typedContents) {
        String name = typedContent.getName() == null ? field : typedContent.getName();
        parts.add(new Part(name, typedContent.getFileName(), typedContent.getContentType(),
            typedContent.getContent()));
      }

      return this;
    }

    /**
     * Add to the builder several parts of type {@link TypedContent TypedContent} identified by
     * field. Parts are built in the {@link TypedContent TypedContent} data type and grouped in an
     * Array. Used for parameters annotated with {@link Part @Part} that are of type TypedContet.
     *
     * @param field value with which the part will be identified.
     * @param typedContents parts content encoded as TypedContet.
     */
    public Builder addPart(String field, TypedContent[] typedContents) {

      if (typedContents == null) {
        return this;
      }

      for (TypedContent typedContent : typedContents) {
        String name = typedContent.getName() == null ? field : typedContent.getName();
        parts.add(new Part(name, typedContent.getFileName(), typedContent.getContentType(),
            typedContent.getContent()));
      }

      return this;
    }

    /**
     * Add to the builder the parts of type {@link TypedContent TypedContent} from the Map parts.
     * Parts are built in the {@link TypedContent TypedContent} data type and grouped in a Map. Used
     * for parameters annotated with {@link com.ygmodesto.modernfit.annotations.PartMap @PartMap}
     *
     * @param parts The Part to send encapsulated in a Map.
     */
    public Builder addPart(Map<String, TypedContent> parts) {

      if (parts == null) {
        return this;
      }

      for (Map.Entry<String, TypedContent> part : parts.entrySet()) {
        addPart(part.getKey(), part.getValue());
      }

      return this;
    }

    public MultipartBody build() {
      return new MultipartBody(this);
    }
  }
}
