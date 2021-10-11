/*
 * Copyright 2020 Yago Modesto González Diéguez
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ygmodesto.modernfit.converters;

import com.ygmodesto.modernfit.services.BodyContent;
import com.ygmodesto.modernfit.services.CustomType;
import com.ygmodesto.modernfit.services.ResponseContent;
import java.io.File;

/**
 * An abstract {@linkplain Converter.Factory converter} to create converters that convert scalar
 * objects to text/plain body request and body responses to scalar objects.
 */
public abstract class BaseConverterFactory implements Converter.Factory {

  public Converter<Void, BodyContent> getRequestConverter(Void zombie,
      CustomType<Void> customType) {

    return BaseRequestConverters.VoidRequestConverter.INSTANCE;
  }

  public Converter<String, BodyContent> getRequestConverter(String zombie,
      CustomType<String> customType) {

    return BaseRequestConverters.StringRequestConverter.INSTANCE;
  }

  public Converter<Boolean, BodyContent> getRequestConverter(Boolean zombie,
      CustomType<Boolean> customType) {

    return BaseRequestConverters.BooleanRequestConverter.INSTANCE;
  }

  public Converter<Byte, BodyContent> getRequestConverter(Byte zombie,
      CustomType<Byte> customType) {

    return BaseRequestConverters.ByteRequestConverter.INSTANCE;
  }

  public Converter<Character, BodyContent> getRequestConverter(Character zombie,
      CustomType<Character> customType) {

    return BaseRequestConverters.CharacterRequestConverter.INSTANCE;
  }

  public Converter<Double, BodyContent> getRequestConverter(Double zombie,
      CustomType<Double> customType) {

    return BaseRequestConverters.DoubleRequestConverter.INSTANCE;
  }

  public Converter<Float, BodyContent> getRequestConverter(Float zombie,
      CustomType<Float> customType) {

    return BaseRequestConverters.FloatRequestConverter.INSTANCE;
  }

  public Converter<Integer, BodyContent> getRequestConverter(Integer zombie,
      CustomType<Integer> customType) {

    return BaseRequestConverters.IntegerRequestConverter.INSTANCE;
  }

  public Converter<Long, BodyContent> getRequestConverter(Long zombie,
      CustomType<Long> customType) {

    return BaseRequestConverters.LongRequestConverter.INSTANCE;
  }

  public Converter<Short, BodyContent> getRequestConverter(Short zombie,
      CustomType<Short> customType) {

    return BaseRequestConverters.ShortRequestConverter.INSTANCE;
  }

  public Converter<File, BodyContent> getRequestConverter(File zombie,
      CustomType<File> customType) {

    return BaseRequestConverters.FileRequestConverter.INSTANCE;
  }

  public Converter<ResponseContent, Void> getResponseConverter(Void zombie,
      CustomType<Void> customType) {

    return BaseResponseConverters.VoidResponseConverter.INSTANCE;
  }

  public Converter<ResponseContent, String> getResponseConverter(String zombie,
      CustomType<String> customType) {

    return BaseResponseConverters.StringResponseConverter.INSTANCE;
  }

  public Converter<ResponseContent, Boolean> getResponseConverter(Boolean zombie,
      CustomType<Boolean> customType) {

    return BaseResponseConverters.BooleanResponseConverter.INSTANCE;
  }

  public Converter<ResponseContent, Byte> getResponseConverter(Byte zombie,
      CustomType<Byte> customType) {

    return BaseResponseConverters.ByteResponseConverter.INSTANCE;
  }

  public Converter<ResponseContent, Character> getResponseConverter(Character zombie,
      CustomType<Character> customType) {

    return BaseResponseConverters.CharacterResponseConverter.INSTANCE;
  }

  public Converter<ResponseContent, Double> getResponseConverter(Double zombie,
      CustomType<Double> customType) {

    return BaseResponseConverters.DoubleResponseConverter.INSTANCE;
  }

  public Converter<ResponseContent, Float> getResponseConverter(Float zombie,
      CustomType<Float> customType) {

    return BaseResponseConverters.FloatResponseConverter.INSTANCE;
  }

  public Converter<ResponseContent, Integer> getResponseConverter(Integer zombie,
      CustomType<Integer> customType) {

    return BaseResponseConverters.IntegerResponseConverter.INSTANCE;
  }

  public Converter<ResponseContent, Long> getResponseConverter(Long zombie,
      CustomType<Long> customType) {

    return BaseResponseConverters.LongResponseConverter.INSTANCE;
  }

  public Converter<ResponseContent, Short> getResponseConverter(Short zombie,
      CustomType<Short> customType) {

    return BaseResponseConverters.ShortResponseConverter.INSTANCE;
  }
  
  public Converter<Void, String> getUrlConverter(Void zombie, CustomType<Void> customType) {

    return BaseUrlConverters.VoidUrlConverter.INSTANCE;
  }

  public Converter<String, String> getUrlConverter(String zombie, CustomType<String> customType) {

    return BaseUrlConverters.StringUrlConverter.INSTANCE;
  }

  public Converter<Boolean, String> getUrlConverter(Boolean zombie,
      CustomType<Boolean> customType) {

    return BaseUrlConverters.BooleanUrlConverter.INSTANCE;
  }

  public Converter<Byte, String> getUrlConverter(Byte zombie, CustomType<Byte> customType) {

    return BaseUrlConverters.ByteUrlConverter.INSTANCE;
  }

  public Converter<Character, String> getUrlConverter(Character zombie,
      CustomType<Character> customType) {

    return BaseUrlConverters.CharacterUrlConverter.INSTANCE;
  }

  public Converter<Double, String> getUrlConverter(Double zombie, CustomType<Double> customType) {

    return BaseUrlConverters.DoubleUrlConverter.INSTANCE;
  }

  public Converter<Float, String> getUrlConverter(Float zombie, CustomType<Float> customType) {

    return BaseUrlConverters.FloatUrlConverter.INSTANCE;
  }

  public Converter<Integer, String> getUrlConverter(Integer zombie,
      CustomType<Integer> customType) {

    return BaseUrlConverters.IntegerUrlConverter.INSTANCE;
  }

  public Converter<Long, String> getUrlConverter(Long zombie, CustomType<Long> customType) {

    return BaseUrlConverters.LongUrlConverter.INSTANCE;
  }

  public Converter<Short, String> getUrlConverter(Short zombie, CustomType<Short> customType) {

    return BaseUrlConverters.ShortUrlConverter.INSTANCE;
  }

  @Override
  public <T> Converter<T, String> getUrlConverter(T zombie, CustomType<T> customType) {

    return new BaseUrlConverters.ObjectUrlConverter<>();
  }
}
