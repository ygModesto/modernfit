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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ygmodesto.modernfit.processor.MapUtils;
import com.ygmodesto.modernfit.processor.repository.RxJava2EchoResponseRepository;
import com.ygmodesto.modernfit.processor.server.EchoResponse;
import com.ygmodesto.modernfit.processor.server.ModelTO;
import com.ygmodesto.modernfit.processor.server.MultipartEchoResponse;
import com.ygmodesto.modernfit.services.TypedContent;
import io.reactivex.observers.TestObserver;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.core.io.ClassPathResource;

@RunWith(JUnit4.class)
public class RxJava2EchoResponseServerTest extends AbstractFunctionalTest {

  public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

  private static RxJava2EchoResponseRepository rxEchoResponseRepository;
  private static ObjectMapper objectMapper;

  @BeforeClass
  public static void setUp() throws Exception {
    rxEchoResponseRepository = util(RxJava2EchoResponseRepository.class, "Impl");
    objectMapper = new ObjectMapper();
  }

  @Test
  public void getEchoTest() throws Exception {

    TestObserver<EchoResponse> observer = new TestObserver<EchoResponse>();

    rxEchoResponseRepository.getEcho().subscribe(observer);

    observer.assertComplete();
    observer.assertNoErrors();
    assertThat(observer.values().get(0).getMethod()).isEqualTo("GET");
  }

  @Test
  public void headEchoTest() throws Exception {

    TestObserver<EchoResponse> observer = new TestObserver<EchoResponse>();

    rxEchoResponseRepository.headEcho().subscribe(observer);

    observer.assertComplete();
    observer.assertNoErrors();
  }

  @Test
  public void deleteEchoTest() throws Exception {

    TestObserver<EchoResponse> observer = new TestObserver<EchoResponse>();

    rxEchoResponseRepository.deleteEcho().subscribe(observer);

    observer.assertComplete();
    observer.assertNoErrors();
    assertThat(observer.values().get(0).getMethod()).isEqualTo("DELETE");
  }

  @Test
  public void optionsEchoTest() throws Exception {

    TestObserver<EchoResponse> observer = new TestObserver<EchoResponse>();

    rxEchoResponseRepository.optionsEcho().subscribe(observer);

    observer.assertComplete();
    observer.assertNoErrors();
  }

  @Test
  public void postEchoWithBodyTest() throws Exception {

    TestObserver<EchoResponse> observer = new TestObserver<EchoResponse>();

    ModelTO modelTO = new ModelTO(1L, "model", "@model");

    rxEchoResponseRepository.postEchoWithBody(modelTO).subscribe(observer);

    observer.assertComplete();
    observer.assertNoErrors();
    EchoResponse echoResponse = observer.values().get(0);

    assertThat(echoResponse.getMethod()).isEqualTo("POST");
    assertThat(objectMapper.readValue(echoResponse.getBody(), ModelTO.class)).isEqualTo(modelTO);
  }

  @Test
  public void putEchoWithBodyTest() throws Exception {

    TestObserver<EchoResponse> observer = new TestObserver<EchoResponse>();

    ModelTO modelTO = new ModelTO(1L, "model", "@model");

    rxEchoResponseRepository.putEchoWithBody(modelTO).subscribe(observer);

    observer.assertComplete();
    observer.assertNoErrors();
    EchoResponse echoResponse = observer.values().get(0);
    assertThat(echoResponse.getMethod()).isEqualTo("PUT");
    assertThat(objectMapper.readValue(echoResponse.getBody(), ModelTO.class)).isEqualTo(modelTO);
  }

  @Test
  public void optionsEchoWithBodyTest() throws Exception {

    TestObserver<EchoResponse> observer = new TestObserver<EchoResponse>();

    ModelTO modelTO = new ModelTO(1L, "model", "@model");

    rxEchoResponseRepository.optionsEchoWithBody(modelTO).subscribe(observer);

    observer.assertComplete();
    observer.assertNoErrors();
  }

  @Test
  public void patchEchoWithBodyTest() throws Exception {

    TestObserver<EchoResponse> observer = new TestObserver<EchoResponse>();

    ModelTO modelTO = new ModelTO(1L, "model", "@model");

    rxEchoResponseRepository.patchEchoWithBody(modelTO).subscribe(observer);

    observer.assertComplete();
    observer.assertNoErrors();
    EchoResponse echoResponse = observer.values().get(0);
    assertThat(echoResponse.getMethod()).isEqualTo("PATCH");
    assertThat(objectMapper.readValue(echoResponse.getBody(), ModelTO.class)).isEqualTo(modelTO);
  }

  @Test
  public void fieldsAndQueryMap() throws UnsupportedEncodingException {

    TestObserver<EchoResponse> observer = new TestObserver<EchoResponse>();

    Long userId = 2L;

    List<Long> userIds = new ArrayList<Long>();
    userIds.add(1L);
    userIds.add(2L);

    String keyA = "portal";
    String keyB = "address";
    Map<String, String> queryMap = new HashMap<String, String>();
    queryMap.put(keyA, "2");
    queryMap.put(keyB, "Ames");

    Map<String, List<String>> parametersExpected =
        MapUtils.populateExpectedMap(new HashMap<String, List<String>>(), "userId", userId);
    parametersExpected = MapUtils.populateExpectedMap(parametersExpected, "userIds", userIds);
    parametersExpected = MapUtils.populateExpectedMap(parametersExpected, queryMap);

    rxEchoResponseRepository.fieldsAndQueryMap(userId, userIds, queryMap).subscribe(observer);

    observer.assertComplete();
    observer.assertNoErrors();
    EchoResponse echoResponse = observer.values().get(0);
    Map<String, List<String>> parameters = echoResponse.getParameters();
    assertThat(parameters).isEqualTo(parametersExpected);
    assertThat(echoResponse.getBody())
        .isEqualTo(MapUtils.toQueryStringEncoded(parametersExpected, DEFAULT_CHARSET));
  }

  @Test
  public void partsAndPartMapAndTypedContent() throws IOException {

    TestObserver<MultipartEchoResponse> observer = new TestObserver<MultipartEchoResponse>();

    Long partObjectA = 2L;

    List<Long> partListA = new ArrayList<Long>();
    partListA.add(1L);
    partListA.add(2L);

    List<Long> partListB = new ArrayList<Long>();
    partListB.add(4L);
    partListB.add(5L);
    String keyA = "partListB";
    Map<String, Collection<Long>> partMap = new HashMap<String, Collection<Long>>();
    partMap.put(keyA, partListB);

    Collection<TypedContent> typedContents = new ArrayList<TypedContent>();
    File file = new ClassPathResource("filetoload.txt").getFile();
    typedContents.add(TypedContent.create("text/plain", file));
    File file2 = new ClassPathResource("filetoload2.txt").getFile();
    typedContents.add(TypedContent.create("text/plain", file2));

    rxEchoResponseRepository
        .partsAndPartMapAndTypedContent(partObjectA, partListA, partMap, typedContents)
        .subscribe(observer);

    observer.assertComplete();
    observer.assertNoErrors();
    MultipartEchoResponse echoResponse = observer.values().get(0);
    List<Long> listA =
        objectMapper.readValue(
            objectMapper.writeValueAsString(echoResponse.getPartListA()),
            new TypeReference<List<Long>>() {});
    List<Long> listB =
        objectMapper.readValue(
            objectMapper.writeValueAsString(echoResponse.getPartListB()),
            new TypeReference<List<Long>>() {});

    assertThat(Long.valueOf(echoResponse.getPartObjectA())).isEqualTo(partObjectA);
    assertThat(listA).isEqualTo(partListA);
    assertThat(listB).isEqualTo(partListB);
    assertThat(echoResponse.getFileParts()).hasSize(2);
    assertThat(echoResponse.getFileParts().get(0).getBytes())
        .isEqualTo(Files.readAllBytes(file.toPath()));
    assertThat(echoResponse.getFileParts().get(1).getBytes())
        .isEqualTo(Files.readAllBytes(file2.toPath()));
  }
}
