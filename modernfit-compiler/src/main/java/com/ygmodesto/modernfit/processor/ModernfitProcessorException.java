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

package com.ygmodesto.modernfit.processor;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;

/**
 * Class to encapsulate unexpected errors in processor stage.
 *
 */
public class ModernfitProcessorException extends Exception {

  private Element element;
  private AnnotationMirror annotationMirror;
  private AnnotationValue annotationValue;

  private static final long serialVersionUID = 1L;

  public ModernfitProcessorException(String msg, Element element) {
    super(msg);
    this.element = element;
  }

  /**
   * Construct the exception indicating which item was being processed when it occurred.
   */
  public ModernfitProcessorException(
      String msg, Element element, AnnotationMirror annotationMirror) {
    super(msg);
    this.element = element;
    this.annotationMirror = annotationMirror;
  }

  /**
   * Construct the exception indicating which item was being processed when it occurred.
   */
  public ModernfitProcessorException(
      String msg,
      Element element,
      AnnotationMirror annotationMirror,
      AnnotationValue annotationValue) {
    super(msg);
    this.element = element;
    this.annotationMirror = annotationMirror;
    this.annotationValue = annotationValue;
  }

  public Element getElement() {
    return element;
  }

  public AnnotationMirror getAnnotationMirror() {
    return annotationMirror;
  }

  public AnnotationValue getAnnotationValue() {
    return annotationValue;
  }
}
