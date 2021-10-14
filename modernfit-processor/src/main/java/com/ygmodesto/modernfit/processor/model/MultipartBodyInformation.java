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
import java.util.List;
import java.util.Map;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * It defines all the information necessary to generate a method that performs an HTTP 
 * request of type Multipart.
 */
public class MultipartBodyInformation extends AbstractBodyInformation {

  private List<AnnotationInformation> parts;
  private List<VariableElement> partsMap;

  private MultipartBodyInformation(Builder builder) throws ModernfitProcessorException {
    super(builder);

    this.parts = builder.parts;
    this.partsMap = builder.partsMap;
  }

  public List<AnnotationInformation> getParts() {
    return parts;
  }

  public List<VariableElement> getPartsMap() {
    return partsMap;
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
    } else if (builder.body != null) {
      throw new ModernfitProcessorException(
          "@Body not allowed in @Multipart method", methodInformation.getExecutableElement());
    } else if (!builder.partsMap.isEmpty()) {
      for (VariableElement variableElement : builder.partsMap) {
        TypeMirror asType = variableElement.asType();
        if (!utils.isSameGenericType(asType, Map.class)
            || !utils.isSubtype(utils.getFirstTypeArgument(asType), String.class)) {
          throw new ModernfitProcessorException(
              "@PartMap must be a Map with a String key", variableElement);
        }
      }
    }
  }

  /** 
   * Builder class for {@link MultipartBodyInformation MultipartBodyInformation}. 
   */
  public static class Builder extends AbstractBodyInformation.Builder {

    @Override
    public MultipartBodyInformation build() throws ModernfitProcessorException {
      return new MultipartBodyInformation(this);
    }
  }
}
