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

package com.ygmodesto.modernfit.processor.syntactic;

import com.ygmodesto.modernfit.processor.repository.ApplicationJsonRepository;
import com.ygmodesto.modernfit.processor.repository.ApplicationJsonRepositoryAsync;
import com.ygmodesto.modernfit.processor.repository.ApplicationJsonRepositoryAsyncImpl;
import com.ygmodesto.modernfit.processor.repository.ApplicationJsonRepositoryImpl;
import com.ygmodesto.modernfit.processor.repository.ApplicationJsonRepositoryRxJava2;
import com.ygmodesto.modernfit.processor.repository.ApplicationJsonRepositoryRxJava2Impl;
import com.ygmodesto.modernfit.processor.repository.ApplicationJsonRepositoryRxJava3;
import com.ygmodesto.modernfit.processor.repository.ApplicationJsonRepositoryRxJava3Impl;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ApplicationJsonTest extends AbstractSyntacticTest {

  @Test
  public void syncTest() throws IOException {

    coreTest(ApplicationJsonRepository.class, ApplicationJsonRepositoryImpl.class);
  }

  @Test
  public void asyncTest() throws IOException {

    coreTest(ApplicationJsonRepositoryAsync.class, ApplicationJsonRepositoryAsyncImpl.class);
  }

  @Test
  public void rxJava2Test() throws IOException {

    coreTest(ApplicationJsonRepositoryRxJava2.class, ApplicationJsonRepositoryRxJava2Impl.class);
  }

  @Test
  public void rxJava3Test() throws IOException {

    coreTest(ApplicationJsonRepositoryRxJava3.class, ApplicationJsonRepositoryRxJava3Impl.class);
  }
}
