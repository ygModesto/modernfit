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

package com.ygmodesto.modernfit.processor.model;

import javax.lang.model.element.VariableElement;

/**
 * Stores annotation information that annotates a {@code VariableElement} 
 * and contains the name and (optional) encoded fields.
 */
public class AnnotationInformation {

  private String name;
  private boolean encoded;
  private VariableElement variableElement;

  public AnnotationInformation(VariableElement variableElement) {
    this.variableElement = variableElement;
  }

  public AnnotationInformation(String name, VariableElement variableElement) {
    this.name = name;
    this.variableElement = variableElement;
  }

  public AnnotationInformation(boolean encoded, VariableElement variableElement) {
    this.encoded = encoded;
    this.variableElement = variableElement;
  }

  /**
   * Construct an {@code AnnotationInformation} from all its fields.
   */
  public AnnotationInformation(String name, boolean encoded, VariableElement variableElement) {
    this.name = name;
    this.encoded = encoded;
    this.variableElement = variableElement;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isEncoded() {
    return encoded;
  }

  public void setEncoded(boolean encoded) {
    this.encoded = encoded;
  }

  public String getVariableName() {
    return variableElement.getSimpleName().toString();
  }

  public VariableElement getVariableElement() {
    return variableElement;
  }

  public void setVariableElement(VariableElement variableElement) {
    this.variableElement = variableElement;
  }
}
