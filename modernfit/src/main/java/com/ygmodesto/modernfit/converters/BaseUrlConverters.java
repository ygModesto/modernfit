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

package com.ygmodesto.modernfit.converters;


/**
 * A scalar converter that produces String.
 */
public class BaseUrlConverters {


  protected BaseUrlConverters() {}

  static final class VoidUrlConverter implements Converter<Void, String> {

    static final VoidUrlConverter INSTANCE = new VoidUrlConverter();

    @Override
    public String convert(Void src) throws ModernfitConverterException {
      return "";
    }
  }

  static final class StringUrlConverter implements Converter<String, String> {

    static final StringUrlConverter INSTANCE = new StringUrlConverter();

    @Override
    public String convert(String src) throws ModernfitConverterException {
      return src;
    }
  }

  static final class BooleanUrlConverter implements Converter<Boolean, String> {

    static final BooleanUrlConverter INSTANCE = new BooleanUrlConverter();

    @Override
    public String convert(Boolean src) throws ModernfitConverterException {
      return src.toString();
    }
  }

  static final class ByteUrlConverter implements Converter<Byte, String> {

    static final ByteUrlConverter INSTANCE = new ByteUrlConverter();

    @Override
    public String convert(Byte src) throws ModernfitConverterException {
      return String.valueOf(src);
    }
  }

  static final class CharacterUrlConverter implements Converter<Character, String> {

    static final CharacterUrlConverter INSTANCE = new CharacterUrlConverter();

    @Override
    public String convert(Character src) throws ModernfitConverterException {
      return String.valueOf(src);
    }
  }

  static final class DoubleUrlConverter implements Converter<Double, String> {

    static final DoubleUrlConverter INSTANCE = new DoubleUrlConverter();

    public String convert(Double src) throws ModernfitConverterException {
      return String.valueOf(src);
    }
  }

  static final class FloatUrlConverter implements Converter<Float, String> {

    static final FloatUrlConverter INSTANCE = new FloatUrlConverter();

    @Override
    public String convert(Float src) throws ModernfitConverterException {
      return String.valueOf(src);
    }
  }

  static final class IntegerUrlConverter implements Converter<Integer, String> {

    static final IntegerUrlConverter INSTANCE = new IntegerUrlConverter();

    @Override
    public String convert(Integer src) throws ModernfitConverterException {
      return String.valueOf(src);
    }
  }

  static final class LongUrlConverter implements Converter<Long, String> {

    static final LongUrlConverter INSTANCE = new LongUrlConverter();

    @Override
    public String convert(Long src) throws ModernfitConverterException {
      return String.valueOf(src);
    }
  }

  static final class ShortUrlConverter implements Converter<Short, String> {

    static final ShortUrlConverter INSTANCE = new ShortUrlConverter();

    @Override
    public String convert(Short src) throws ModernfitConverterException {
      return String.valueOf(src);
    }
  }

  static final class ObjectUrlConverter<T> implements Converter<T, String> {

    @Override
    public String convert(T src) throws ModernfitConverterException {
      return String.valueOf(src);
    }
  }
  
}
