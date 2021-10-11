package com.ygmodesto.modernfit.services;

import java.util.Map;

/**
 * Class to use when the HTTP code and headers information is needed in addition to the response
 * converted to an object.
 *
 * @param <T> The type of object to which the response body will be converted.
 */
public class HttpInfo<T> {

  private int code;
  private Map<String, String> headers;
  private T body;

  /**
   * Build an object from its fields.
   *
   * @param code HTTP status code.
   * @param headers of the HTTP response.
   * @param body of the HTTP response as a object.
   */
  public HttpInfo(int code, Map<String, String> headers, T body) {
    this.code = code;
    this.headers = headers;
    this.body = body;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public T getBody() {
    return body;
  }

  public void setBody(T body) {
    this.body = body;
  }

  @Override
  public String toString() {
    return "HttpInfo [code=" + code + ", headers=" + headers + ", body=" + body + "]";
  }

}
