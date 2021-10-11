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

/**
 * This interface must be implemented by all requests other than Multipart.
 */
public interface DiscreteBody {

  /**
   * Represents the Content-Type entity header.
   * The Content-Type is used to indicate the media type of the resource.
   */
  public String getContentType();

  /**
   * The body request as a byte array.
   */
  public byte[] getContent();
}
