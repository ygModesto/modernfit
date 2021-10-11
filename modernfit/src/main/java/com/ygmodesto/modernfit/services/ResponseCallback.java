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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

// TODO change the name.
// TODO es necesario obligar a reescribir los metodos ?
/**
 * Class used to make asynchronous requests through callbacks.
 *
 * <p>It is imperative that the annotated method be of type void.
 *
 * <p>For example, if a method from which it is expected to receive an object of type Foo, 
 * we want to do Asynchronous with callbacks it is possible to do it 
 * with ResponseCallback as follows:
 *
 * <pre>
 *
 *  {@code void updateFoo(@Body FooUpdate fooUpdate, ResponseCallback<Foo> responseCallback);}
 *
 *  </pre>
 *
 * <p>There are 2 methods that should be overridden by the user
 *
 * <ul>
 *   <li><code>public void onSuccess(T t)</code></li>
 *   <li><code>public void onFailure(ModernfitException e)</code></li>
 * </ul>
 *
 * <p>Example of use:
 *
 * <pre><code>
 *  fooRepository.updateUser(new FooUpdate("bar"), new ResponseCallback&#60;Foo&#62;() {
 *
 *  &#64;Override
 *  public void onSuccess(Foo foo) {
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
public class ResponseCallback<T> {

  protected Converter<ResponseContent, T> converter;

  /**
   * Constructs a new type literal. Derives represented class from type parameter.
   *
   * <p>Clients create an empty anonymous subclass. Doing so embeds the type parameter in the
   * anonymous class's type hierarchy so we can reconstitute it at runtime despite erasure.
   */
  protected ResponseCallback() {}

  /** Returns an object of Type representing the actual type argument to this type. */
  public Type getType() {
    return ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }

  /**
   * Set the converter to be used to pass from ResponseConverter to the desired returned object of 
   * type T. No designer to be used by the library user.
   */
  public void setConverter(Converter<ResponseContent, T> converter) {
    this.converter = converter;
  }

  public Converter<ResponseContent, T> getConverter() {
    return converter;
  }

  /**
   * Method called by an {@link HttpClient HttpClient} in case of failure of the HTTP request.
   */
  public void notifyFailure(ModernfitException e) {
    onFailure(e);
  }

  /**
   * Method called by an {@link HttpClient HttpClient} in case of success in the HTTP request.
   */
  public void notifySuccess(ResponseContent response) {

    try {
      onSuccess(converter.convert(response));
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
  public void onSuccess(T t) {}

  /**
   * Method that will be called in case an exception occurs in the request.
   *
   * @param e ModernfitException encapsulating the true causing exception.
   */
  public void onFailure(ModernfitException e) {}
}
