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

import java.util.List;
import java.util.Map;

/**
 * Utility class for application/x-www-form-urlencoded requests. 
 */
public class FormUrlEncoder {

  private FormUrlEncoder() {}

  /**
   * Given a Map&#60;String, List&#60;String&#62;&#62; is converted to a String 
   * that conforms to the type application/x-www-form-urlencoded.
   * 
   * <p>Example of use:
   * <pre><code>
   * Map&#60;String, List&#60;String&#62;&#62; source = new HashMap&#60;&#62;();
   * source.put("keyA", Arrays.asList("value1A", "value1B"));
   * source.put("keyB", Arrays.asList("value2A"));
   * 
   * FormUrlEncoder.encode(source);
   * 
   * returns "keyA=value1A&amp;keyA=value2A&amp;keyB=value1B"
   *  
   * </code></pre>
   * 
   *
   * @param source The Map to convert
   * @return A String in application/x-www-form-urlencoded form.
   */
  public static String encode(Map<String, List<String>> source) {

    if (source == null) {
      return null;
    }

    final StringBuilder sb = new StringBuilder();

    for (Map.Entry<String, List<String>> field : source.entrySet()) {
      for (String value : field.getValue()) {
        sb.append(field.getKey() + "=" + value + "&");
      }
    }
    sb.deleteCharAt(sb.length() - 1);

    return sb.toString();
  }
}
