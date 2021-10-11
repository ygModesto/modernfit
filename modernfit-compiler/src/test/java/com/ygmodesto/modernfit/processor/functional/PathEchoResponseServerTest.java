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

import com.ygmodesto.modernfit.processor.repository.PathEchoResponseRepository;
import com.ygmodesto.modernfit.processor.server.EchoResponse;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PathEchoResponseServerTest extends AbstractFunctionalTest {

  private static PathEchoResponseRepository pathEchoResponseRepository;

  @BeforeClass
  public static void setUp() throws Exception {
    pathEchoResponseRepository = util(PathEchoResponseRepository.class, "Impl");
  }

  @Test
  public void pathValueNameFromAnnotation() {

    Long userId = 1L;

    EchoResponse echoResponse = pathEchoResponseRepository.pathValueFromAnnotation(userId);
    assertThat(echoResponse.getUrl()).isEqualTo("/api/echo/1");
  }

  @Test
  public void pathValueFromParameterEncoded() {

    String name = "Blas+de+Lezo";

    EchoResponse echoResponse = pathEchoResponseRepository.pathValueFromParameterEncoded(name);
    assertThat(echoResponse.getUrl()).isEqualTo("/api/echo/Blas+de+Lezo");
  }

  @Test
  public void pathValueFromParameterNotEncoded() {

    String name = "Blas+de+Lezo";

    EchoResponse echoResponse = pathEchoResponseRepository.pathValueFromParameterNotEncoded(name);
    assertThat(echoResponse.getUrl()).isEqualTo("/api/echo/Blas%2Bde%2BLezo");
  }
}
