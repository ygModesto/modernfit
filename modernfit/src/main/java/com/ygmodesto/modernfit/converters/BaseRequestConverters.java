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

import com.ygmodesto.modernfit.services.BodyContent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A scalar converter that produces text/plain BodyContent.
 */
public class BaseRequestConverters {

  private static final String MEDIA_TYPE = "text/plain";
  private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

  protected BaseRequestConverters() {}

  static final class VoidRequestConverter implements Converter<Void, BodyContent> {

    static final VoidRequestConverter INSTANCE = new VoidRequestConverter();

    @Override
    public BodyContent convert(Void src) throws ModernfitConverterException {
      return new BodyContent(MEDIA_TYPE, DEFAULT_CHARSET, null);
    }
  }

  static final class StringRequestConverter implements Converter<String, BodyContent> {

    static final StringRequestConverter INSTANCE = new StringRequestConverter();

    @Override
    public BodyContent convert(String src) throws ModernfitConverterException {
      return new BodyContent(MEDIA_TYPE, DEFAULT_CHARSET, src.getBytes());
    }
  }

  static final class BooleanRequestConverter implements Converter<Boolean, BodyContent> {

    static final BooleanRequestConverter INSTANCE = new BooleanRequestConverter();

    @Override
    public BodyContent convert(Boolean src) throws ModernfitConverterException {
      return new BodyContent(
          MEDIA_TYPE, DEFAULT_CHARSET, String.valueOf(src).getBytes(DEFAULT_CHARSET));
    }
  }

  static final class ByteRequestConverter implements Converter<Byte, BodyContent> {

    static final ByteRequestConverter INSTANCE = new ByteRequestConverter();

    @Override
    public BodyContent convert(Byte src) throws ModernfitConverterException {
      return new BodyContent(
          MEDIA_TYPE, DEFAULT_CHARSET, String.valueOf(src).getBytes(DEFAULT_CHARSET));
    }
  }

  static final class CharacterRequestConverter implements Converter<Character, BodyContent> {

    static final CharacterRequestConverter INSTANCE = new CharacterRequestConverter();

    @Override
    public BodyContent convert(Character src) throws ModernfitConverterException {
      return new BodyContent(
          MEDIA_TYPE, DEFAULT_CHARSET, String.valueOf(src).getBytes(DEFAULT_CHARSET));
    }
  }

  static final class DoubleRequestConverter implements Converter<Double, BodyContent> {

    static final DoubleRequestConverter INSTANCE = new DoubleRequestConverter();

    public BodyContent convert(Double src) throws ModernfitConverterException {
      return new BodyContent(
          MEDIA_TYPE, DEFAULT_CHARSET, String.valueOf(src).getBytes(DEFAULT_CHARSET));
    }
  }

  static final class FloatRequestConverter implements Converter<Float, BodyContent> {

    static final FloatRequestConverter INSTANCE = new FloatRequestConverter();

    @Override
    public BodyContent convert(Float src) throws ModernfitConverterException {
      return new BodyContent(
          MEDIA_TYPE, DEFAULT_CHARSET, String.valueOf(src).getBytes(DEFAULT_CHARSET));
    }
  }

  static final class IntegerRequestConverter implements Converter<Integer, BodyContent> {

    static final IntegerRequestConverter INSTANCE = new IntegerRequestConverter();

    @Override
    public BodyContent convert(Integer src) throws ModernfitConverterException {
      return new BodyContent(
          MEDIA_TYPE, DEFAULT_CHARSET, String.valueOf(src).getBytes(DEFAULT_CHARSET));
    }
  }

  static final class LongRequestConverter implements Converter<Long, BodyContent> {

    static final LongRequestConverter INSTANCE = new LongRequestConverter();

    @Override
    public BodyContent convert(Long src) throws ModernfitConverterException {
      return new BodyContent(
          MEDIA_TYPE, DEFAULT_CHARSET, String.valueOf(src).getBytes(DEFAULT_CHARSET));
    }
  }

  static final class ShortRequestConverter implements Converter<Short, BodyContent> {

    static final ShortRequestConverter INSTANCE = new ShortRequestConverter();

    @Override
    public BodyContent convert(Short src) throws ModernfitConverterException {
      return new BodyContent(
          MEDIA_TYPE, DEFAULT_CHARSET, String.valueOf(src).getBytes(DEFAULT_CHARSET));
    }
  }

  static final class FileRequestConverter implements Converter<File, BodyContent> {

    static final FileRequestConverter INSTANCE = new FileRequestConverter();

    @Override
    public BodyContent convert(File src) throws ModernfitConverterException {

      try {

        Path path = src.toPath();

        return new BodyContent(Files.probeContentType(path), Files.readAllBytes(path));
      } catch (IOException e) {
        throw new ModernfitConverterException(e);
      }
    }
  }
}
