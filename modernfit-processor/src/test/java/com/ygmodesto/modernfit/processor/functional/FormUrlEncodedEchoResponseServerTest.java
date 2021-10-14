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
import com.ygmodesto.modernfit.processor.repository.FormUrlEncodedEchoResponseRepository;
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
public class FormUrlEncodedEchoResponseServerTest extends AbstractFunctionalTest {

  public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
  private static final String MEDIA_TYPE = "application/x-www-form-urlencoded";

  private static FormUrlEncodedEchoResponseRepository formUrlEncodedEchoResponseRepository;

  @BeforeClass
  public static void setUp() throws Exception {
    formUrlEncodedEchoResponseRepository = util(FormUrlEncodedEchoResponseRepository.class, "Impl");
  }

  @Test
  public void fieldValueNameOfParameter() throws UnsupportedEncodingException {

    Long userId = 2L;
    EchoResponse echoResponse =
        formUrlEncodedEchoResponseRepository.fieldValueNameOfParameter(userId);
    Map<String, List<String>> parametersExpected =
        MapUtils.populateExpectedMap(new HashMap<String, List<String>>(), "userId", userId);
    Map<String, List<String>> parameters = echoResponse.getParameters();
    assertThat(echoResponse.getContentType()).isEqualTo(MEDIA_TYPE);
    assertThat(parameters).isEqualTo(parametersExpected);
    assertThat(echoResponse.getBody())
        .isEqualTo(MapUtils.toQueryStringEncoded(parametersExpected, DEFAULT_CHARSET));
  }

  @Test
  public void fieldValueFromAnnotationEncoded() {

    String notes = "hello hola";
    EchoResponse echoResponse =
        formUrlEncodedEchoResponseRepository.fieldValueNameOfParameterEncoded(notes);

    Map<String, List<String>> parametersExpected =
        MapUtils.populateExpectedMap(new HashMap<String, List<String>>(), "notes", notes);
    Map<String, List<String>> parameters = echoResponse.getParameters();
    //		assertThat(echoResponse.getContentType()).isEqualTo(MEDIA_TYPE);
    assertThat(parameters).isEqualTo(parametersExpected);
    //
    //	assertThat(echoResponse.getBody()).isNotEqualTo(MapUtils.toQueryStringEncoded(parametersExpected, DEFAULT_CHARSET));
  }

  @Test
  public void fieldValueFromAnnotationNotEncoded() throws UnsupportedEncodingException {

    String notes = "hello hola";
    EchoResponse echoResponse =
        formUrlEncodedEchoResponseRepository.fieldValueNameOfParameterNotEncoded(notes);

    Map<String, List<String>> parametersExpected =
        MapUtils.populateExpectedMap(new HashMap<String, List<String>>(), "notes", notes);
    Map<String, List<String>> parameters = echoResponse.getParameters();
    //		assertThat(echoResponse.getContentType()).isEqualTo(MEDIA_TYPE);
    assertThat(parameters).isEqualTo(parametersExpected);
    assertThat(echoResponse.getBody())
        .isEqualTo(MapUtils.toQueryStringEncoded(parametersExpected, DEFAULT_CHARSET));
  }

  @Test
  public void fieldListValueNameOfParameter() throws UnsupportedEncodingException {

    List<Long> userIds = new ArrayList<Long>();
    userIds.add(1L);
    userIds.add(2L);
    EchoResponse echoResponse = formUrlEncodedEchoResponseRepository.fieldList(userIds);
    Map<String, List<String>> parametersExpected =
        MapUtils.populateExpectedMap(new HashMap<String, List<String>>(), "userIds", userIds);
    Map<String, List<String>> parameters = echoResponse.getParameters();
    //		assertThat(echoResponse.getContentType()).isEqualTo(MEDIA_TYPE);
    assertThat(parameters).isEqualTo(parametersExpected);
    assertThat(echoResponse.getBody())
        .isEqualTo(MapUtils.toQueryStringEncoded(parametersExpected, DEFAULT_CHARSET));
  }

  @Test
  public void fieldArrayValueNameOfParameter() throws UnsupportedEncodingException {

    Long userIds[] = new Long[] {1L, 2L};
    EchoResponse echoResponse = formUrlEncodedEchoResponseRepository.fieldArray(userIds);
    Map<String, List<String>> parametersExpected =
        MapUtils.populateExpectedMap(new HashMap<String, List<String>>(), "userIds", userIds);
    Map<String, List<String>> parameters = echoResponse.getParameters();
    //		assertThat(echoResponse.getContentType()).isEqualTo(MEDIA_TYPE);
    assertThat(parameters).isEqualTo(parametersExpected);
    assertThat(echoResponse.getBody())
        .isEqualTo(MapUtils.toQueryStringEncoded(parametersExpected, DEFAULT_CHARSET));
  }

  @Test
  public void fieldMapStrings() throws UnsupportedEncodingException {

    String keyA = "userId";
    String keyB = "address";
    Map<String, String> queryMap = new HashMap<String, String>();
    queryMap.put(keyA, "2");
    queryMap.put(keyB, "Ames, ES");
    EchoResponse echoResponse =
        formUrlEncodedEchoResponseRepository.fieldMapStringKeyStringValueNotEncoded(queryMap);

    Map<String, List<String>> parametersExpected =
        MapUtils.populateExpectedMap(new HashMap<String, List<String>>(), queryMap);
    Map<String, List<String>> parameters = echoResponse.getParameters();
    //		assertThat(echoResponse.getContentType()).isEqualTo(MEDIA_TYPE);
    assertThat(parameters).isEqualTo(parametersExpected);
    assertThat(echoResponse.getBody())
        .isEqualTo(MapUtils.toQueryStringEncoded(parametersExpected, DEFAULT_CHARSET));
  }

  @Test
  public void fieldMapLongs() throws UnsupportedEncodingException {

    String keyA = "userId";
    String keyB = "address";
    Map<String, Long> queryMap = new HashMap<String, Long>();
    queryMap.put(keyA, 81L);
    queryMap.put(keyB, 91L);
    EchoResponse echoResponse =
        formUrlEncodedEchoResponseRepository.fieldMapStringKeyLongValue(queryMap);

    Map<String, List<String>> parametersExpected =
        MapUtils.populateExpectedMap(new HashMap<String, List<String>>(), queryMap);
    Map<String, List<String>> parameters = echoResponse.getParameters();
    //		assertThat(echoResponse.getContentType()).isEqualTo(MEDIA_TYPE);
    assertThat(parameters).isEqualTo(parametersExpected);
    assertThat(echoResponse.getBody())
        .isEqualTo(MapUtils.toQueryStringEncoded(parametersExpected, DEFAULT_CHARSET));
  }

  @Test
  public void fieldsAndQueryMap() throws UnsupportedEncodingException {

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
        formUrlEncodedEchoResponseRepository.fieldsAndQueryMap(userId, userIds, queryMap);

    Map<String, List<String>> parametersExpected =
        MapUtils.populateExpectedMap(new HashMap<String, List<String>>(), "userId", userId);
    parametersExpected = MapUtils.populateExpectedMap(parametersExpected, "userIds", userIds);
    parametersExpected = MapUtils.populateExpectedMap(parametersExpected, queryMap);

    Map<String, List<String>> parameters = echoResponse.getParameters();
    //		assertThat(echoResponse.getContentType()).isEqualTo(MEDIA_TYPE);
    assertThat(parameters).isEqualTo(parametersExpected);
    assertThat(echoResponse.getBody())
        .isEqualTo(MapUtils.toQueryStringEncoded(parametersExpected, DEFAULT_CHARSET));
  }
}
