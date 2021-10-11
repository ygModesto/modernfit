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

import java.util.List;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Annotations processing utility class.
 */
public class Utils {

  private static Utils INSTANCE;

  private static Types types;
  private static Elements elements;

  /**
   * Returns a single Utils instance from {@code Types} and {@code Elements}. 
   */
  public static Utils getInstance(Types types, Elements elements) {
    if (INSTANCE == null) {
      INSTANCE = new Utils();
    }

    Utils.types = types;
    Utils.elements = elements;

    return INSTANCE;
  }

  /**
   * Returns the already created instance of Utils.
   */
  public static Utils getInstance() {
    if (INSTANCE == null) {
      throw new IllegalStateException("first call getInstance(Types types, Elements elements)");
    }

    return INSTANCE;
  }

  private Utils() {}

  public Types getTypes() {
    return types;
  }

  public Elements getElements() {
    return elements;
  }

  public TypeElement boxedType(TypeMirror typeMirror) {

    return types.boxedClass(types.unboxedType(typeMirror));
  }

  public boolean isVoid(TypeMirror typeMirror) {
    return types.getNoType(TypeKind.VOID).equals(typeMirror);
  }

  /**
   * Given a TypeMirror and a Class it returns if they are the same types or not.
   */
  public boolean isSameType(TypeMirror typeMirror, Class<?> clazz) {

    if ((typeMirror == null) || (clazz == null)) {
      return false;
    }

    TypeElement typeElement = elements.getTypeElement(clazz.getCanonicalName());
    DeclaredType declaredType = types.getDeclaredType(typeElement);

    return types.isSameType(typeMirror, declaredType);
  }
  
  /**
   * Given a TypeMirror and a Class it returns if they are the same types or not.
   * Compare from the class name and package to both instead of using
   * the classes elements and types.
   *
   * <p>{@link #isSameType (TypeMirror typeMirror, Class <?> clazz)} causes an exception
   * if clazz is not in the classpath of the project using modernfit-compiler.
   * 
   */
  public boolean isSameTypeByClassName(TypeMirror typeMirror, Class<?> clazz) {

    if ((typeMirror == null) || (clazz == null)) {
      return false;
    }
    
    // System.out.println("typeMirror.toString = " + typeMirror.toString());
    // System.out.println("clazz.getCanonicalName = " + clazz.getCanonicalName());
    // System.out.println("clazz.getTypeName() = " + clazz.getTypeName());
    // System.out.println("clazz.getName() = " + clazz.getName());
    // System.out.println("clazz.getSimpleName() = " + clazz.getSimpleName());
    // System.out.println("typeMirror.getKind " + typeMirror.getKind());
    
    return typeMirror.toString().equals(clazz.getCanonicalName());
  }

  /**
   * Given a TypeMirror and a list of Class returns if any of the clazzs 
   * is of the same class type as TypeMirror.
   */
  public boolean isAnyType(TypeMirror typeMirror, Class<?>... clazzs) {

    if ((typeMirror == null) || (clazzs == null)) {
      return false;
    }

    for (Class<?> clazz : clazzs) {
      TypeElement typeElement = elements.getTypeElement(clazz.getCanonicalName());
      DeclaredType declaredType = types.getDeclaredType(typeElement);
      if (types.isSameType(typeMirror, declaredType)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Given a TypeMirror and a Class it returns if they are the same type or not.
   * In case clazz is an array of classes, first build a parameterized type 
   * and check if this and typeMirror are the same.
   * 
   * <p>Example:
   *
   * <pre><code>
   * isSameTypeDeep(typeMirror, Set.class, String.class);
   * </code></pre>
   * check if {@code typeMirror} is same type as {@code Set<String>}.
   * 
   */
  public boolean isSameTypeDeep(TypeMirror typeMirror, Class<?>... clazz) {
    if ((typeMirror == null) || (clazz == null) || (clazz.length <= 0) || (clazz[0] == null)) {
      return false;
    }

    TypeElement typeElement = elements.getTypeElement(clazz[0].getCanonicalName());

    TypeMirror[] typeMirrors = new TypeMirror[clazz.length - 1];
    for (int i = 0; i < clazz.length - 1; i++) {
      typeMirrors[i] =
          types.getDeclaredType(elements.getTypeElement(clazz[i + 1].getCanonicalName()));
    }

    DeclaredType declaredType = types.getDeclaredType(typeElement, typeMirrors);

    return types.isSameType(typeMirror, declaredType);
  }


  /**
   * Given a TypeMirror and a Class it returns if clazz is a subtype of typeMirror.
   * In case clazz is an array of classes, first build a parameterized type 
   * and check if this is a subtype of typeMirror.
   * Any type is considered to be a subtype of itself.
   * 
   * <p>Example:
   *
   * <pre><code>
   * isAllSubtypes(typeMirror, Set.class, String.class);
   * </code></pre>
   * check if {@code typeMirror} is subtype of {@code Set<String>}.
   * 
   */
  public boolean isAllSubtypes(TypeMirror typeMirror, Class<?>... clazz) {

    if ((typeMirror == null) || (clazz == null) || (clazz.length <= 0) || (clazz[0] == null)) {
      return false;
    }

    TypeElement typeElement = elements.getTypeElement(clazz[0].getCanonicalName());

    TypeMirror[] typeMirrors = new TypeMirror[clazz.length - 1];
    for (int i = 0; i < clazz.length - 1; i++) {
      typeMirrors[i] =
          types.getWildcardType(
              types.getDeclaredType(elements.getTypeElement(clazz[i + 1].getCanonicalName())),
              null);
    }

    DeclaredType declaredType = types.getDeclaredType(typeElement, typeMirrors);

    return types.isSubtype(typeMirror, declaredType);
  }

  /**
   * Determines if an array of clazz is a subtype of typeMirror.
   */
  public boolean isSubtypeArray(TypeMirror typeMirror, Class<?> clazz) {

    if ((typeMirror == null) || (clazz == null)) {
      return false;
    }

    ArrayType arrayType =
        types.getArrayType(
            types.getDeclaredType(elements.getTypeElement(clazz.getCanonicalName())));

    return types.isSubtype(typeMirror, arrayType);
  }

  /**
   * Determines if typeMirror is of type array.
   */
  public boolean isArrayType(TypeMirror typeMirror) {

    return typeMirror.getKind() == TypeKind.ARRAY;
  }

  /**
   * Determines if typeMirror and clazz are the same generic type. 
   */
  public boolean isSameGenericType(TypeMirror typeMirror, Class<?> clazz) {

    if (typeMirror == null) {
      return false;
    }

    return isSameType(types.erasure(typeMirror), clazz);
  }

  /**
   * Determines if typeMirror and clazz are the same generic type.
   * Compare using {@link #isSameTypeByClassName}
   */
  public boolean isSameGenericTypeByClassName(TypeMirror typeMirror, Class<?> clazz) {
    // System.out.println("isSameGenericTypeByClassName");
    // System.out.println("typeMirror.toString = " + typeMirror.toString());
    // System.out.println("clazz.getCanonicalName = " + clazz.getCanonicalName());
    // System.out.println("clazz.getTypeName() = " + clazz.getTypeName());
    // System.out.println("clazz.getName() = " + clazz.getName());
    // System.out.println("clazz.getSimpleName() = " + clazz.getSimpleName());
    // System.out.println("typeMirror.getKind " + typeMirror.getKind());

    return isSameTypeByClassName(types.erasure(typeMirror), clazz);
  }

  /**
   * Determines if clazz is a subtype of typeMirror.
   * Any type is considered to be a subtype of itself.
   */
  public boolean isSubtype(TypeMirror typeMirror, Class<?> clazz) {

    String canonicalName = clazz.getCanonicalName();
    TypeElement typeElement = elements.getTypeElement(canonicalName);
    DeclaredType declaredType = types.getDeclaredType(typeElement);

    return types.isSubtype(typeMirror, declaredType);
  }

  /**
   * Returns the erasure of a type.
   *
   * @param typeMirror the type to be erased.
   * @returnthe type to be erased.
   */
  public TypeMirror getErasureType(TypeMirror typeMirror) {

    return types.erasure(typeMirror);
  }

  /**
   * Determines if the type of clazz is assignable to the type defined by typeMirror.
   */
  public boolean isAssignable(TypeMirror typeMirror, Class<?> clazz) throws ClassNotFoundException {

    // TODO darle una vuelta teniendo en cuenta arrays
    String erasure = types.erasure(typeMirror).toString();
    if (erasure.charAt(erasure.length() - 1) == ']') {
      erasure = erasure.substring(0, erasure.length() - 2);
    }

    Class<?> className = Class.forName(erasure);
    return clazz.isAssignableFrom(className);
  }

  /**
   * Returns the component type of this array type.

   * @return the component type of this array type.
   */
  public TypeMirror getTypeOfArray(TypeMirror typeMirror) {

    ArrayType arrayType = ArrayType.class.cast(typeMirror);

    return arrayType.getComponentType();
  }

  /**
   * Returns the first type argument of typeMirror.
   *
   * @return the first type argument.
   */
  public TypeMirror getFirstTypeArgument(TypeMirror typeMirror) {

    DeclaredType type = (DeclaredType) typeMirror;
    List<? extends TypeMirror> typeArguments = type.getTypeArguments();

    if ((typeArguments != null) && !typeArguments.isEmpty()) {
      return typeArguments.get(0);
    } else {
      return null;
    }
  }

  /**
   * Returns the second type argument of typeMirror.
   *
   * @return the second type argument.
   */
  public TypeMirror getSecondTypeArgument(TypeMirror typeMirror) {

    DeclaredType type = DeclaredType.class.cast(typeMirror);
    List<? extends TypeMirror> typeArguments = type.getTypeArguments();

    if ((typeArguments != null) && !typeArguments.isEmpty()) {
      return typeArguments.get(1);
    } else {
      return null;
    }
  }
}
