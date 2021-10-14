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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ygmodesto.modernfit.processor.repository.MethodEchoResponseRepository;
import com.ygmodesto.modernfit.processor.server.EchoResponse;
import com.ygmodesto.modernfit.processor.server.ModelTO;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MethodEchoResponseServerTest extends AbstractFunctionalTest {

  private static final String MEDIA_TYPE = "application/json;charset=UTF-8";

  private static MethodEchoResponseRepository methodEchoResponseRepository;
  private static ObjectMapper objectMapper;

  @BeforeClass
  public static void setUp() throws Exception {
    methodEchoResponseRepository = util(MethodEchoResponseRepository.class, "Impl");
    objectMapper = new ObjectMapper();
  }

  @Test
  public void getEchoTest() throws Exception {

    EchoResponse echoResponse = methodEchoResponseRepository.getEcho();
    assertThat(echoResponse.getContentType()).isNull();
    assertThat(echoResponse.getMethod()).isEqualTo("GET");
  }

  @Test
  public void headEchoTest() throws Exception {

    methodEchoResponseRepository.headEcho();

    // No exceptions is a success

  }

  @Test
  public void deleteEchoTest() throws Exception {

    EchoResponse echoResponse = methodEchoResponseRepository.deleteEcho();
    assertThat(echoResponse.getMethod()).isEqualTo("DELETE");
  }

  @Test
  public void optionsEchoTest() throws Exception {

    methodEchoResponseRepository.optionsEcho();

    // No exceptions is a success

  }

  @Test
  public void patchEchoTest() throws Exception {

    ModelTO modelTO = new ModelTO(1L, "model", "@model");

    EchoResponse echoResponse = methodEchoResponseRepository.patchEcho(modelTO);
    assertThat(echoResponse.getMethod()).isEqualTo("PATCH");
    assertThat(objectMapper.readValue(echoResponse.getBody(), ModelTO.class)).isEqualTo(modelTO);
  }

  @Test
  public void postEchoTest() throws Exception {

    ModelTO modelTO = new ModelTO(1L, "model", "@model");

    EchoResponse echoResponse = methodEchoResponseRepository.postEcho(modelTO);
    assertThat(echoResponse.getContentType()).isEqualTo(MEDIA_TYPE);
    assertThat(echoResponse.getMethod()).isEqualTo("POST");
    assertThat(objectMapper.readValue(echoResponse.getBody(), ModelTO.class)).isEqualTo(modelTO);
  }

  @Test
  public void putEchoTest() throws Exception {

    ModelTO modelTO = new ModelTO(1L, "model", "@model");

    EchoResponse echoResponse = methodEchoResponseRepository.putEcho(modelTO);
    assertThat(echoResponse.getContentType()).isEqualTo(MEDIA_TYPE);
    assertThat(echoResponse.getMethod()).isEqualTo("PUT");
    assertThat(objectMapper.readValue(echoResponse.getBody(), ModelTO.class)).isEqualTo(modelTO);
  }

  @Test
  public void optionsEchoWithBodyTest() throws Exception {

    ModelTO modelTO = new ModelTO(1L, "model", "@model");

    methodEchoResponseRepository.optionsEchoWithBody(modelTO);
  }

  @Test
  public void postEcho404Test() throws Exception {

    ModelTO modelTO = new ModelTO(1L, "model", "@model");

    EchoResponse echoResponse = methodEchoResponseRepository.post404Echo(modelTO);
    assertThat(echoResponse.getMethod()).isEqualTo("POST");
    assertThat(echoResponse.getContentType()).isEqualTo(MEDIA_TYPE);
    assertThat(echoResponse.getCode()).isEqualTo(404);
    assertThat(objectMapper.readValue(echoResponse.getBody(), ModelTO.class)).isEqualTo(modelTO);
  }

  @Test
  public void postEcho500Test() throws Exception {

    ModelTO modelTO = new ModelTO(1L, "model", "@model");

    EchoResponse echoResponse = methodEchoResponseRepository.post500Echo(modelTO);
    assertThat(echoResponse.getMethod()).isEqualTo("POST");
    assertThat(echoResponse.getContentType()).isEqualTo(MEDIA_TYPE);
    assertThat(echoResponse.getCode()).isEqualTo(500);
    assertThat(objectMapper.readValue(echoResponse.getBody(), ModelTO.class)).isEqualTo(modelTO);
  }
}
