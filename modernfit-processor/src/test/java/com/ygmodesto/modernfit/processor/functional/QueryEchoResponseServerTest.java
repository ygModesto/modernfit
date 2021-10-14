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

package com.ygmodesto.modernfit.processor.functional;

import static com.google.common.truth.Truth.assertThat;

import com.ygmodesto.modernfit.processor.MapUtils;
import com.ygmodesto.modernfit.processor.repository.QueryEchoResponseRepository;
import com.ygmodesto.modernfit.processor.server.EchoResponse;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class QueryEchoResponseServerTest extends AbstractFunctionalTest {

  public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

  private static QueryEchoResponseRepository queryEchoResponseRepository;

  @BeforeClass
  public static void setUp() throws Exception {
    queryEchoResponseRepository = util(QueryEchoResponseRepository.class, "Impl");
  }

  @Test
  public void queryValueFromAnnotation() {

    Long userId = 2L;
    EchoResponse echoResponse = queryEchoResponseRepository.queryValueFromAnnotation(userId);
    Map<String, List<String>> parametersExpected =
        MapUtils.populateExpectedMap(new HashMap<String, List<String>>(), "userId", userId);
    Map<String, List<String>> parameters = echoResponse.getParameters();
    assertThat(parameters).isEqualTo(parametersExpected);
  }

  @Test
  public void queryValueNameOfParameter() {

    Long userId = 2L;
    EchoResponse echoResponse = queryEchoResponseRepository.queryValueNameOfParameter(userId);
    Map<String, List<String>> parametersExpected =
        MapUtils.populateExpectedMap(new HashMap<String, List<String>>(), "userId", userId);
    Map<String, List<String>> parameters = echoResponse.getParameters();
    assertThat(parameters).isEqualTo(parametersExpected);
  }

  @Test
  public void queryValueNameOfParameterEncoded() throws UnsupportedEncodingException {

    String notes = "hello hola";
    EchoResponse echoResponse = queryEchoResponseRepository.queryValueNameOfParameterEncoded(notes);

    Map<String, List<String>> parametersExpected =
        MapUtils.populateExpectedMap(new HashMap<String, List<String>>(), "notes", notes);
    Map<String, List<String>> parameters = echoResponse.getParameters();
    String queryString = echoResponse.getQueryString();
    assertThat(parameters).isEqualTo(parametersExpected);
    assertThat(queryString)
        .isNotEqualTo(MapUtils.toQueryStringEncoded(parametersExpected, DEFAULT_CHARSET));
  }

  @Test
  public void queryValueNameOfParameterNotEncoded() throws UnsupportedEncodingException {

    String notes = "hello hola";
    EchoResponse echoResponse =
        queryEchoResponseRepository.queryValueNameOfParameterNotEncoded(notes);

    Map<String, List<String>> parametersExpected =
        MapUtils.populateExpectedMap(new HashMap<String, List<String>>(), "notes", notes);
    Map<String, List<String>> parameters = echoResponse.getParameters();
    String queryString = echoResponse.getQueryString();
    assertThat(parameters).isEqualTo(parametersExpected);
    assertThat(queryString)
        .isEqualTo(MapUtils.toQueryStringEncoded(parametersExpected, DEFAULT_CHARSET));
  }

  @Test
  public void queryListValueNameOfParameter() {

    List<Long> userIds = new ArrayList<Long>();
    userIds.add(1L);
    userIds.add(2L);
    EchoResponse echoResponse = queryEchoResponseRepository.queryList(userIds);
    Map<String, List<String>> parametersExpected =
        MapUtils.populateExpectedMap(new HashMap<String, List<String>>(), "userIds", userIds);
    Map<String, List<String>> parameters = echoResponse.getParameters();
    assertThat(parameters).isEqualTo(parametersExpected);
  }

  @Test
  public void queryArrayValueNameOfParameter() {

    Long userIds[] = new Long[] {1L, 2L};
    EchoResponse echoResponse = queryEchoResponseRepository.queryArray(userIds);
    Map<String, List<String>> parametersExpected =
        MapUtils.populateExpectedMap(new HashMap<String, List<String>>(), "userIds", userIds);
    Map<String, List<String>> parameters = echoResponse.getParameters();
    assertThat(parameters).isEqualTo(parametersExpected);
  }

  @Test
  public void queryMapStrings() throws UnsupportedEncodingException {

    String keyA = "userId";
    String keyB = "address";
    Map<String, String> queryMap = new HashMap<String, String>();
    queryMap.put(keyA, "2");
    queryMap.put(keyB, "Ames, ES");
    EchoResponse echoResponse =
        queryEchoResponseRepository.queryMapStringKeyStringValueNotEncoded(queryMap);
    String queryString = echoResponse.getQueryString();

    Map<String, List<String>> parametersExpected =
        MapUtils.populateExpectedMap(new HashMap<String, List<String>>(), queryMap);
    Map<String, List<String>> parameters = echoResponse.getParameters();
    assertThat(parameters).isEqualTo(parametersExpected);
    assertThat(queryString)
        .isEqualTo(MapUtils.toQueryStringEncoded(parametersExpected, DEFAULT_CHARSET));
  }

  @Test
  public void queryMapLongs() {

    String keyA = "keyA";
    String keyB = "keyB";
    Map<String, Long> queryMap = new HashMap<String, Long>();
    queryMap.put(keyA, 81L);
    queryMap.put(keyB, 91L);
    EchoResponse echoResponse = queryEchoResponseRepository.queryMapStringKeyLongValue(queryMap);

    Map<String, List<String>> parametersExpected =
        MapUtils.populateExpectedMap(new HashMap<String, List<String>>(), queryMap);
    Map<String, List<String>> parameters = echoResponse.getParameters();
    assertThat(parameters).isEqualTo(parametersExpected);
  }

  @Test
  public void querysAndQueryMap() {

    Long userId = 2L;

    List<Long> userIds = new ArrayList<Long>();
    userIds.add(1L);
    userIds.add(2L);

    String keyA = "portal";
    String keyB = "address";
    Map<String, String> queryMap = new HashMap<String, String>();
    queryMap.put(keyA, "2");
    queryMap.put(keyB, "Ames");
    EchoResponse echoResponse =
        queryEchoResponseRepository.querysAndQueryMap(userId, userIds, queryMap);

    Map<String, List<String>> parametersExpected =
        MapUtils.populateExpectedMap(new HashMap<String, List<String>>(), "userId", userId);
    parametersExpected = MapUtils.populateExpectedMap(parametersExpected, "userIds", userIds);
    parametersExpected = MapUtils.populateExpectedMap(parametersExpected, queryMap);

    Map<String, List<String>> parameters = echoResponse.getParameters();
    assertThat(parameters).isEqualTo(parametersExpected);
  }
}
