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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Body of an HTTP request of type application/x-www-form-urlencoded.
 */
public class FormUrlEncodedBody implements DiscreteBody {

  public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
  public static final String DEFAULT_CHARSET_VALUE = DEFAULT_CHARSET.displayName();
  private static final String MEDIA_TYPE = "application/x-www-form-urlencoded";

  private Map<String, List<String>> fields;
  private byte[] body;

  private FormUrlEncodedBody(Builder builder) {

    this.fields = builder.fields;

    if (builder.fields.isEmpty()) {
      return;
    }

    body = FormUrlEncoder.encode(builder.fields).getBytes();
  }

  public Map<String, List<String>> getFields() {
    return fields;
  }

  @Override
  public byte[] getContent() {
    return body;
  }

  @Override
  public String getContentType() {
    return MEDIA_TYPE;
  }

  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder class for {@link FormUrlEncodedBody FormUrlEncodedBody}.
   */
  public static final class Builder {

    private Map<String, List<String>> fields = new HashMap<String, List<String>>();

    /**
     * Add to the builder an iterable amount of values for the field,
     * also it is passed the converter for the type T of the values.
     *
     * @param <T> the type of the values to send.
     * @param field form key.
     * @param values the values associated with the field.
     * @param converter to convert from type T to BodyContent.
     */
    public <T> Builder addField(
        String field, Iterable<T> values, Converter<T, BodyContent> converter) {
      try {
        if (values == null) {
          return this;
        }

        // TODO revisar si usar lista de Strings o de byte[]
        List<String> valuesOfField = getOrCreateList(field);

        for (T v : values) {
          valuesOfField.add(new String(converter.convert(v).getContent(), DEFAULT_CHARSET_VALUE));
        }

        return this;
      } catch (UnsupportedEncodingException e) {
        throw new ModernfitException(e);
      }
    }

    /**
     * Add to the builder an array of values for the field,
     * also it is passed the converter for the type T of the values.
     *
     * @param <T> the type of the values to send.
     * @param field form key.
     * @param values the values associated with the field.
     * @param converter to convert from type T to BodyContent.
     */
    public <T> Builder addField(String field, T[] values, Converter<T, BodyContent> converter) {
      try {
        if (values == null) {
          return this;
        }

        List<String> valuesOfField = getOrCreateList(field);

        for (T v : values) {
          valuesOfField.add(new String(converter.convert(v).getContent(), DEFAULT_CHARSET_VALUE));
        }

        return this;
      } catch (UnsupportedEncodingException e) {
        throw new ModernfitException(e);
      }
    }

    /**
     * Add to the builder a value of type T with key field,
     * also it is passed the converter for the type T of the value.
     *
     * @param <T> the type of the values to send.
     * @param field form key.
     * @param value the value associated with the field.
     * @param converter to convert from type T to BodyContent.
     */
    public <T> Builder addField(String field, T value, Converter<T, BodyContent> converter) {
      try {
        List<String> values = getOrCreateList(field);

        values.add(new String(converter.convert(value).getContent(), DEFAULT_CHARSET_VALUE));

        return this;
      } catch (UnsupportedEncodingException e) {
        throw new ModernfitException(e);
      }
    }

    
    /**
     * Add to the builder an iterable amount of values for the field,
     * also it is passed the converter for the type T of the values.
     * The values will be encoded with {@link java.net.URLEncoder # encode URLEncoder.encode}
     * with UTF-8 charset.
     *
     * @param <T> the type of the values to send.
     * @param field form key.
     * @param values the values associated with the field.
     * @param converter to convert from type T to BodyContent.
     */
    public <T> Builder addFieldNotEncoded(
        String field, Iterable<T> values, Converter<T, BodyContent> converter) {
      try {
        if (values == null) {
          return this;
        }

        List<String> valuesOfField = getOrCreateList(field);

        for (T v : values) {
          valuesOfField.add(
              URLEncoder.encode(
                  new String(converter.convert(v).getContent(), DEFAULT_CHARSET_VALUE),
                  DEFAULT_CHARSET_VALUE));
        }

        return this;
      } catch (UnsupportedEncodingException e) {
        throw new ModernfitException(e);
      }
    }

    /**
     * Add to the builder an array of values for the field,
     * also it is passed the converter for the type T of the values.
     * The values will be encoded with {@link java.net.URLEncoder # encode URLEncoder.encode}
     * with UTF-8 charset.
     *
     * @param <T> the type of the values to send.
     * @param field form key.
     * @param values the values associated with the field.
     * @param converter to convert from type T to BodyContent.
     */
    public <T> Builder addFieldNotEncoded(
        String field, T[] values, Converter<T, BodyContent> converter) {
      try {
        if (values == null) {
          return this;
        }

        List<String> valuesOfField = getOrCreateList(field);

        for (T v : values) {
          valuesOfField.add(
              URLEncoder.encode(
                  new String(converter.convert(v).getContent(), DEFAULT_CHARSET_VALUE),
                  DEFAULT_CHARSET_VALUE));
        }

        return this;
      } catch (UnsupportedEncodingException e) {
        throw new ModernfitException(e);
      }
    }

    /**
     * Add to the builder a value with field key,
     * also it is passed the converter for the type T of the value.
     * The values will be encoded with {@link java.net.URLEncoder # encode URLEncoder.encode}
     * with UTF-8 charset.
     *
     * @param <T> the type of the values to send.
     * @param field form key.
     * @param value the value associated with the field.
     * @param converter to convert from type T to BodyContent.
     */
    public <T> Builder addFieldNotEncoded(
        String field, T value, Converter<T, BodyContent> converter) {
      try {
        if (value == null) {
          return this;
        }

        List<String> values = getOrCreateList(field);

        values.add(
            URLEncoder.encode(
                new String(converter.convert(value).getContent(), DEFAULT_CHARSET_VALUE),
                DEFAULT_CHARSET_VALUE));

        return this;
      } catch (UnsupportedEncodingException e) {
        throw new ModernfitException(e);
      }
    }

    /**
     * Add to the builder the values passed in the Map.
     * The map keys will be used as the form field.
     * also it is passed the converter for the type T of the values.
     *
     * @param <T> the type of the values to send.
     * @param fields Map with the fields and values of the form.
     * @param converter to convert from type T to BodyContent.
     */
    public <T> Builder addFieldMap(
        Map<String, T> fields, Converter<T, BodyContent> converter) {

      if (fields == null) {
        return this;
      }

      for (Map.Entry<String, T> field : fields.entrySet()) {
        addField(field.getKey(), field.getValue(), converter);
      }

      return this;
    }

    /**
     * Add to the builder the values passed in the Map.
     * The map keys will be used as the form field.
     * also it is passed the converter for the type T of the values.
     * The values will be encoded with {@link java.net.URLEncoder # encode URLEncoder.encode}
     * with UTF-8 charset.
     *
     * @param <T> the type of the values to send.
     * @param fields Map with the fields and values of the form.
     * @param converter to convert from type T to BodyContent.
     */
    public <T> Builder addFieldMapNotEncoded(
        Map<String, T> fields, Converter<T, BodyContent> converter) {

      try {
        if (fields == null) {
          return this;
        }

        // TODO el field key aqui se hace URLEncoded y en el addFieldNotEncoded simple no se hace --
        // que hay que hacer?
        for (Map.Entry<String, T> field : fields.entrySet()) {
          addFieldNotEncoded(
              URLEncoder.encode(field.getKey(), DEFAULT_CHARSET_VALUE),
              field.getValue(),
              converter);
        }

        return this;
      } catch (UnsupportedEncodingException e) {
        throw new ModernfitException(e);
      }
    }

    private List<String> getOrCreateList(String field) {

      List<String> values = fields.get(field);
      if (values == null) {
        values = new ArrayList<String>();
        this.fields.put(field, values);
      }

      return values;
    }

    public FormUrlEncodedBody build() {
      return new FormUrlEncodedBody(this);
    }
  }
}
