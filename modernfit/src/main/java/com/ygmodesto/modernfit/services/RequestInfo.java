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
 * Saves all the information of a request that is not from the body.
 */
public class RequestInfo {

  public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
  public static final String DEFAULT_CHARSET_VALUE = DEFAULT_CHARSET.displayName();

  private HttpMethod httpMethod;
  private Map<String, String> headers;
  private String url;

  private RequestInfo(Builder builder) {

    this.httpMethod = builder.httpMethod;
    this.headers = builder.headers;

    StringBuilder stringBuilder = new StringBuilder(builder.urlBuilder);
    if ((builder.parameters != null) && (!builder.parameters.isEmpty())) {
      stringBuilder.append("?" + FormUrlEncoder.encode(builder.parameters));
    }

    this.url = stringBuilder.toString();
  }

  public static Builder baseUrl(String url) {
    return new Builder().setBaseUrl(url);
  }

  public HttpMethod getHttpMethod() {
    return httpMethod;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public String getUrl() {
    return url;
  }

  @Override
  public String toString() {
    return "RequestInfo [httpMethod=" + httpMethod + ", headers=" + headers + ", url=" + url + "]";
  }

  /**
   * Builder class for {@link RequestInfo RequestInfo}.
   */
  public static final class Builder {

    private StringBuilder urlBuilder = new StringBuilder();
    private HttpMethod httpMethod;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, List<String>> parameters = new HashMap<>();

    /**
     * Sets url constant starting part of url.
     * 
     */
    public Builder setBaseUrl(String baseUrl) {
      this.urlBuilder.append(baseUrl);

      return this;
    }

    /**
     * Append to url a constant String.
     *
     * @param path the constant String.
     */
    public Builder addUrlPathAsConstant(String path) {
      this.urlBuilder.append(path);

      return this;
    }

    /**
     * Append the value of path converted to String to the URL.
     * Used for parameters annotated with {@link com.ygmodesto.modernfit.annotations.Path @Path}.
     *
     * @param <T> the Type of path
     * @param path the value to append.
     * @param converter used to convert to String.
     */
    public <T> Builder addUrlPath(T path, Converter<T, String> converter) {

      this.urlBuilder.append(converter.convert(path));

      return this;
    }

    /**
     * Append the value of path converted to String to the URL.
     * The result of convert path to String is encoded using 
     * {@link java.net.URLEncoder#encode URLEncoder.encode} with charset UTF-8.
     * Used for parameters annotated with {@link com.ygmodesto.modernfit.annotations.Path @Path}.
     *
     * @param <T> the Type of path
     * @param path the value to append.
     * @param converter used to convert to String.
     */
    public <T> Builder addUrlPathNotEncoded(T path, Converter<T, String> converter) {
      try {
        this.urlBuilder.append(toStringEncoded(path, converter));

        return this;
      } catch (UnsupportedEncodingException e) {
        throw new ModernfitException(e);
      }
    }

    /**
     * Add the HTTP method for this request.
     */
    public Builder addHttpMethod(HttpMethod httpMethod) {
      this.httpMethod = httpMethod;

      return this;
    }

    /**
     * Add a header for this request. Used for parameters annotated with {@link
     * com.ygmodesto.modernfit.annotations.Header @Header}.
     *
     * @param key the key value of the header.
     * @param value the value for this key.
     */
    public Builder addHeader(String key, String value) {
      this.headers.put(key, value);

      return this;
    }

    // TODO esto deber'ia ser convertido con el convert de url.
    /**
     * Add a header to the request. Used for parameters annotated with {@link
     * com.ygmodesto.modernfit.annotations.Header @Header}.
     *
     * @param key of the header.
     * @param value of the header.
     */
    public Builder addHeader(Object key, Object value) {
      this.headers.put(String.valueOf(key), String.valueOf(value));

      return this;
    }

    //TODO esto deber'ia ser convertido con el convert de url.
    /**
     * Add headers to the request.
     * key-value pairs are obtained from Map headerMap.
     */
    public Builder addHeaders(Map<? extends Object, ? extends Object> headerMap) {

      if ((headerMap == null) || (headerMap.isEmpty())) {
        return this;
      }

      for (Map.Entry<? extends Object, ? extends Object> entry : headerMap.entrySet()) {
        this.headers.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
      }

      return this;
    }

    /**
     * Add a query parameter to URL.
     * Used for parameters annotated with {@link com.ygmodesto.modernfit.annotations.Query @Query}.
     *
     * @param <T> Type of value.
     * @param key of query parameter.
     * @param value of query parameter.
     * @param converter used to convert to String. 
     */
    public <T> Builder addParameter(String key, T value, Converter<T, String> converter) {

      List<String> values = parameters.get(key);
      if (values == null) {
        values = new ArrayList<>();
        parameters.put(key, values);
      }
      values.add(converter.convert(value));

      return this;
    }

    /**
     * Add query parameters to URL.
     * Used for parameters annotated with {@link com.ygmodesto.modernfit.annotations.Query @Query}.
     *
     * @param <T> Type of value.
     * @param key of query parameters.
     * @param values of query parameter as Array.
     * @param converter used to convert each value to String.
     */
    public <T> Builder addParameter(String key, T[] values, Converter<T, String> converter) {

      List<String> valuesForKey = parameters.get(key);
      if (valuesForKey == null) {
        valuesForKey = new ArrayList<>();
        parameters.put(key, valuesForKey);
      }
      for (T value : values) {
        valuesForKey.add(converter.convert(value));
      }

      return this;
    }

    /**
     * Add query parameters to URL.
     * Used for parameters annotated with {@link com.ygmodesto.modernfit.annotations.Query @Query}.
     *
     * @param <T> Type of value.
     * @param key of query parameters.
     * @param values of query parameter as Iterable.
     * @param converter used to convert each value to String.
     */
    public <T> Builder addParameter(
        String key, Iterable<T> values, Converter<T, String> converter) {

      List<String> valuesForKey = parameters.get(key);
      if (valuesForKey == null) {
        valuesForKey = new ArrayList<>();
        parameters.put(key, valuesForKey);
      }
      for (T value : values) {
        valuesForKey.add(converter.convert(value));
      }

      return this;
    }

    /**
     * Add a query parameter to URL.
     * The result of convert value to String is encoded using 
     * {@link java.net.URLEncoder#encode URLEncoder.encode} with charset UTF-8.
     * Used for parameters annotated with {@link com.ygmodesto.modernfit.annotations.Query @Query}.
     *
     * @param <T> Type of value.
     * @param key of query parameter.
     * @param value of query parameter.
     * @param converter used to convert to String. 
     */
    public <T> Builder addParameterNotEncoded(
        String key, T value, Converter<T, String> converter) {
      try {
        List<String> values = parameters.get(key);
        if (values == null) {
          values = new ArrayList<>();
          parameters.put(key, values);
        }
        values.add(toStringEncoded(value, converter));

        return this;
      } catch (UnsupportedEncodingException e) {
        throw new ModernfitException(e);
      }
    }

    /**
     * Add query parameters to URL.
     * The result of convert each value to String is encoded using 
     * {@link java.net.URLEncoder#encode URLEncoder.encode} with charset UTF-8.
     * Used for parameters annotated with {@link com.ygmodesto.modernfit.annotations.Query @Query}.
     *
     * @param <T> Type of value.
     * @param key of query parameters.
     * @param values of query parameters as Array.
     * @param converter used to convert each value to String. 
     */
    public <T> Builder addParameterNotEncoded(
        String key, T[] values, Converter<T, String> converter) {
      try {
        List<String> valuesForKey = parameters.get(key);
        if (valuesForKey == null) {
          valuesForKey = new ArrayList<>();
          parameters.put(key, valuesForKey);
        }
        for (T value : values) {
          valuesForKey.add(toStringEncoded(value, converter));
        }

        return this;
      } catch (UnsupportedEncodingException e) {
        throw new ModernfitException(e);
      }
    }

    /**
     * Add query parameters to URL.
     * The result of convert each value to String is encoded using 
     * {@link java.net.URLEncoder#encode URLEncoder.encode} with charset UTF-8.
     * Used for parameters annotated with {@link com.ygmodesto.modernfit.annotations.Query @Query}.
     *
     * @param <T> Type of value.
     * @param key of query parameters.
     * @param values of query parameters as Iterable.
     * @param converter used to convert each value to String. 
     */
    public <T> Builder addParameterNotEncoded(
        String key, Iterable<T> values, Converter<T, String> converter) {
      try {
        List<String> valuesForKey = parameters.get(key);

        if (valuesForKey == null) {
          valuesForKey = new ArrayList<>();
          parameters.put(key, valuesForKey);
        }
        for (T value : values) {
          valuesForKey.add(toStringEncoded(value, converter));
        }

        return this;
      } catch (UnsupportedEncodingException e) {
        throw new ModernfitException(e);
      }
    }

    /**
     * Add query parameters to URL. key-value pairs are obtained from Map map. Used for parameters
     * annotated with {@link com.ygmodesto.modernfit.annotations.QueryMap @QueryMap}.
     *
     * @param <V> Type of values.
     * @param map pairs of keys and values.
     * @param converter used to convert each value to String.
     */
    public <V> Builder addParameterMap(Map<String, V> map, Converter<V, String> converter) {

      if (map == null) {
        return this;
      }

      for (Map.Entry<String, V> entry : map.entrySet()) {
        addParameter(entry.getKey(), entry.getValue(), converter);
      }

      return this;
    }

    /**
     * Add query parameters to URL. key-value pairs are obtained from Map map. The result of convert
     * each value to String is encoded using {@link java.net.URLEncoder#encode URLEncoder.encode}
     * with charset UTF-8. Used for parameters annotated with {@link
     * com.ygmodesto.modernfit.annotations.QueryMap @QueryMap}.
     *
     * @param <V> Type of values.
     * @param map pairs of keys and values.
     * @param converter used to convert each value to String.
     */
    public <V> Builder addParameterMapNotEncoded(
        Map<String, V> map, Converter<V, String> converter) {

      if (map == null) {
        return this;
      }

      for (Map.Entry<String, V> entry : map.entrySet()) {
        addParameterNotEncoded(entry.getKey(), entry.getValue(), converter);
      }

      return this;
    }

    /**
     * Build a RequestInfo object. 
     */
    public RequestInfo build() {

      assert httpMethod != null : "httpMethod == null";
      assert urlBuilder != null : "baseUrl == null";

      return new RequestInfo(this);
    }

    private <T> String toStringEncoded(T value, Converter<T, String> converter)
        throws UnsupportedEncodingException {

      return URLEncoder.encode(converter.convert(value), StandardCharsets.UTF_8.displayName());
    }

  }
}
