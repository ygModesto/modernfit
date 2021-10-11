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

package com.ygmodesto.modernfit.processor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapUtils {

  public static Map<String, List<String>> populateExpectedMap(
      Map<String, List<String>> parametersExpected, Map<? extends Object, ? extends Object> map) {

    for (Map.Entry<? extends Object, ? extends Object> entry : map.entrySet()) {
      List<String> values = new ArrayList<String>(1);
      values.add(String.valueOf(entry.getValue()));
      parametersExpected.put(String.valueOf(entry.getKey()), values);
    }

    return parametersExpected;
  }

  public static Map<String, List<String>> populateExpectedMap(
      Map<String, List<String>> parametersExpected, String key, Object[] array) {

    List<String> userIdsExpected = new ArrayList<String>();
    for (Object object : array) {
      userIdsExpected.add(String.valueOf(object));
    }
    parametersExpected.put(key, userIdsExpected);

    return parametersExpected;
  }

  public static Map<String, List<String>> populateExpectedMap(
      Map<String, List<String>> parametersExpected, String key, List<? extends Object> list) {

    List<String> userIdsExpected = new ArrayList<String>();
    for (Object object : list) {
      userIdsExpected.add(String.valueOf(object));
    }
    parametersExpected.put(key, userIdsExpected);

    return parametersExpected;
  }

  public static Map<String, List<String>> populateExpectedMap(
      Map<String, List<String>> parametersExpected, String key, Object object) {

    List<String> userIdsExpected = new ArrayList<String>();
    userIdsExpected.add(String.valueOf(object));
    parametersExpected.put(key, userIdsExpected);

    return parametersExpected;
  }

  public static String toQueryString(Map<String, List<String>> parameters) {

    StringBuilder queryStringBuilder = new StringBuilder();

    for (Map.Entry<String, List<String>> entry : parameters.entrySet()) {
      for (String value : entry.getValue()) {
        queryStringBuilder.append(entry.getKey() + "=" + value + "&");
      }
    }
    queryStringBuilder.deleteCharAt(queryStringBuilder.length() - 1);

    return queryStringBuilder.toString();
  }

  public static String toQueryStringEncoded(Map<String, List<String>> parameters, Charset charset)
      throws UnsupportedEncodingException {

    StringBuilder queryStringBuilder = new StringBuilder();

    for (Map.Entry<String, List<String>> entry : parameters.entrySet()) {
      for (String value : entry.getValue()) {
        queryStringBuilder.append(
            entry.getKey() + "=" + URLEncoder.encode(value, charset.displayName()) + "&");
      }
    }
    queryStringBuilder.deleteCharAt(queryStringBuilder.length() - 1);

    return queryStringBuilder.toString();
  }
}
