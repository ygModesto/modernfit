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

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

/**
 * Represents a generic type {@code T}. 
 * To obtain the information of a generic class this class must be extended.
 *
 * <p>For example, to create a type literal for {@code List<Long>}, you can
 * create an empty anonymous inner class:
 *
 * <p>{@code CustomType<List<Long>> list = new CustomType<List<Long>>() {};}
 *
 */
public class CustomType<T> {

  private final Class<T> clazz;
  private final Type type;

  @SuppressWarnings("unchecked")
  protected CustomType() {
    Type superClass = getClass().getGenericSuperclass();
    if (superClass instanceof Class<?>) { // sanity check, should never happen
      throw new IllegalArgumentException(
          "Internal error: TypeReference constructed without actual type information");
    }

    ParameterizedType parameterizedType = (ParameterizedType) superClass;
    type = parameterizedType.getActualTypeArguments()[0];
    clazz = (Class<T>) extractRawClass(type);
  }

  /** Returns an object of Type representing the actual type argument to this type. */
  public Type getType() {
    return type;
  }

  public Class<T> getRawClass() {
    return clazz;
  }

  /**
   * Extract the Class of a Type object.
   */
  public Class<?> extractRawClass(Type type) {
    if (type instanceof Class<?>) {
      return (Class<?>) type;
    } else if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;

      Type rawType = parameterizedType.getRawType();
      return (Class<?>) rawType;

    } else if (type instanceof GenericArrayType) {
      Type componentType = ((GenericArrayType) type).getGenericComponentType();
      return Array.newInstance(extractRawClass(componentType), 0).getClass();
    } else if (type instanceof TypeVariable) {
      return Object.class;
    } else if (type instanceof WildcardType) {
      return extractRawClass(((WildcardType) type).getUpperBounds()[0]);

    } else {
      throw new IllegalArgumentException("Unexpected Type <" + type + ">");
    }
  }
}
