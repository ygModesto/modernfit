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

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

/**
 * Defines the information required to create a field of type 
 * com.ygmodesto.modernfit.converters.Converter.
 */
public class ConverterSpec {

  private FieldSpec field;
  private TypeName source;
  private TypeName destination;

  /**
   * Create a ConverterSpec from the {@link FieldSpec} the TypeName of the source data type and
   * the TypeName of the target data type.
   *
   * @param field FiledSpec of the converter field.
   * @param source TypeName of source data type.
   * @param destination TypeName of destination data type.
   */
  public ConverterSpec(FieldSpec field, TypeName source, TypeName destination) {
    this.field = field;
    this.source = source;
    this.destination = destination;
  }

  public FieldSpec getField() {
    return field;
  }

  public void setField(FieldSpec field) {
    this.field = field;
  }     

  public TypeName getSource() {
    return source;
  }

  public void setSource(TypeName source) {
    this.source = source;
  }

  public TypeName getDestination() {
    return destination;
  }

  public void setDestination(TypeName destination) {
    this.destination = destination;
  }

  @Override
  public String toString() {
    return "ConverterSpec [field="
        + field
        + ", source="
        + source
        + ", destination="
        + destination
        + "]";
  }
}
