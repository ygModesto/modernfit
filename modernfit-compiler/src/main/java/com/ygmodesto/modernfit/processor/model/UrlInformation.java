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

import com.ygmodesto.modernfit.annotations.Path;
import com.ygmodesto.modernfit.annotations.Query;
import com.ygmodesto.modernfit.annotations.QueryMap;
import com.ygmodesto.modernfit.processor.ModernfitProcessorException;
import com.ygmodesto.modernfit.processor.Utils;
import com.ygmodesto.modernfit.services.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/** Defines the information necessary to construct the url of a method. */
public class UrlInformation {

  private Utils utils = Utils.getInstance();

  private MethodInformation methodInformation;

  private String baseUrl;
  private String methodUrl;
  // TODO check if a String
  private VariableElement parameterUrl;
  private List<Segment> segmentsUrl;

  private Map<String, AnnotationInformation> paths;
  private List<AnnotationInformation> queries;
  private List<AnnotationInformation> queriesMaps;

  private UrlInformation(Builder builder) throws ModernfitProcessorException {

    this.methodInformation = builder.methodInformation;
    this.baseUrl = builder.baseUrl;
    this.methodUrl = builder.methodUrl;
    this.parameterUrl = builder.parameterUrl;

    this.paths = builder.paths;
    this.queries = builder.queries;
    this.queriesMaps = builder.queriesMaps;

    this.segmentsUrl = toSegments(this.methodUrl);

    validate();
  }

  public MethodInformation getMethodInformation() {
    return methodInformation;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public boolean containsBaseUrl() {
    return !Constants.EMPTY_STRING.equals(baseUrl);
  }

  public String getMethodUrl() {
    return methodUrl;
  }

  public boolean containsMehtodUrl() {
    return !Constants.EMPTY_STRING.equals(methodUrl);
  }

  public VariableElement getParameterUrl() {
    return parameterUrl;
  }

  public boolean containsParameterUrl() {
    return parameterUrl != null;
  }

  public List<Segment> getSegmentsUrl() {
    return segmentsUrl;
  }

  public Map<String, AnnotationInformation> getPaths() {
    return paths;
  }

  public List<AnnotationInformation> getQueries() {
    return queries;
  }

  public List<AnnotationInformation> getQueriesMaps() {
    return queriesMaps;
  }

  private List<Segment> toSegments(String url) {

    List<Segment> segments = new ArrayList<>();
    boolean constant = false;
    int i = 0;
    int lastWordEndIndex = 0;
    for (i = 0; i < url.length(); i++) {
      if ((url.charAt(i) == '{') || (url.charAt(i) == '}')) {
        if (i - lastWordEndIndex > 0) {
          segments.add(new Segment(constant, url.substring(lastWordEndIndex, i)));
          lastWordEndIndex = i + 1;
        }
        constant = !constant;
      }
    }
    if (i - lastWordEndIndex > 0) {
      segments.add(new Segment(constant, url.substring(lastWordEndIndex, i)));
    }

    return segments;
  }

  private void validate() throws ModernfitProcessorException {

    if (isBlank(baseUrl) && isBlank(methodUrl) && (parameterUrl == null)) {
      throw new ModernfitProcessorException(
          "Url not found", methodInformation.getExecutableElement());
    }

    int i = 0;
    for (Segment segment : segmentsUrl) {
      if (segment.isExpression()) {
        i++;
        if (!paths.containsKey(segment.getValue())) {
          throw new ModernfitProcessorException(
              String.format("@Path for {%s} not found", segment.getValue()),
              methodInformation.getExecutableElement());
        }
      }
    }

    if (i != paths.size()) {
      throw new ModernfitProcessorException(
          "The number of expressions in the url does not match with the number of @Path",
          methodInformation.getExecutableElement());
    }

    if (!queriesMaps.isEmpty()) {
      for (AnnotationInformation queryMap : queriesMaps) {
        TypeMirror asType = queryMap.getVariableElement().asType();
        if (!utils.isSameGenericType(asType, Map.class)
            || !utils.isSubtype(utils.getFirstTypeArgument(asType), String.class)) {
          throw new ModernfitProcessorException(
              "@QueryMap must be a Map with a String key", queryMap.getVariableElement());
        }
      }
    }
  }

  private boolean isBlank(String value) {
    return ((value == null) || (value.trim().isEmpty()));
  }

  public static Builder builder() {
    return new Builder();
  }

  /** 
   * Builder class for {@link UrlInformation UrlInformation}. 
   */
  public static final class Builder {

    private MethodInformation methodInformation;

    private String baseUrl;
    private String methodUrl;

    private VariableElement parameterUrl;

    private Map<String, AnnotationInformation> paths = new HashMap<>();
    private List<AnnotationInformation> queries = new ArrayList<>();
    private List<AnnotationInformation> queriesMaps = new ArrayList<>();

    public Builder addMethodInformation(MethodInformation methodInformation) {
      this.methodInformation = methodInformation;
      return this;
    }

    public Builder addBaseUrl(String baseUrl) {
      this.baseUrl = baseUrl.trim();
      return this;
    }

    public Builder addMethodUrl(String methodUrl) {
      this.methodUrl = methodUrl.trim();
      return this;
    }

    public Builder addParameterUrl(VariableElement parameterUrl) {
      this.parameterUrl = parameterUrl;
      return this;
    }

    /** Add to the builder a VariableElement that represents a Path variable. */
    public Builder addPath(VariableElement variableElement) {

      Path path = variableElement.getAnnotation(Path.class);

      return addPath(path, variableElement);
    }

    /**
     * Add to the builder a Path variable via its annotation and the VariableElement 
     * that it annotates.
     */
    public Builder addPath(Path path, VariableElement variableElement) {

      String pathName =
          (path.value().equals(Constants.EMPTY_STRING))
          ? variableElement.getSimpleName().toString()
              : path.value();

      this.paths.put(
          pathName, new AnnotationInformation(pathName, path.encoded(), variableElement));
      return this;
    }

    /**
     * Add to the builder a list of Path variables via a VariableElement list.
     */
    public Builder addPaths(List<VariableElement> paths) {

      if (paths == null) {
        return this;
      }

      for (VariableElement variableElement : paths) {
        addPath(variableElement);
      }
      return this;
    }

    /**
     * Add to the builder a variable that will be used to build a query param.
     */
    public Builder addQuery(VariableElement variableElement) {

      Query query = variableElement.getAnnotation(Query.class);

      return addQuery(query, variableElement);
    }

    /**
     * Add to the builder a variable that will be used to build a query param.
     */
    public Builder addQuery(Query query, VariableElement variableElement) {

      String queryName =
          (query.value().equals(Constants.EMPTY_STRING))
          ? variableElement.getSimpleName().toString()
              : query.value();

      this.queries.add(new AnnotationInformation(queryName, query.encoded(), variableElement));
      return this;
    }

    /**
     * Add to the builder a list of variables that will be used to build query params.
     */
    public Builder addQueries(List<VariableElement> queries) {

      if (queries == null) {
        return this;
      }

      for (VariableElement variableElement : queries) {
        addQuery(variableElement);
      }
      return this;
    }

    public Builder addQueryMap(QueryMap queryMap, VariableElement variableElement) {
      this.queriesMaps.add(new AnnotationInformation(queryMap.encoded(), variableElement));
      return this;
    }

    public UrlInformation build() throws ModernfitProcessorException {

      return new UrlInformation(this);
    }
  }

  /**
   * The relative URL indicated in each method is divided into segments, the constant parts and the
   * variable path parts are separated. Example: {@code @GET("/user/{id}/info")}.
   *
   * <p>It will be divided into 3 segments.
   * <ul>
   * <li>/user/</li>
   * <li>id</li>
   * <li>/info</li>
   * </ul>
   */
  public static class Segment {

    private boolean expression;
    private String value;

    public Segment(boolean expression, String value) {
      this.expression = expression;
      this.value = value;
    }
    
    public boolean isExpression() {
      return expression;
    }
    
    public String getValue() {
      return value;
    }

  }
}
