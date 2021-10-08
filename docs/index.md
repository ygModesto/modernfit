## Introduction

Define your interface and Modernfit implements it for you.

 * At compile time
 * Generate readable source code
 * Compile-time errors, don't wait to run
 * Use OkHttp, Volley, or one defined by you
 * Use the converter you want, Jakson, Gson or one defined by you



=== "Synchronous"

    ``` java
    @Modernfit(
      value = "https://remotehost/api", 
      converterFactory = JacksonConverterFactory.class)
    public interface SynchronousProductModernfit {
  
      @GET("/product/{barcode}")
      Product getProduct(@Path Long barcode);
  
      @PUT("/product/{barcode}")
      Product updateProduct(@Path("barcode") Long identifier, @Body String name);
  
      @POST("/product/create")
      Product createProduct(@Body Product product);
  
      @GET("/products")
      Collection<Product> listProducts();
  
      @GET("/product/{barcode}")
      HttpInfo<Product> getHttpInfoAndProduct(@Path Long barcode);
    }
    ```
=== "Callback"

    ``` java
    @Modernfit(
      value = "https://remotehost/api", 
      converterFactory = JacksonConverterFactory.class)
    public interface AsyncProductModernfit {
  
      @GET("/product/{barcode}")
      void getProduct(@Path Long barcode, ResponseCallback<Product> callback);
  
      @PUT("/product/{barcode}")
      void updateProduct(@Path("barcode") Long identifier, @Body String name, ResponseCallback<Product> callback);
  
      @POST("/product/create")
      void createProduct(@Body Product product, ResponseCallback<Product> callback);
  
      @GET("/products")
      void listProducts(ResponseCallback<Collection<Product>> callback);
  
      @GET("/product/{barcode}")
      void getHttpInfoAndProduct(@Path Long barcode, HttpResponseCallback<Product> callback);
    }
    ```
=== "RxJava"

    ``` java
    @Modernfit(
      value = "https://remotehost/api", 
      converterFactory = JacksonConverterFactory.class)
    public interface RxJava3ProductModernfit {
  
      @GET("/product/{barcode}")
      Single<Product> getProduct(@Path Long barcode);
  
      @PUT("/product/{barcode}")
      Observable<Product> updateProduct(@Path("barcode") Long identifier, @Body String name);
  
      @POST("/product/create")
      Observable<Product> createProduct(@Body Product product);
  
      @GET("/products")
      Observable<Collection<Product>> listProducts();
  
      @GET("/product/{barcode}")
      Single<HttpInfo<Product>> getHttpInfoAndProduct(@Path Long barcode);
    }
    ```

``` java
UserModernfit userModernfit = UserModernfitImpl.builder().build();
```


## Request Method
The methods of the interface must be annotated with `#!java @GET`, `#!java @POST`, `#!java @PUT`, `#!java @DELETE`, `#!java @OPTIONS` or `#!java @HEAD`. This indicates what type of HTTP request they are. With this annotation the relative url is also indicated.

``` java
    @GET("/users")
    Collection<User> getUsers();


    @PUT(value = "/updateUser/{email}")
    User getUsers(@Path String email, @Body UpdateUserTO updateUser);
```

## URL Manipulation
The URL can be modified with variables between `{ }`, each variable will be replaced by the value of the parameter of the same name annotated with `#!java @Path`, you can put a different parameter name indicating which variable it replaces through the `#!java @Path("variable")`

``` java
	@GET("/user/{id}")
	User getUser(@Path("id") int userId);

    //equivalente
    @GET("/user/{id}")
	User getUser(@Path int id);
```

Query parameters can also be used. As with `#!java @Path` you can use a different name than the parameter with `#!java @Query("variable")`.

``` java
    @GET("/{enterprise}/users")
    List<User> getUsersByEnterprise(@Path String enterprise, @Query String("sort") sortValue);

    //equivalente
    @GET("/{enterprise}/users")
    List<User> getUsersByEnterprise(@Path String enterprise, @Query String sort);
```

Or if you prefer you can also pass the Query parameters as a Map with `#!java @QueryMap`


``` java
    @GET("/{enterprise}/users")
    List<User> getUsersByEnterprise(@Path String enterprise, @QueryMap Map<String, String> queries);
```

## Headers
Headers can be included through annotations `#!java @Header`, `#!java @Headers` and `#!java @HeaderMap` or by extending the interface of `ConfigurationInterface`.

#### Annotations
`#!java @Header`  by default it will use the name of the variable as a key, if you want to change it you can indicate in the annotation `#!java @Header("key")`
``` java
  @Headers({"Compilation: 987654321", "DeviceType: Android"})
  @GET("/device")
  Configuration headers();

  @GET("/device")
  Configuration headers(
      @Header Long compilation,
      @Header String deviceType);

  @Headers({"header1: value1", "header2: value2"})
  @GET("/device")
  Configuration headersAnyway(
      @Header String country,
      @Header Long deviceUUID,
      @HeaderMap Map<String, String> headers);
```
#### ConfigurationInterface
If we extend the interface of ConfigurationInterfaz we can call the methods to establish and obtain the headers defined for all the methods of the interface.

- `#!java Map<String, String> getHeaders();`
- `#!java void setHeaders(Map<String, String> headers);`

## URL Dynamically

You can change the base url set with `#!java @Modernfit` using `#!java @Url` or by making the interface extend from `ConfigurationInterface`.

#### @URL

The url passed by parameter will cause the base url to be ignored  `"https://remotehost/api"`.

``` java
@Modernfit(value = "https://remotehost/api", converterFactory = JacksonConverterFactory.class)
public interface UrlEchoResponseRepository {

  @GET("/echo")
  EchoResponse urlAsParameter(@Url String url);
}

```

#### ConfigurationInterface

By extending ConfigurationInterface we can call the method `#!java void setBaseUrl(String baseUrl);` to change the base url.

``` java
@Modernfit(value = "http://remotehost/api", converterFactory = JacksonConverterFactory.class)
public interface UrlEchoResponseRepository extends ConfigurationInterface {

  @GET("/echo")
  EchoResponse urlInInterfaceImpl();
}
```

## FormUrlEncoded
If the annotation is used, the `#!java @FormUrlEncoded` HTTP request will be of type `"application/x-www-form-urlencoded"`. Each key value is defined by the annotation `#!java @Field`, the key is given by the name of the variable or through the annotation field `#!java @Field("key")`, the object provides the value.

Fields can also be provided with an annotated Map with `#!java @FieldMap`

``` java
@FormUrlEncoded
@POST("/user/create")
User createUser(@Field String name, @Field String login);

@FormUrlEncoded
@POST("/user/create")
User createUser(@FieldMap Map<String, String> user);
```

##Multipart
If the annotation is used, the `#!java @Multipart` HTTP request will be of type Multipart. Each part is declared using annotation `#!java @Part`.

You can also include multiple parts at once using a Map and annotation  `#!java @PartMap`

``` java
@Multipart
@POST("/user/create")
User createUser(@Part User user, @Part String entry);

@Multipart
@POST("/users/createHeavy")
Collection<User> createUsers(@PartMap Map<String, User> users);

```

## HTTP Client

You can use the clients already defined by Modernfit or if you prefer define a [custom client http](#define-custom-http-client) . The client to use is defined in the client field of the annotation `#!java @Modernfit`.

 - [ClientOkHttp.class](https://gitlab.com/ygModesto/modernfit/-/blob/develop/modernfit/src/main/java/com/ygmodesto/modernfit/services/ClientOkHttp.java) para usar [OkHttp](https://square.github.io/okhttp/)
 - [ClientVolley.class](https://gitlab.com/ygModesto/modernfit/-/blob/develop/android/volley/src/main/java/com/ygmodesto/modernfit/volley/ClientVolley.java) para usar [Volley](https://developer.android.com/training/volley) (Solo para Android).


#### ClientOkHttp.class

For HTTP requests it uses the [OkHttp](https://square.github.io/okhttp/) library. This library needs the [OkHttpClient](https://github.com/square/okhttp/blob/master/okhttp/src/main/kotlin/okhttp3/OkHttpClient.kt) object that if you don't provide one to Modernfit it will generate one when building the interface implementation.

If you want to contribute an OkHttpClient object, it must be done when calling the constructor of the interface implementation. If we assume that the interface is called PongRepository.

``` java

OkHttpClient okHttpClient = new OkHttpClient();
PongRepositoryImpl.builder()
                .addHttpClient(ClientOkHttp.create(okHttpClient))
                .build();

```

#### ClientVolley.class

For HTTP requests it uses the [Volley](https://developer.android.com/training/volley) library. This library needs the [RequestQueue](https://github.com/google/volley/blob/master/core/src/main/java/com/android/volley/RequestQueue.java) object that if you don't provide one to Modernfit it will generate one when building the interface implementation.

!!! note
    To use Volley if the RequestQueue object is not passed, the Context must be passed through the following call:

    `#!java AndroidModernfitContext.setContext(context);`


If you want to provide a RequestQueue object, it must be done when calling the interface implementation constructor. If we assume that the interface is called PongRepository.

``` java

RequestQueue requestQueue = Volley.newRequestQueue(this);
PongRepositoryImpl.builder()
                .addHttpClient(ClientVolley.create(requestQueue))
                .build();

```

## Define Custom HTTP Client
To define an HTTP Client to use with Modernfit we must create a class that meets 2 requirements:

- It must have a static method `#!java public static ClientCustom create();`
- You must implement the `HttpClient` interface

Once we have our ClientCustom we can use it in the annotation `#!java @Modernfit`.
``` java
@Modernfit(client = ClientCustom.class)
public interface PongRepository {
}
```



Here is the skeleton of an example:
``` java
public class ClientCustom implements HttpClient {


  public static ClientCustom create() {
    //Creation of everything you need
  }


  @Override
  public ResponseContent callMethod(RequestInfo requestInfo, DiscreteBody body)
      throws ModernfitException {

    //code for http request 
  }

  @Override
  public <T> void callMethod(RequestInfo requestInfo, DiscreteBody body,
      ResponseCallback<T> callback) throws ModernfitException {

    //code for http request using callback
  }

  @Override
  public ResponseContent callMethod(RequestInfo requestInfo, MultipartBody body)
      throws ModernfitException {


    //code for http multipart request 
  }

  @Override
  public <T> void callMethod(RequestInfo requestInfo, MultipartBody body,
      ResponseCallback<T> callback) throws ModernfitException {

    //code for http multipart request using callback
  }
}
```

## Define Custom Converter

To define a Converter to use with Modernfit we must create a class that meets 2 requirements:

- It must have a static method `#!java public static CustomConverterFactory create();`
- You must implement the `Converter.Factory` interface

Converter.Factory forces to implement 3 methods. These methods should return a Converter.

- `#!java public <T> Converter<T, BodyContent> getRequestConverter(T zombie, CustomType<T> customType);` This Coverter will be used to convert the Object annotated with `#!java @Body` of type T.
- `#!java public <T> Converter<ResponseContent, T> getResponseConverter(T zombie, CustomType<T> customType);` This Converter will be used to convert the response of the HTTP request to the object to be returned.
- `#!java public <T> Converter<T, String> getUrlConverter(T zombie, CustomType<T> customType);` This Converter will be used to convert the parameters noted with `#!java @Path`, `#!java @Query` and `#!java @QueryMap` to convert them to URL.


Here is the skeleton of an example:
```java
public class CustomConverterFactory extends BaseConverterFactory implements Converter.Factory {

  public static CustomConverterFactory create(){
    //Creation of everything you need
  }
  
  @Override
  public <T> Converter<T, BodyContent> getRequestConverter(T zombie, CustomType<T> customType) {
    //code to create the converter
  }

  @Override
  public <T> Converter<ResponseContent, T> getResponseConverter(T zombie,
      CustomType<T> customType) {
    //code to create the converter
  }

  @Override
  public <T> Converter<T, String> getUrlConverter(T zombie, CustomType<T> customType) {
    //code to create the converter
  }

}
```

The `CustomType<T> customType` parameter contains all the necessary information that the main Converters libraries need. The parameter `T zombie` has been added to be able to do Method Overloading, this parameter will only receive an object of that type set to null. Thanks to Method Overloading we can generate specific converters for certain types of data, for example, the BaseConverterFactory class from which the previous example extends is responsible for generating all Converters for cases in which the response or request are scalars (integers, floats, a raw string instead of the JSON expected by Jackson or Gson for example).


##Usage
### maven
``` xml
    <properties>
		<java.version>1.8</java.version> <!-- depending on your project -->
		<modernfit.version>1.0.0-SNAPSHOT</modernfit.version>
	</properties>
    ...
    <dependency>
	    <groupId>com.ygmodesto.modernfit</groupId>
		<artifactId>modernfit</artifactId>
        <version>${modernfit.version}</version>
	</dependency>
    ...
    <build>
	    <plugins>
		    <plugin>
			    <groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.8.1</version> <!-- depending on your project -->
				<configuration>
					<source>${java.version}</source>
					<target>${java.version}</target>
					<annotationProcessorPaths>
						<path>
							<groupId>com.ygmodesto.modernfit</groupId>
							<artifactId>modernfit-compiler</artifactId>
							<version>${modernfit.version}</version>
						</path>
					</annotationProcessorPaths>
				</configuration>
			</plugin>
		</plugins>
	</build>
```
Remember to add the dependencies of the client and the converter that you are going to use, if for example you are going to use OkHttp and Gson you need to add the dependencies of the library.

``` xml
      <dependency>
				<groupId>com.squareup.okhttp3</groupId>
				<artifactId>okhttp</artifactId>
				<version>${okhttp.version}</version>
			</dependency>

			<dependency>
				<groupId>com.google.code.gson</groupId>
				<artifactId>gson</artifactId>
				<version>${gson.version}</version>
			</dependency>

```

### gradle

``` gradle
implementation 'com.ygmodesto.modernfit:modernfit:1.0.0-SNAPSHOT'
annotationProcessor 'com.ygmodesto.modernfit:modernfit-compiler:1.0.0-SNAPSHOT'
```

Remember to add the dependencies of the client and the converter that you are going to use, if for example you are going to use OkHttp and Gson you need to add the dependencies of the library.

``` gradle
implementation 'com.squareup.okhttp3:okhttp:VERSION'
annotationProcessor 'com.google.code.gson:gson:VERSION'
```