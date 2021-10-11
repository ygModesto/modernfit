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

package com.ygmodesto.modernfit.processor.server;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class FunctionalREST {

  @RequestMapping("/echo/scalar/{type}/**")
  public String echoScalarStringRequest(
      HttpServletRequest request,
      @PathVariable String type,
      @RequestHeader Map<String, String> headers,
      @RequestParam MultiValueMap<String, String> params,
      @RequestBody(required = false) String body) {

    if (type.equals("long") && (body == null)) {
      return "5";
    }

    return body == null ? "echo" : body;
  }

  @RequestMapping(value = {"/{httpCode}/echo/**", "/echo/**"})
  public ResponseEntity<EchoResponse> echoRequest(
      HttpServletRequest request,
      @RequestHeader Map<String, String> headers,
      @RequestParam MultiValueMap<String, String> params,
      @PathVariable(required = false) Integer httpCode,
      @RequestBody(required = false) String body) {

    EchoResponse echoResponse = new EchoResponse();
    echoResponse.setHeaders(headers);
    echoResponse.setBody(body);
    echoResponse.setUrl(request.getRequestURI());
    echoResponse.setMethod(request.getMethod());
    echoResponse.setContentType(request.getContentType());
    echoResponse.setParametersAsArray(request.getParameterMap());
    echoResponse.setQueryString(request.getQueryString());

    HttpStatus httpStatus = httpCode == null ? HttpStatus.OK : HttpStatus.resolve(httpCode);

    // fixme remove this with a funcionality
    echoResponse.setCode(httpStatus.value());

    return new ResponseEntity<EchoResponse>(echoResponse, httpStatus);
  }

  @RequestMapping(value = "/multipart/echo/**", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public MultipartEchoResponse multipartEchoRequest(
      HttpServletRequest request,
      @RequestHeader Map<String, String> headers,
      @RequestPart(required = false) String partObjectA,
      @RequestPart(required = false) String partObjectB,
      @RequestPart(required = false) Collection<String> partListA,
      @RequestPart(required = false) Collection<String> partListB,
      @RequestPart(required = false) Collection<MultipartFile> fileParts)
      throws IOException {

    MultipartEchoResponse echoResponse = new MultipartEchoResponse();
    echoResponse.setHeaders(headers);
    echoResponse.setPartObjectA(partObjectA);
    echoResponse.setPartObjectB(partObjectB);
    echoResponse.setPartListA(partListA);
    echoResponse.setPartListB(partListB);
    if ((fileParts != null) && (!fileParts.isEmpty())) {
      for (MultipartFile filePart : fileParts) {
        // TODO use charset when modernfit support them
        echoResponse.addFilePart(new String(filePart.getBytes()));
      }
    }
    echoResponse.setUrl(request.getRequestURI());
    echoResponse.setMethod(request.getMethod());
    echoResponse.setContentType(request.getContentType());
    echoResponse.setParametersAsArray(request.getParameterMap());
    echoResponse.setQueryString(request.getQueryString());

    return echoResponse;
  }

  @RequestMapping(value = "/echo/head", method = RequestMethod.HEAD)
  public void echoHeadRequest() {}

  @RequestMapping(value = "/echo/options", method = RequestMethod.OPTIONS)
  public void echoOptionsRequest() {}
}
