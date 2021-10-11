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

package com.ygmodesto.modernfit.processor.errors;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import com.ygmodesto.modernfit.processor.ModernfitProcessor;

@RunWith(JUnit4.class)
public class ExceptionsTest {

	private static final String TEMPLATE = "package com.ygmodesto.modernfit.processor.repository.exceptions;\n" + 
			"\n" + 
			"import com.ygmodesto.modernfit.annotations.Field;\n" +
			"import com.ygmodesto.modernfit.annotations.FieldMap;\n" +
			"import com.ygmodesto.modernfit.annotations.FormUrlEncoded;\n" +
			"import com.ygmodesto.modernfit.annotations.Headers;\n" +
			"import com.ygmodesto.modernfit.annotations.Part;\n" +
			"import com.ygmodesto.modernfit.annotations.PartMap;\n" +
            "import com.ygmodesto.modernfit.annotations.QueryMap;\n" +
			"import com.ygmodesto.modernfit.annotations.Body;\n" +
			"import com.ygmodesto.modernfit.annotations.GET;\n" + 
			"import com.ygmodesto.modernfit.annotations.POST;\n" + 
			"import com.ygmodesto.modernfit.annotations.PUT;\n" + 
			"import com.ygmodesto.modernfit.annotations.Modernfit;\n" +
			"import com.ygmodesto.modernfit.annotations.Multipart;\n" + 
			"import com.ygmodesto.modernfit.annotations.Path;\n" + 
			"import com.ygmodesto.modernfit.annotations.Url;\n" + 
			"import com.ygmodesto.modernfit.processor.server.ModelTO;\n" +
			"import com.ygmodesto.modernfit.converters.JacksonConverterFactory;\n" +
			"import com.ygmodesto.modernfit.services.ResponseCallback;\n" +
			"import io.reactivex.Single;\n"+
			"import java.util.Collection;\n"+
            "import java.util.Map;\n"
			+ "\n"
			+ "@Modernfit(converterFactory = JacksonConverterFactory.class)\n" + 
			"public interface ExceptionRepository {\n" + 
			"	\n"+ 
			"	%s \n" +  
			"}";

	protected static JavaCompiler javacs = ToolProvider.getSystemJavaCompiler();
	protected static StandardJavaFileManager fileManager = javacs.getStandardFileManager(null, null, null);

	@BeforeClass
	public static void setUp() throws IOException {
		fileManager.setLocation(StandardLocation.SOURCE_PATH, Collections.singleton(new File("src"+File.separator+"test"+File.separator+"java"+File.separator)));
	}

    
    @Test
    public void queryMapWihtoutStringKeysMethodTest() throws IOException {

        hadErrorContainingMatch( 
                "@GET(\"http://localhost:8080/api\") \n" +
                "String getValue(@QueryMap Map<Long,String> queryMap);",
                "@QueryMap must be a Map with a String key");
    }
	
	@Test
	public void multipartAndFormUrlEncodedAnntationsTest() throws IOException {

		hadErrorContainingMatch( 
				"@FormUrlEncoded @Multipart @GET(\"http://localhost:8080/api\") \n" + 
				"String getValue();",
				"@FormUlrEconded and @Multipart not supported together");

	}

	@Test
	public void fieldInDiscreteMethodTest() throws IOException {

		hadErrorContainingMatch( 
				"@GET(\"http://localhost:8080/api\") \n" + 
				"String getValue(@Field String field);",
				"@Field only allowed in @FormUrlEncoded method");

	}

	@Test
	public void fieldMapInDiscreteMethodTest() throws IOException {

		hadErrorContainingMatch( 
				"@GET(\"http://localhost:8080/api\") \n" +
				"String getValue(@FieldMap Map<String, String> fieldMap);",
				"@FieldMap only allowed in @FormUrlEncoded method");
	}
	
    @Test
    public void fieldMapWihtoutStringKeysMethodTest() throws IOException {

        hadErrorContainingMatch( 
                "@FormUrlEncoded @GET(\"http://localhost:8080/api\") \n" +
                "String getValue(@FieldMap Map<Long,String> fieldMap);",
                "@FieldMap must be a Map with a String key");
    }

	@Test
	public void partInDiscreteMethodTest() throws IOException {

		hadErrorContainingMatch(				
				"@GET(\"http://localhost:8080/api\") \n" +
				"String getValue(@Part String part);",
				"@Part only allowed in @Multipart method");
	}

	@Test
	public void partMapInDiscreteMethodTest() throws IOException {

		hadErrorContainingMatch(				
				"@GET(\"http://localhost:8080/api\") \n" +
				"String getValue(@PartMap Map<String, String> partMap);",
				"@PartMap only allowed in @Multipart method");
	}
    
    @Test
    public void partMapWihtoutStringKeysMethodTest() throws IOException {

        hadErrorContainingMatch( 
                "@Multipart @GET(\"http://localhost:8080/api\") \n" +
                "String getValue(@PartMap Map<Long,String> fieldMap);",
                "@PartMap must be a Map with a String key");
    }

	@Test
	public void bodyInFormUrlEncodedMethodTest() throws IOException {

		hadErrorContainingMatch(			
				"@FormUrlEncoded @GET(\"http://localhost:8080/api\") \n" + 
				"String getValue(@Body String body);",
				"@Body not allowed in @FormUrlEncoded method");
	}



	@Test
	public void partInFormUrlEncodedMethodTest() throws IOException {

		hadErrorContainingMatch(			
				"@FormUrlEncoded @GET(\"http://localhost:8080/api\") \n" +
				"String getValue(@Part String part);",
				"@Part only allowed in @Multipart method");
	}

	@Test
	public void partMapInFormUrlEncodedMethodTest() throws IOException {

		hadErrorContainingMatch(			
				"@FormUrlEncoded @GET(\"http://localhost:8080/api\") \n" + 
				"String getValue(@PartMap Map<String, String> partMap);",
				"@PartMap only allowed in @Multipart method");
	}



	@Test
	public void bodyInMultipartMethodTest() throws IOException {

		hadErrorContainingMatch(			
				"@Multipart @GET(\"http://localhost:8080/api\") \n" + 
				"String getValue(@Body String body);",
				"@Body not allowed in @Multipart method");
	}



	@Test
	public void fieldInMultipartMethodTest() throws IOException {

		hadErrorContainingMatch(			
				"@Multipart @GET(\"http://localhost:8080/api\") \n" + 
				"String getValue(@Field String field);",
				"@Field only allowed in @FormUrlEncoded method");
	}

	@Test
	public void fieldMapInMultipartMethodTest() throws IOException {

		hadErrorContainingMatch(			
				"@Multipart @GET(\"http://localhost:8080/api\") \n" + 
				"String getValue(@FieldMap Map<String, String> fieldMap);",
				"@FieldMap only allowed in @FormUrlEncoded method");
	}




	@Test
	public void callbackParamInSyncMethodTest() throws IOException {

		hadErrorContainingMatch(			
				"@GET(\"http://localhost:8080/api\") \n" +
				"String getValue(ResponseCallback<String> reponseCallback);",
				"Async with Callback need void in method return");
	}

	@Test
    public void pathNotFoundURLTest() throws IOException {

        hadErrorContainingMatch(            
                "@GET(\"http://localhost:8080/api/{id}\") \n" +
                "String getValue(@Path Long idc);",
                "@Path for {id} not found");
    }
	
	@Test
    public void pathsAndExpressionNotMachTest() throws IOException {

        hadErrorContainingMatch(            
                "@GET(\"http://localhost:8080/api/{id}\") \n" +
                "String getValue(@Path Long id, @Path String tag);",
                "The number of expressions in the url does not match with the number of @Path");
    }

	
	@Test
	public void urlNotFoundMethodTest() throws IOException {

		hadErrorContainingMatch(			
				"@GET \n" +
				"String getValue();",
				"Url not found");
	}
	
	
	@Test
	public void urlNotFoundSpacesAsValueTest() throws IOException {

		hadErrorContainingMatch(			
				"@GET(\"  \") \n" +
				"String getValue();",
				"Url not found");
	}
	

	
	
	@Test
	public void noAnnotationsTest() throws IOException {

		hadErrorContainingMatch(			
				"\n" +
				"String getValue();",
				"Method not annotated");
	}
	

	
	
	@Test
	public void malformedHeadersInHeadersAnnotationTest() throws IOException {

		hadErrorContainingMatch(			
				"@Headers(\"X-AUTH-TOKEN ; token \") @GET(\"http://localhost:8080/api\")\n" +
				"String getValue();",
				"Malformed headers in @Headers annotation");
	}
	
	
	@Test
	public void getAndPostMethodAnnotationTest() throws IOException {

		hadErrorContainingMatch(			
				"@POST(\"http://localhost:8080/api\") @GET(\"http://localhost:8080/api\")\n" +
				"String getValue();",
				"@POST and @GET founds, method can only contains one");
	}
	
	@Test
	public void putAndPostMethodAnnotationTest() throws IOException {

		hadErrorContainingMatch(			
				"@PUT(\"http://localhost:8080/api\") @POST(\"http://localhost:8080/api\")\n" +
				"String getValue();",
				"@PUT and @POST founds, method can only contains one");
	}
	
	
	
//	@Test
//	public void debugTest() throws IOException {
//
//		hadErrorContainingMatch(			
//				"@POST(\"http://localhost:8080/api\")\n" +
//				"String getValue(@Body Collection<ModelTO> body);",
//				"@PUT and @POST founds, method can only contains one");
//	}
	
	


	private void hadErrorContainingMatch(String methodAsString, String expectedMessage) throws IOException {

		JavaFileObject interfaceJavaFileObject = JavaFileObjects.forSourceLines("ExceptionRepository", String.format(TEMPLATE, 
				methodAsString));

		Compilation compilation = 
				javac()
				.withProcessors(new ModernfitProcessor())
				.compile(interfaceJavaFileObject);

		assertThat(compilation)
			.hadErrorContaining(expectedMessage)
			.inFile(interfaceJavaFileObject)
			.onLine(29L);

	}

}
