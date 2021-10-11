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
import com.ygmodesto.modernfit.processor.Utils;
import com.ygmodesto.modernfit.services.HttpInfo;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * It defines all the information necessary to define how the response of an HTTP request 
 * will be returned in the generated method.
 * 
 * <p>It can be in four ways:
 * <ul>
 * <li>Object in return</li>
 * <li>RxJava2 in return</li>
 * <li>RxJava3 in return</li>
 * <li>Through a Callback that is passed as a parameter</li>
 * </ul>
 */
public class ReturnInformation {

  private Utils utils = Utils.getInstance();

  private MethodInformation methodInformation;

  private ReturnEnum returnEnum;

  private TypeMirror returnType;
  private VariableElement callback;
  private TypeMirror returnBodyType;
  
  private boolean httpInfo = false;


  private ReturnInformation(Builder builder) throws ModernfitProcessorException {

    this.methodInformation = builder.methodInformation;
    this.returnType = builder.returnType;
    this.callback = builder.callback;
    build(callback, returnType);

    validate();
  }

  public MethodInformation getMethodInformation() {
    return methodInformation;
  }

  public TypeMirror getReturnType() {
    return returnType;
  }

  public VariableElement getCallback() {
    return callback;
  }

  public TypeMirror getReturnBodyType() {
    return returnBodyType;
  }

  public boolean isSynchronos() {
    return returnEnum == ReturnEnum.OBJECT;
  }

  public boolean isAsynchronos() {
    return callback != null;
  }

  public boolean isRxJava2() {
    return returnEnum == ReturnEnum.RXJAVA2;
  }

  public boolean isRxJava3() {
    return returnEnum == ReturnEnum.RXJAVA3;
  }
  
  public boolean isHttpInfo() {
    return httpInfo;
  }

  private void build(VariableElement callback, TypeMirror returnType) {

    if (callback != null) {
      returnEnum = ReturnEnum.CALLBACK;
      returnBodyType = utils.getFirstTypeArgument(callback.asType());
    } else if (utils.isSameTypeByClassName(returnType, io.reactivex.Completable.class)) {
      returnEnum = ReturnEnum.RXJAVA2;
      returnBodyType = returnType;
    } else if (utils.isSameTypeByClassName(returnType, Completable.class)) {
      returnEnum = ReturnEnum.RXJAVA3;
      returnBodyType = returnType;
    } else if (utils.isSameGenericTypeByClassName(returnType, io.reactivex.Single.class)
        || utils.isSameGenericTypeByClassName(returnType, io.reactivex.Maybe.class)
        || utils.isSameGenericTypeByClassName(returnType, io.reactivex.Flowable.class)
        || utils.isSameGenericTypeByClassName(returnType, io.reactivex.Observable.class)) {
      returnEnum = ReturnEnum.RXJAVA2;
      returnBodyType = utils.getFirstTypeArgument(returnType);
    } else if (utils.isSameGenericTypeByClassName(returnType, Single.class)
        || utils.isSameGenericTypeByClassName(returnType, Maybe.class)
        || utils.isSameGenericTypeByClassName(returnType, Flowable.class)
        || utils.isSameGenericTypeByClassName(returnType, Observable.class)) {
      returnEnum = ReturnEnum.RXJAVA3;
      returnBodyType = utils.getFirstTypeArgument(returnType);
    } else {
      returnEnum = ReturnEnum.OBJECT;
      returnBodyType = returnType;
    }

    httpInfo = utils.isSameGenericType(returnBodyType, HttpInfo.class);
    if (httpInfo) {
      returnBodyType = utils.getFirstTypeArgument(returnBodyType);
    }
  }

  private void validate() throws ModernfitProcessorException {
    if ((returnEnum == ReturnEnum.CALLBACK) && !utils.isVoid(returnType)) {
      throw new ModernfitProcessorException(
          "Async with Callback need void in method return",
          methodInformation.getExecutableElement());
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  /** 
   * Builder class for {@link ReturnInformation ReturnInformation}. 
   */
  public static class Builder {

    private MethodInformation methodInformation;

    private TypeMirror returnType;
    private VariableElement callback;

    public Builder addMethodInformation(MethodInformation methodInformation) {
      this.methodInformation = methodInformation;
      return this;
    }

    public Builder addReturnType(TypeMirror returnType) {
      this.returnType = returnType;
      return this;
    }

    public Builder addCallback(VariableElement callback) {
      this.callback = callback;
      return this;
    }

    public ReturnInformation build() throws ModernfitProcessorException {
      return new ReturnInformation(this);
    }
  }
}
