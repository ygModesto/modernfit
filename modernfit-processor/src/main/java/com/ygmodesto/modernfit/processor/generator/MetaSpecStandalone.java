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

package com.ygmodesto.modernfit.processor.generator;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

/**
 * Class that supports {@link MetaSpec} when generating the implementations of the interfaces that 
 * have their componentModel defined in {@code @Modernfit} as ComponentModel.STANDALONE.
 */
public class MetaSpecStandalone extends MetaSpec {

  private MethodSpec.Builder constructor;
  private TypeSpec.Builder builderClass;

  public MetaSpecStandalone(String requestInfoBuilderName, String bodyBuilderName) {
    super(requestInfoBuilderName, bodyBuilderName);
  }

  public MethodSpec.Builder getConstructor() {
    return constructor;
  }

  public void setConstructor(MethodSpec.Builder constructor) {
    this.constructor = constructor;
  }

  public TypeSpec.Builder getBuilderClass() {
    return builderClass;
  }

  public void setBuilderClass(TypeSpec.Builder builderClass) {
    this.builderClass = builderClass;
  }
}
