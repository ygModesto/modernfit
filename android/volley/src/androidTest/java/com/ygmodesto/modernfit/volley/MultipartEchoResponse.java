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

package com.ygmodesto.modernfit.volley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultipartEchoResponse {

	private final String lineEnd = "\r\n";

	private String url;
	private String method;
	private String contentType;
	private Map<String, List<String>>  parameters;
	private String queryString;
	private Map<String, List<String>> headers;
	private List<String> parts;
	
	public MultipartEchoResponse() {}

	public MultipartEchoResponse(String url, Map<String, List<String>> headers, List<String> parts) {
		super();
		this.url = url;
		this.headers = headers;
		this.parts = parts;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Map<String, List<String>> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, List<String>> parameters) {
		this.parameters = parameters;
	}
	
	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public void setParametersAsArray(Map<String, String[]> parameters) {
		if (parameters == null) { return ; }
		
		this.parameters = new HashMap<String, List<String>>();
		for(Map.Entry<String, String[]> entry : parameters.entrySet()) {
			this.parameters.put(entry.getKey(), Arrays.asList(entry.getValue()));
		}
		
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}

	public List<String> getParts() {
		return parts;
	}

	public void setParts(List<String> parts) {
		this.parts = parts;
	}

	public void setPartsAsString(String parts) {
		this.parts = new ArrayList<>();

		String[] lines = parts.split("\r\n");
		int i;
		for(i=0; i<(lines.length/5); i++){
			this.parts.add(lines[i*5+4]);
		}
	}

}
