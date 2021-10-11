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

import com.ygmodesto.modernfit.processor.repository.ScalarEchoResponseRepository;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScalarEchoResponseServerTest extends AbstractFunctionalTest {

  private static ScalarEchoResponseRepository scalarEchoResponseRepository;

  @BeforeClass
  public static void setUp() throws Exception {
    scalarEchoResponseRepository = util(ScalarEchoResponseRepository.class, "Impl");
  }

  @Test
  public void getEchoLongTest() throws Exception {

    Long scalar = scalarEchoResponseRepository.getEchoLong();
    assertThat(scalar).isEqualTo(5L);
  }

  @Test
  public void postEchoLongTest() throws Exception {

    Long value = 7L;
    Long scalar = scalarEchoResponseRepository.postEchoLong(value);
    assertThat(scalar).isEqualTo(value);
  }

  @Test
  public void postEchoLongUnboxedTest() throws Exception {

    long value = 7L;
    long scalar = scalarEchoResponseRepository.postEchoLongUnboxed(value);
    assertThat(scalar).isEqualTo(value);
  }

  @Test
  public void getEchoStringTest() throws Exception {

    String scalar = scalarEchoResponseRepository.getEchoString();
    assertThat(scalar).isEqualTo("echo");
  }

  @Test
  public void postEchoStringTest() throws Exception {

    String value = "pong";
    String scalar = scalarEchoResponseRepository.postEchoString(value);
    assertThat(scalar).isEqualTo(value);
  }
}
