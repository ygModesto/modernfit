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

import com.ygmodesto.modernfit.processor.repository.HeaderEchoResponseRepository;
import com.ygmodesto.modernfit.processor.server.EchoResponse;
import java.util.HashMap;
import java.util.Map;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class HeaderEchoResponseServerTest extends AbstractFunctionalTest {

  private static HeaderEchoResponseRepository headerEchoResponseRepository;

  @BeforeClass
  public static void setUp() throws Exception {
    headerEchoResponseRepository = util(HeaderEchoResponseRepository.class, "Impl");
  }

  @Test
  public void headerStringValueFromAnnotation() {

    String key = "deviceUUID".toLowerCase();
    String deviceUUID = "abcd-1234";
    EchoResponse echoResponse =
        headerEchoResponseRepository.headerStringValueFromAnnotation(deviceUUID);

    Map<String, String> headers = echoResponse.getHeaders();

    Map<String, String> expectedHeaders = new HashMap<String, String>();
    expectedHeaders.put(key, deviceUUID);
    assertThat(headers).containsAtLeastEntriesIn(expectedHeaders);
  }

  @Test
  public void headerValueFromAnnotation() {

    String key = "deviceUUID".toLowerCase();
    Long deviceUUID = 98764L;
    EchoResponse echoResponse = headerEchoResponseRepository.headerValueFromAnnotation(deviceUUID);

    Map<String, String> headers = echoResponse.getHeaders();

    Map<String, String> expectedHeaders = new HashMap<String, String>();
    expectedHeaders.put(key, String.valueOf(deviceUUID));
    assertThat(headers).containsAtLeastEntriesIn(expectedHeaders);
  }

  @Test
  public void headerValueNameOfParameter() {

    String key = "deviceUUID".toLowerCase();
    Long deviceUUID = 98764L;
    EchoResponse echoResponse = headerEchoResponseRepository.headerValueNameOfParameter(deviceUUID);

    Map<String, String> headers = echoResponse.getHeaders();

    Map<String, String> expectedHeaders = new HashMap<String, String>();
    expectedHeaders.put(key, String.valueOf(deviceUUID));
    assertThat(headers).containsAtLeastEntriesIn(expectedHeaders);
  }

  // TODO que pasa con los list de headers?

  @Test
  public void headerMapStringKeyStringValue() {

    Map<String, String> headersMap = new HashMap<String, String>();
    headersMap.put("deviceuuid", "abcd-1234");
    headersMap.put("country", "es");
    EchoResponse echoResponse =
        headerEchoResponseRepository.headerMapStringKeyStringValue(headersMap);

    Map<String, String> headers = echoResponse.getHeaders();

    assertThat(headers).containsAtLeastEntriesIn(headersMap);
  }

  @Test
  public void headerMapObjectKeyObjectValue() {

    Map<Object, Object> headersMap = new HashMap<Object, Object>();
    headersMap.put("deviceuuid", 123459L);
    headersMap.put("country", "es");
    EchoResponse echoResponse =
        headerEchoResponseRepository.headerMapObjectKeyObjectValue(headersMap);
    Map<String, String> headersExpected = new HashMap<String, String>();
    for (Map.Entry<Object, Object> entry : headersMap.entrySet()) {
      headersExpected.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
    }

    Map<String, String> headers = echoResponse.getHeaders();

    assertThat(headers).containsAtLeastEntriesIn(headersExpected);
  }

  @Test
  public void headerAsMethodAnnotation() {

    Map<String, String> headersExpected = new HashMap<String, String>();
    headersExpected.put("deviceuuid", "abcd-1234");
    headersExpected.put("country", "es");

    EchoResponse echoResponse = headerEchoResponseRepository.headersAsMethodAnnotation();

    Map<String, String> headers = echoResponse.getHeaders();

    assertThat(headers).containsAtLeastEntriesIn(headersExpected);
  }

  @Test
  public void headersInAbstractInterfaceImpl() {

    Map<String, String> headersExpected = new HashMap<String, String>();
    headersExpected.put("deviceuuid", "abcd-1234");
    headersExpected.put("country", "es");

    headerEchoResponseRepository.setHeaders(headersExpected);
    EchoResponse echoResponse = headerEchoResponseRepository.headersInAbstractInterfaceImpl();

    Map<String, String> headers = echoResponse.getHeaders();

    assertThat(headers).containsAtLeastEntriesIn(headersExpected);
    headerEchoResponseRepository.setHeaders(null);
  }

  @Test
  public void headersAnyway() {

    String country = "es";
    Long deviceuuid = 1234L;

    Map<String, String> headerMap = new HashMap<String, String>();
    headerMap.put("header3", "value3");
    headerMap.put("header4", "value4");

    Map<Object, Object> headerMapAsObject = new HashMap<Object, Object>();
    headerMapAsObject.put("header5", 5L);
    headerMapAsObject.put("header6", 6L);

    Map<String, String> headerMapInAbstractInterfaceImpl = new HashMap<String, String>();
    headerMap.put("header6", "value6");
    headerMap.put("header7", "value7");

    headerEchoResponseRepository.setHeaders(headerMapInAbstractInterfaceImpl);
    EchoResponse echoResponse =
        headerEchoResponseRepository.headersAnyway(
            country, deviceuuid, headerMap, headerMapAsObject);

    Map<String, String> headers = echoResponse.getHeaders();

    Map<String, String> headersExpected = new HashMap<String, String>(headerMap);
    for (Map.Entry<Object, Object> entry : headerMapAsObject.entrySet()) {
      headersExpected.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
    }
    headersExpected.putAll(headerMapInAbstractInterfaceImpl);
    headersExpected.put("country", country);
    headersExpected.put("deviceuuid", String.valueOf(deviceuuid));

    assertThat(headers).containsAtLeastEntriesIn(headersExpected);
    headerEchoResponseRepository.setHeaders(null);
  }
}
