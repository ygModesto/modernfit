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
import com.ygmodesto.modernfit.processor.repository.AsyncEchoResponseRepository;
import com.ygmodesto.modernfit.processor.server.EchoResponse;
import com.ygmodesto.modernfit.processor.server.ModelTO;
import com.ygmodesto.modernfit.processor.server.MultipartEchoResponse;
import com.ygmodesto.modernfit.services.ResponseCallback;
import com.ygmodesto.modernfit.services.ModernfitException;
import com.ygmodesto.modernfit.services.TypedContent;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.core.io.ClassPathResource;

@RunWith(JUnit4.class)
public class AsyncEchoResponseServerTest extends AbstractFunctionalTest {

  public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

  private static AsyncEchoResponseRepository asyncEchoResponseRepository;
  private static ObjectMapper objectMapper;

  @BeforeClass
  public static void setUp() throws Exception {
    asyncEchoResponseRepository = util(AsyncEchoResponseRepository.class, "Impl");
    objectMapper = new ObjectMapper();
  }

  private ResponseCallback<EchoResponse> getFutureCallback(CompletableFuture<EchoResponse> future) {

    return new ResponseCallback<EchoResponse>() {
      @Override
      public void onSuccess(EchoResponse echoResponse) throws ModernfitException {
        future.complete(echoResponse);
      }

      @Override
      public void onFailure(ModernfitException e) {
        future.completeExceptionally(e);
      }
    };
  }

  @Test
  public void getEchoTest() throws Exception {

    CompletableFuture<EchoResponse> future = new CompletableFuture<>();

    asyncEchoResponseRepository.getEcho(getFutureCallback(future));
    assertThat(future.get().getMethod()).isEqualTo("GET");
  }

  @Test
  public void headEchoTest() throws Exception {

    CompletableFuture<String> future = new CompletableFuture<>();

    ResponseCallback<Void> reponseCallback =
        new ResponseCallback<Void>() {

          @Override
          public void onSuccess(Void response) throws ModernfitException {
            future.complete("HEAD");
          }
        };

    asyncEchoResponseRepository.headEcho(reponseCallback);
    assertThat(future.get()).isEqualTo("HEAD");
  }

  @Test
  public void deleteEchoTest() throws Exception {

    CompletableFuture<EchoResponse> future = new CompletableFuture<>();

    asyncEchoResponseRepository.deleteEcho(getFutureCallback(future));
    assertThat(future.get().getMethod()).isEqualTo("DELETE");
  }

  @Test
  public void optionsEchoTest() throws Exception {

    CompletableFuture<String> future = new CompletableFuture<>();

    ResponseCallback<Void> responseCallback =
        new ResponseCallback<Void>() {

          @Override
          public void onSuccess(Void response) throws ModernfitException {
            future.complete("OPTIONS");
          }
        };

    asyncEchoResponseRepository.optionsEcho(responseCallback);
    assertThat(future.get()).isEqualTo("OPTIONS");
  }

  @Test
  public void postEchoWithBodyTest() throws Exception {

    CompletableFuture<EchoResponse> future = new CompletableFuture<>();

    ModelTO modelTO = new ModelTO(1L, "model", "@model");

    asyncEchoResponseRepository.postEchoWithBody(modelTO, getFutureCallback(future));
    assertThat(future.get().getMethod()).isEqualTo("POST");
    assertThat(objectMapper.readValue(future.get().getBody(), ModelTO.class)).isEqualTo(modelTO);
  }

  @Test
  public void putEchoWithBodyTest() throws Exception {

    CompletableFuture<EchoResponse> future = new CompletableFuture<>();

    ModelTO modelTO = new ModelTO(1L, "model", "@model");

    asyncEchoResponseRepository.putEchoWithBody(modelTO, getFutureCallback(future));
    assertThat(future.get().getMethod()).isEqualTo("PUT");
    assertThat(objectMapper.readValue(future.get().getBody(), ModelTO.class)).isEqualTo(modelTO);
  }

  @Test
  public void optionsEchoWithBodyTest() throws Exception {

    CompletableFuture<String> future = new CompletableFuture<>();

    ModelTO modelTO = new ModelTO(1L, "model", "@model");

    ResponseCallback<Void> responseCallback =
        new ResponseCallback<Void>() {

          @Override
          public void onSuccess(Void response) throws ModernfitException {
            future.complete("OPTIONS");
          }

          @Override
          public void onFailure(ModernfitException e) {
            future.completeExceptionally(e);
          }
        };

    asyncEchoResponseRepository.optionsEchoWithBody(modelTO, responseCallback);
    assertThat(future.get()).isEqualTo("OPTIONS");
  }

  @Test
  public void patchEchoWithBodyTest() throws Exception {

    CompletableFuture<EchoResponse> future = new CompletableFuture<>();

    ModelTO modelTO = new ModelTO(1L, "model", "@model");

    asyncEchoResponseRepository.patchEchoWithBody(modelTO, getFutureCallback(future));
    assertThat(future.get().getMethod()).isEqualTo("PATCH");
    assertThat(objectMapper.readValue(future.get().getBody(), ModelTO.class)).isEqualTo(modelTO);
  }

  @Test
  public void fieldsAndQueryMap()
      throws InterruptedException, ExecutionException, UnsupportedEncodingException {

    CompletableFuture<EchoResponse> future = new CompletableFuture<>();

    Long userId = 2L;

    List<Long> userIds = new ArrayList<Long>();
    userIds.add(1L);
    userIds.add(2L);

    String keyA = "portal";
    String keyB = "address";
    Map<String, String> queryMap = new HashMap<String, String>();
    queryMap.put(keyA, "2");
    queryMap.put(keyB, "Ames");

    asyncEchoResponseRepository.fieldsAndQueryMap(
        userId, userIds, queryMap, getFutureCallback(future));
    Map<String, List<String>> parametersExpected =
        MapUtils.populateExpectedMap(new HashMap<String, List<String>>(), "userId", userId);
    parametersExpected = MapUtils.populateExpectedMap(parametersExpected, "userIds", userIds);
    parametersExpected = MapUtils.populateExpectedMap(parametersExpected, queryMap);

    Map<String, List<String>> parameters = future.get().getParameters();

    assertThat(parameters).isEqualTo(parametersExpected);
    assertThat(future.get().getBody())
        .isEqualTo(MapUtils.toQueryStringEncoded(parametersExpected, DEFAULT_CHARSET));
  }

  @Test
  public void partsAndPartMapAndTypedContent()
      throws IOException, InterruptedException, ExecutionException {

    CompletableFuture<MultipartEchoResponse> future = new CompletableFuture<>();

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

    ResponseCallback<MultipartEchoResponse> responseCallback =
        new ResponseCallback<MultipartEchoResponse>() {

          @Override
          public void onSuccess(MultipartEchoResponse echoResponse) throws ModernfitException {
            future.complete(echoResponse);
          }

          @Override
          public void onFailure(ModernfitException e) {
            future.completeExceptionally(e);
          }
        };

    asyncEchoResponseRepository.partsAndPartMapAndTypedContent(
        partObjectA, partListA, partMap, typedContents, responseCallback);
    List<Long> listA =
        objectMapper.readValue(
            objectMapper.writeValueAsString(future.get().getPartListA()),
            new TypeReference<List<Long>>() {});
    List<Long> listB =
        objectMapper.readValue(
            objectMapper.writeValueAsString(future.get().getPartListB()),
            new TypeReference<List<Long>>() {});

    assertThat(Long.valueOf(future.get().getPartObjectA())).isEqualTo(partObjectA);
    assertThat(listA).isEqualTo(partListA);
    assertThat(listB).isEqualTo(partListB);
    assertThat(future.get().getFileParts()).hasSize(2);
    assertThat(future.get().getFileParts().get(0).getBytes())
        .isEqualTo(Files.readAllBytes(file.toPath()));
    assertThat(future.get().getFileParts().get(1).getBytes())
        .isEqualTo(Files.readAllBytes(file2.toPath()));
  }
}
