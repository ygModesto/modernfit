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

import com.ygmodesto.modernfit.processor.ModernfitProcessorException;
import javax.lang.model.element.VariableElement;

/**
 * Defines the information required to generate a method that performs an HTTP request 
 * of type Discrete Body (not Multipart).
 */
public class DiscreteBodyInformation extends AbstractBodyInformation {

  private VariableElement body;

  private DiscreteBodyInformation(Builder builder) throws ModernfitProcessorException {
    super(builder);

    this.body = builder.body;
  }

  public VariableElement getBody() {
    return body;
  }

  public static AbstractBodyInformation.Builder builder() {
    return new Builder();
  }

  @Override
  protected void validate(AbstractBodyInformation.Builder builder)
      throws ModernfitProcessorException {
    if (!builder.fields.isEmpty()) {
      throw new ModernfitProcessorException(
          "@Field only allowed in @FormUrlEncoded method",
          methodInformation.getExecutableElement());
    } else if (!builder.fieldsMap.isEmpty()) {
      throw new ModernfitProcessorException(
          "@FieldMap only allowed in @FormUrlEncoded method",
          methodInformation.getExecutableElement());
    } else if (!builder.parts.isEmpty()) {
      throw new ModernfitProcessorException(
          "@Part only allowed in @Multipart method", methodInformation.getExecutableElement());
    } else if (!builder.partsMap.isEmpty()) {
      throw new ModernfitProcessorException(
          "@PartMap only allowed in @Multipart method", methodInformation.getExecutableElement());
    }
  }

  /** 
   * Builder class for {@link DiscreteBodyInformation DiscreteBodyInformation}. 
   */
  public static class Builder extends AbstractBodyInformation.Builder {

    @Override
    public DiscreteBodyInformation build() throws ModernfitProcessorException {
      return new DiscreteBodyInformation(this);
    }
  }
}
