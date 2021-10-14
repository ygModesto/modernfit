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
import javax.lang.model.type.TypeMirror;

/**
 * Defines the information required to generate a method that performs an HTTP request 
 * of type application/x-www-form-urlencoded.
 */
public class FormUrlEncodedBodyInformation extends AbstractBodyInformation {

  private List<AnnotationInformation> fields;
  private List<AnnotationInformation> fieldsMap;

  private FormUrlEncodedBodyInformation(Builder builder) throws ModernfitProcessorException {
    super(builder);

    this.fields = builder.fields;
    this.fieldsMap = builder.fieldsMap;
  }

  public List<AnnotationInformation> getFields() {
    return fields;
  }

  public List<AnnotationInformation> getFieldsMap() {
    return fieldsMap;
  }

  public static AbstractBodyInformation.Builder builder() {
    return new Builder();
  }

  @Override
  protected void validate(AbstractBodyInformation.Builder builder)
      throws ModernfitProcessorException {
    if (!builder.parts.isEmpty()) {
      throw new ModernfitProcessorException(
          "@Part only allowed in @Multipart method", methodInformation.getExecutableElement());
    } else if (!builder.partsMap.isEmpty()) {
      throw new ModernfitProcessorException(
          "@PartMap only allowed in @Multipart method", methodInformation.getExecutableElement());
    } else if (builder.body != null) {
      throw new ModernfitProcessorException(
          "@Body not allowed in @FormUrlEncoded method", methodInformation.getExecutableElement());
    } else if (!builder.fieldsMap.isEmpty()) {
      for (AnnotationInformation ai : builder.fieldsMap) {
        TypeMirror asType = ai.getVariableElement().asType();
        if (!utils.isSameGenericType(asType, Map.class)
            || !utils.isSubtype(utils.getFirstTypeArgument(asType), String.class)) {
          throw new ModernfitProcessorException(
              "@FieldMap must be a Map with a String key", ai.getVariableElement());
        }
      }
    }
  }
 

  /** 
   * Builder class for {@link FormUrlEncodedBodyInformation FormUrlEncodedBodyInformation}. 
   */
  public static class Builder extends AbstractBodyInformation.Builder {

    @Override
    public FormUrlEncodedBodyInformation build() throws ModernfitProcessorException {
      return new FormUrlEncodedBodyInformation(this);
    }
  }
}
