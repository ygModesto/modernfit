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
import com.ygmodesto.modernfit.processor.repository.MultipartEchoResponseRepository;
import com.ygmodesto.modernfit.processor.server.ModelTO;
import com.ygmodesto.modernfit.processor.server.MultipartEchoResponse;
import com.ygmodesto.modernfit.services.TypedContent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
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
public class MultipartEchoResponseServerTest extends AbstractFunctionalTest {

  public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

  private static MultipartEchoResponseRepository multipartEchoResponseRepository;
  private static ObjectMapper objectMapper;

  @BeforeClass
  public static void setUp() throws Exception {
    multipartEchoResponseRepository = util(MultipartEchoResponseRepository.class, "Impl");
    objectMapper = new ObjectMapper();
  }

  @Test
  public void partValueFromAnnotation() {

    Long userId = 2L;
    MultipartEchoResponse echoResponse =
        multipartEchoResponseRepository.partValueFromAnnotation(userId);
    assertThat(Long.valueOf(echoResponse.getPartObjectA())).isEqualTo(userId);
  }

  @Test
  public void partValueNameOfParameter() {

    Long userId = 2L;
    MultipartEchoResponse echoResponse =
        multipartEchoResponseRepository.partValueNameOfParameter(userId);
    assertThat(Long.valueOf(echoResponse.getPartObjectA())).isEqualTo(userId);
  }

  @Test
  public void partListValueNameOfParameter() throws IOException {

    List<Long> userIds = new ArrayList<Long>();
    userIds.add(1L);
    userIds.add(2L);
    MultipartEchoResponse echoResponse = multipartEchoResponseRepository.partList(userIds);
    List<Long> list =
        objectMapper.readValue(
            objectMapper.writeValueAsString(echoResponse.getPartListA()),
            new TypeReference<List<Long>>() {});
    assertThat(list).isEqualTo(userIds);
  }

  @Test
  public void partArrayValueNameOfParameter() throws IOException {

    Long userIds[] = new Long[] {1L, 2L};
    MultipartEchoResponse echoResponse = multipartEchoResponseRepository.partArray(userIds);
    List<Long> list =
        objectMapper.readValue(
            objectMapper.writeValueAsString(echoResponse.getPartListA()),
            new TypeReference<List<Long>>() {});
    assertThat(list).isEqualTo(Arrays.asList(userIds));
  }

  @Test
  public void partMapLongs() {

    String keyA = "partObjectA";
    Map<String, Long> partMap = new HashMap<String, Long>();
    partMap.put(keyA, 81L);
    MultipartEchoResponse echoResponse =
        multipartEchoResponseRepository.partMapStringKeyLongValue(partMap);

    assertThat(Long.valueOf(echoResponse.getPartObjectA())).isEqualTo(partMap.get(keyA));
  }

  @Test
  public void partsAndPartMap() throws IOException {

    Long partObjectA = 2L;

    List<Long> partListA = new ArrayList<Long>();
    partListA.add(1L);
    partListA.add(2L);

    List<Long> partListB = new ArrayList<Long>();
    partListB.add(4L);
    partListB.add(5L);
    String keyPartListB = "partListB";
    Map<String, Collection<Long>> partMap = new HashMap<String, Collection<Long>>();
    partMap.put(keyPartListB, partListB);
    MultipartEchoResponse echoResponse =
        multipartEchoResponseRepository.partsAndPartMap(partObjectA, partListA, partMap);

    assertThat(Long.valueOf(echoResponse.getPartObjectA())).isEqualTo(partObjectA);

    List<Long> listA =
        objectMapper.readValue(
            objectMapper.writeValueAsString(echoResponse.getPartListA()),
            new TypeReference<List<Long>>() {});
    List<Long> listB =
        objectMapper.readValue(
            objectMapper.writeValueAsString(echoResponse.getPartListB()),
            new TypeReference<List<Long>>() {});
    assertThat(listA).isEqualTo(partListA);
    assertThat(listB).isEqualTo(partListB);
  }

  @Test
  public void partValueFromAnnotationObject() throws IOException {

    ModelTO modelTOExpected = new ModelTO(2L, "model", "@model");
    MultipartEchoResponse echoResponse =
        multipartEchoResponseRepository.partValueFromAnnotationObject(modelTOExpected);
    ModelTO modelTOResponse = objectMapper.readValue(echoResponse.getPartObjectA(), ModelTO.class);
    assertThat(modelTOResponse).isEqualTo(modelTOExpected);
  }

  @Test
  public void partValueFromAnnotationTypedContent() throws IOException {

    File file = new ClassPathResource("filetoload.txt").getFile();
    TypedContent typedContent = TypedContent.create("text/plain", file);
    MultipartEchoResponse echoResponse =
        multipartEchoResponseRepository.partValueFromAnnotationTypedContent(typedContent);
    assertThat(echoResponse.getFileParts()).hasSize(1);
    assertThat(echoResponse.getFileParts().get(0).getBytes())
        .isEqualTo(Files.readAllBytes(file.toPath()));
  }

  @Test
  public void partValueFromAnnotationListTypedContent() throws IOException {

    Collection<TypedContent> typedContents = new ArrayList<TypedContent>();
    File file = new ClassPathResource("filetoload.txt").getFile();
    typedContents.add(TypedContent.create("text/plain", file));
    File file2 = new ClassPathResource("filetoload2.txt").getFile();
    typedContents.add(TypedContent.create("text/plain", file2));

    MultipartEchoResponse echoResponse =
        multipartEchoResponseRepository.partValueFromAnnotationListTypedContent(typedContents);
    assertThat(echoResponse.getFileParts()).hasSize(2);
    assertThat(echoResponse.getFileParts().get(0).getBytes())
        .isEqualTo(Files.readAllBytes(file.toPath()));
    assertThat(echoResponse.getFileParts().get(1).getBytes())
        .isEqualTo(Files.readAllBytes(file2.toPath()));
  }

  @Test
  public void partValueFromAnnotationArrayTypedContent() throws IOException {

    TypedContent[] typedContents = new TypedContent[2];
    File file = new ClassPathResource("filetoload.txt").getFile();
    typedContents[0] = TypedContent.create("text/plain", file);
    File file2 = new ClassPathResource("filetoload2.txt").getFile();
    typedContents[1] = TypedContent.create("text/plain", file2);

    MultipartEchoResponse echoResponse =
        multipartEchoResponseRepository.partValueFromAnnotationArrayTypedContent(typedContents);
    assertThat(echoResponse.getFileParts()).hasSize(2);
    assertThat(echoResponse.getFileParts().get(0).getBytes())
        .isEqualTo(Files.readAllBytes(file.toPath()));
    assertThat(echoResponse.getFileParts().get(1).getBytes())
        .isEqualTo(Files.readAllBytes(file2.toPath()));
  }

  @Test
  public void partValueFromAnnotationPartMapTypedContent() throws IOException {

    Map<String, TypedContent> typedContents = new HashMap<String, TypedContent>();
    File file = new ClassPathResource("filetoload.txt").getFile();
    typedContents.put("fileParts", TypedContent.create("text/plain", file));

    MultipartEchoResponse echoResponse =
        multipartEchoResponseRepository.partValueFromAnnotationPartMapTypedContent(typedContents);
    assertThat(echoResponse.getFileParts()).hasSize(1);
    assertThat(echoResponse.getFileParts().get(0).getBytes())
        .isEqualTo(Files.readAllBytes(file.toPath()));
  }

  @Test
  public void partsAndPartMapAndTypedContent() throws IOException {

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

    MultipartEchoResponse echoResponse =
        multipartEchoResponseRepository.partsAndPartMapAndTypedContent(
            partObjectA, partListA, partMap, typedContents);

    assertThat(Long.valueOf(echoResponse.getPartObjectA())).isEqualTo(partObjectA);

    List<Long> listA =
        objectMapper.readValue(
            objectMapper.writeValueAsString(echoResponse.getPartListA()),
            new TypeReference<List<Long>>() {});
    List<Long> listB =
        objectMapper.readValue(
            objectMapper.writeValueAsString(echoResponse.getPartListB()),
            new TypeReference<List<Long>>() {});
    assertThat(listA).isEqualTo(partListA);
    assertThat(listB).isEqualTo(partListB);
    assertThat(echoResponse.getFileParts()).hasSize(2);
    assertThat(echoResponse.getFileParts().get(0).getBytes())
        .isEqualTo(Files.readAllBytes(file.toPath()));
    assertThat(echoResponse.getFileParts().get(1).getBytes())
        .isEqualTo(Files.readAllBytes(file2.toPath()));
  }
}
