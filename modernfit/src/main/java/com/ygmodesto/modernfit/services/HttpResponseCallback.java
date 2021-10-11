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

package com.ygmodesto.modernfit.services;

import com.ygmodesto.modernfit.converters.Converter;

// TODO es necesario obligar a reescribir los metodos ?
/**
 * Class used to make asynchronous requests through callbacks.
 *
 * <p>It is imperative that the annotated method be of type void.
 *
 * <p>For example, if a method from which it is expected to receive an object of type Foo
 * and the HTTP info of the response, 
 * we want to do Asynchronous with callbacks it is possible to do it 
 * with HttpResponseCallback as follows:
 *
 * <pre>
 *
 *  {@code void updateFoo(@Body FooUpdate fooUpdate, HttpResponseCallback<Foo> responseCallback);}
 *
 *  </pre>
 *
 * <p>There are 2 methods that should be overridden by the user
 *
 * <ul>
 *   <li><code>public void onSuccess(HttpInfo&#60;T&#62; t)</code></li>
 *   <li><code>public void onFailure(ModernfitException e)</code></li>
 * </ul>
 *
 * <p>Example of use:
 *
 * <pre><code>
 *  fooRepository.updateUser(new FooUpdate("bar"), new HttpResponseCallback&#60;Foo&#62;() {
 *
 *  &#64;Override
 *  public void onSuccess(HttpInfo&#60;Foo&#62; foo) {
 *      //something
 *  }
 *
 *  &#64;Override
 *  public void onFailure(ModernfitException e) {
 *      //something
 *  }
 *  }
 *  </code></pre>
 *
 * @param <T> the type of return object.
 */
public class HttpResponseCallback<T> extends ResponseCallback<T> {


  /**
   * Method called by an {@link HttpClient HttpClient} in case of success in the HTTP request.
   */
  public void notifySuccess(ResponseContent response) {

    try {
      onSuccess(toHttpInfo(converter, response));
    } catch (ModernfitException e) {
      onFailure(e);
    } catch (Exception e) {
      onFailure(new ModernfitException(e));
    }
  }

  // TODO revisar si clase anonima de interface
  /**
   * Method to be overridden if an object of type T is expected to be received in response.
   *
   * @param t the response converted to an object of type T.
   */
  public void onSuccess(HttpInfo<T> t) {}


  protected HttpInfo<T> toHttpInfo(Converter<ResponseContent, T> converter,
      ResponseContent responseContent) {
    T value = converter.convert(responseContent);
    return new HttpInfo<T>(responseContent.getCode(), responseContent.getHeaders(), value);
  }
}
