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

import com.ygmodesto.modernfit.processor.repository.UrlEchoResponseRepository;
import com.ygmodesto.modernfit.processor.server.EchoResponse;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UrlEchoResponseServerTest extends AbstractFunctionalTest {

  private static UrlEchoResponseRepository urlEchoResponseRepository;

  @BeforeClass
  public static void setUp() throws Exception {
    urlEchoResponseRepository = util(UrlEchoResponseRepository.class, "Impl");
  }

  @Test
  public void urlAsParameter() {

    EchoResponse echoResponse =
        urlEchoResponseRepository.urlAsParameter("http://localhost:8080/api");

    assertThat(echoResponse.getUrl()).isEqualTo("/api/echo");
  }

  @Test
  public void urlInAbstractInterfaceImpl() {

    assertThat(urlEchoResponseRepository.getBaseUrl()).isEqualTo("http://remotehost:8080/api");

    urlEchoResponseRepository.setBaseUrl("http://localhost:8080/api");
    EchoResponse echoResponse = urlEchoResponseRepository.urlInAbstractInterfaceImpl();

    assertThat(echoResponse.getUrl()).isEqualTo("/api/echo");
    assertThat(urlEchoResponseRepository.getBaseUrl()).isEqualTo("http://localhost:8080/api");
  }
}
