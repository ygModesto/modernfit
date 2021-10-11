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

import com.ygmodesto.modernfit.annotations.Field;
import com.ygmodesto.modernfit.annotations.FieldMap;
import com.ygmodesto.modernfit.annotations.Part;
import com.ygmodesto.modernfit.processor.ModernfitProcessorException;
import com.ygmodesto.modernfit.processor.Utils;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.VariableElement;

/**
 * Defines the common elements to generate the methods with requests of type 
 * {@code MultipartBody}, {@code FormUrlEncodedBody} and {@code DiscreteBody}.
 */
public abstract class AbstractBodyInformation {
  
  protected Utils utils = Utils.getInstance();

  protected MethodInformation methodInformation;

  protected AbstractBodyInformation(Builder builder) throws ModernfitProcessorException {

    this.methodInformation = builder.methodInformation;

    validate(builder);
  }

  protected abstract void validate(Builder builder) throws ModernfitProcessorException;

  public MethodInformation getMethodInformation() {
    return methodInformation;
  }

  /** 
   * Builder class for {@link AbstractBodyInformation AbstractBodyInformation}. 
   */
  public abstract static class Builder {
    
    protected static final String EMPTY_STRING = "";

    protected MethodInformation methodInformation;

    protected VariableElement body;
    protected List<AnnotationInformation> fields = new ArrayList<AnnotationInformation>();
    protected List<AnnotationInformation> fieldsMap = new ArrayList<AnnotationInformation>();
    protected List<AnnotationInformation> parts = new ArrayList<AnnotationInformation>();
    protected List<VariableElement> partsMap = new ArrayList<VariableElement>();

    public Builder addMethodInformation(MethodInformation methodInformation) {
      this.methodInformation = methodInformation;
      return this;
    }

    public Builder addBody(VariableElement body) {
      this.body = body;
      return this;
    }

    /**
     * Add an AnnotationInformation to the list of fields from a Field 
     * and the VariableElement annotated with it.
     */
    public Builder addField(Field field, VariableElement variableElement) {

      String fieldName =
          (field.value().equals(EMPTY_STRING))
              ? variableElement.getSimpleName().toString()
              : field.value();
      fields.add(new AnnotationInformation(fieldName, field.encoded(), variableElement));

      return this;
    }

    /**
     * Add an AnnotationInformation to the list of fieldsMap from a FieldMap 
     * and the VariableElement annotated with it.
     */
    public Builder addFieldMap(FieldMap fieldMap, VariableElement variableElement) {

      fieldsMap.add(new AnnotationInformation(fieldMap.encoded(), variableElement));

      return this;
    }

    /**
     * Add an AnnotationInformation to the list of parts from a Part 
     * and the VariableElement annotated with it.
     */
    public Builder addPart(Part part, VariableElement variableElement) {

      String fieldName =
          (part.value().equals(EMPTY_STRING))
              ? variableElement.getSimpleName().toString()
              : part.value();
      parts.add(new AnnotationInformation(fieldName, variableElement));

      return this;
    }

    /**
     * Add an AnnotationInformation to the list of partsMap from the VariableElement 
     * annotated with PartMap.
     */
    public Builder addPartMap(VariableElement variableElement) {

      partsMap.add(variableElement);

      return this;
    }

    public abstract AbstractBodyInformation build() throws ModernfitProcessorException;
  }
}
