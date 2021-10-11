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

import com.ygmodesto.modernfit.services.ResponseContent;

/**
 * A scalar converter that convert a scalar response to a primitive Java type.
 */
public class BaseResponseConverters {

  protected BaseResponseConverters() {}

  static final class VoidResponseConverter implements Converter<ResponseContent, Void> {

    static final VoidResponseConverter INSTANCE = new VoidResponseConverter();

    @Override
    public Void convert(ResponseContent src) throws ModernfitConverterException {
      return null;
    }
  }

  static final class StringResponseConverter implements Converter<ResponseContent, String> {

    static final StringResponseConverter INSTANCE = new StringResponseConverter();

    @Override
    public String convert(ResponseContent src) throws ModernfitConverterException {
      return src.getContentAsString();
    }
  }

  static final class BooleanResponseConverter implements Converter<ResponseContent, Boolean> {

    static final BooleanResponseConverter INSTANCE = new BooleanResponseConverter();

    @Override
    public Boolean convert(ResponseContent src) throws ModernfitConverterException {
      return Boolean.valueOf(src.getContentAsString());
    }
  }

  static final class ByteResponseConverter implements Converter<ResponseContent, Byte> {

    static final ByteResponseConverter INSTANCE = new ByteResponseConverter();

    @Override
    public Byte convert(ResponseContent src) throws ModernfitConverterException {
      return Byte.valueOf(src.getContentAsString());
    }
  }

  static final class CharacterResponseConverter implements Converter<ResponseContent, Character> {

    static final CharacterResponseConverter INSTANCE = new CharacterResponseConverter();

    @Override
    public Character convert(ResponseContent src) throws ModernfitConverterException {
      return Character.valueOf(src.getContentAsString().charAt(0));
    }
  }

  static final class DoubleResponseConverter implements Converter<ResponseContent, Double> {

    static final DoubleResponseConverter INSTANCE = new DoubleResponseConverter();

    public Double convert(ResponseContent src) throws ModernfitConverterException {
      return Double.valueOf(src.getContentAsString());
    }
  }

  static final class FloatResponseConverter implements Converter<ResponseContent, Float> {

    static final FloatResponseConverter INSTANCE = new FloatResponseConverter();

    @Override
    public Float convert(ResponseContent src) throws ModernfitConverterException {
      return Float.valueOf(src.getContentAsString());
    }
  }

  static final class IntegerResponseConverter implements Converter<ResponseContent, Integer> {

    static final IntegerResponseConverter INSTANCE = new IntegerResponseConverter();

    @Override
    public Integer convert(ResponseContent src) throws ModernfitConverterException {
      return Integer.valueOf(src.getContentAsString());
    }
  }

  static final class LongResponseConverter implements Converter<ResponseContent, Long> {

    static final LongResponseConverter INSTANCE = new LongResponseConverter();

    @Override
    public Long convert(ResponseContent src) throws ModernfitConverterException {
      return Long.valueOf(src.getContentAsString());
    }
  }

  static final class ShortResponseConverter implements Converter<ResponseContent, Short> {

    static final ShortResponseConverter INSTANCE = new ShortResponseConverter();

    @Override
    public Short convert(ResponseContent src) throws ModernfitConverterException {
      return Short.valueOf(src.getContentAsString());
    }
  }

}
