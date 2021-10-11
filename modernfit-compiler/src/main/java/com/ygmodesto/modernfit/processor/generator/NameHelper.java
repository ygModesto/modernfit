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

package com.ygmodesto.modernfit.processor.generator;

import java.util.List;
import java.util.Random;

/**
 * Utility class for names. 
 */
public class NameHelper {

  private static NameHelper INSTANCE;

  // From A to Z
  private static final int LEFT_LIMIT = 65;
  private static final int WINDOW_LIMIT = 26;
  private static final int MARGIN_RANDOM_LENGTH = 2;

  private static Random random;


  /**
   * Create a string without conflicts with a list of strings from a base string.
   * If the prefix string is equal to any name in the conflicts list, add characters 
   * to it until it conflicts with all the strings.
   *
   * @param prefix the base string.
   * @param conflicts the strings with which there can be no conflicts.
   * @return a string without conflicts.
   */
  public String generateFreeName(String prefix, List<String> conflicts) {

    if ((conflicts == null) || (conflicts.isEmpty())) {
      return prefix;
    }

    int sufixLength = (conflicts.size() / WINDOW_LIMIT) + MARGIN_RANDOM_LENGTH;
    boolean conflict = false;
    String generatedString = prefix;

    do {
      conflict = false;
      for (String s : conflicts) {
        if (s.equals(generatedString)) {
          conflict = true;
          break;
        }
      }
      if (conflict) {
        StringBuilder buffer = new StringBuilder(prefix);
        for (int i = 0; i < sufixLength; i++) {
          int randomLimitedInt = LEFT_LIMIT + random.nextInt(WINDOW_LIMIT);
          buffer.append((char) randomLimitedInt);
        }
        generatedString = buffer.toString();
      }
    } while (conflict);

    return generatedString;
  }

  private NameHelper() {}

  /**
   * Return the unique instance of NameHelper. 
   */
  public static synchronized NameHelper getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new NameHelper();
    }

    return INSTANCE;
  }
}
