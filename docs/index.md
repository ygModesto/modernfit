## Introducción

Define tu interfaz y Modernfit la implementa por ti

 * En tiempo de compilación
 * Genera codigo fuente legible
 * Errores en tiempo de compilación, no esperes a ejecutar
 * Usa OkHttp, Volley, o uno definido por ti
 * Usa el converter que quieras, Jakson, Gson o uno definido por ti


``` java
@Modernfit(
    value = "http://api.someurl/api", 
    converterFactory = GsonConverterFactory.class)
public interface UserModernfit {

    //Síncrona
	@GET("/user/{id}")
	User getUser(@Path int id);

    //Asíncrona con callback
    @GET("/user/{id}")
	void getUser(@Path int id, ResponseCallback<User> responseCallback);

    //Asíncrona con RxJava
	@GET("/user/{id}")
	Single<User> getUser(@Path int id);
}
```



Peticiones síncronas y asíncronas

``` java
UserModernfit userModernfit = UserModernfitImpl.builder().build();
```


## Request Method
Los métodos de la interfaz tienen que estar anotados con `#!java @GET`, `#!java @POST`, `#!java @PUT`, `#!java @DELETE`, `#!java @OPTIONS` o `#!java @HEAD`. Esto indica qué tipo de petición HTTP son. Con esta anotación también se indica la url relativa y el charset del [Content-Type](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Type)

``` java
    @GET("/users")
    Collection<User> getUsers();


    @PUT(value = "/updateUser/{email}")
    User getUsers(@Path String email, @Body UpdateUserTO updateUser);
```

## URL Manipulation
La URL se puede modificar con variables entre { }, cada variable será sustituida por el valor del parámetro del mismo nombre anotado con `#!java @Path`, se puede poner un nombre diferente de parámetro indicando a qué variable sustituye a través del `#!java @Path("variable")`

``` java
	@GET("/user/{id}")
	User getUser(@Path("id") int userId);

    //equivalente
    @GET("/user/{id}")
	User getUser(@Path int id);
```


También se pueden utilizar Query parameters. Al igual que con `#!java @Path` se puede usar un nombre diferente al del parámetro con `#!java @Query("variable")`

``` java
    @GET("/{enterprise}/users")
    List<User> getUsersByEnterprise(@Path String enterprise, @Query String("sort") sortValue);

    //equivalente
    @GET("/{enterprise}/users")
    List<User> getUsersByEnterprise(@Path String enterprise, @Query String sort);
```

O si lo prefieres también puedes pasar los Query paramenters como un Map con `#!java @QueryMap`


``` java
    @GET("/{enterprise}/users")
    List<User> getUsersByEnterprise(@Path String enterprise, @QueryMap Map<String, String> queries);
```

## Headers
Los headers se pueden incluir a través de la anotación `#!java @Header`, `#!java @Headers` y `#!java @HeaderMap` o extendiendo la interfaz de `#!java ConfigurationInterface`.

### Anotaciones
`#!java @Header` por defecto usará el nombre de la variable como clave, si se desea cambiar se puede indicar en la anotación `#!java @Header("key")`
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
### ConfigurationInterface
Si extendemos la interfaz de ConfigurationInterfaz podremos llamar a los métodos para establecer y obtener las cabeceras definidas para todos los métodos de la interfaz.

- `#!java Map<String, String> getHeaders();`
- `#!java void setHeaders(Map<String, String> headers);`

## URL Dinamicamente

Se puede cambiar la url base establecida con `#!java @Modernfit` mediante `#!java @Url` o haciendo que al interfaz extienda de `#!java ConfigurationInterface`.

### @URL

La url pasada por parámetro hará que se ignore la url base `"https://remotehost/api"`.

``` java
@Modernfit(value = "https://remotehost/api", converterFactory = JacksonConverterFactory.class)
public interface UrlEchoResponseRepository {

  @GET("/echo")
  EchoResponse urlAsParameter(@Url String url);
}

```

### ConfigurationInterface

Al extender de ConfigurationInterface podremos llamar al método `#!java void setBaseUrl(String baseUrl);` para cambiar la url base.

``` java
@Modernfit(value = "http://remotehost/api", converterFactory = JacksonConverterFactory.class)
public interface UrlEchoResponseRepository extends ConfigurationInterface {

  @GET("/echo")
  EchoResponse urlInInterfaceImpl();
}
```

## Cliente HTTP

Puedes utilizar los clientes ya definidos por Modernfit o si lo prefieres definir un [custom client http](#definir-custom-http-client).
El cliente a usar se define en el campo client de la anotación `#!java @Modernfit`.

 - [ClientOkHttp.class](https://gitlab.com/ygModesto/modernfit/-/blob/develop/modernfit/src/main/java/com/ygmodesto/modernfit/services/ClientOkHttp.java) para usar [OkHttp](https://square.github.io/okhttp/)
 - [ClientVolley.class](https://gitlab.com/ygModesto/modernfit/-/blob/develop/android/volley/src/main/java/com/ygmodesto/modernfit/volley/ClientVolley.java) para usar [Volley](https://developer.android.com/training/volley) (Solo para Android).


#### ClientOkHttp.class

Para hacer las peticiones hace uso del objeto [OkHttpClient](https://github.com/square/okhttp/blob/master/okhttp/src/main/kotlin/okhttp3/OkHttpClient.kt) que si no se le proporcionar uno genera el uno.

!!! note
    Para usar Volley si no se le pasa el objeto RequestQueue debe pasarse el Context
    a través de la siguiente llamada 

    `#!java AndroidModernfitContext.setContext(context);`

Si se desea aportar un objeto OkHttpClient debe hacerse a la hora de llamar al constructor de la implementación de la interfaz. Si suponemos que la interfaz se llama PongRepository.

``` java

OkHttpClient okHttpClient = new OkHttpClient();
PongRepositoryImpl.builder()
                .addHttpClient(ClientOkHttp.create(okHttpClient))
                .build();

```

#### ClientVolley.class
Para hacer las peticiones hace uso del objeto [RequestQueue](https://github.com/google/volley/blob/master/core/src/main/java/com/android/volley/RequestQueue.java) que si no se le proporcionar uno genera el uno.

Si se desea aportar un objeto RequestQueue debe hacerse a la hora de llamar al constructor de la implementación de la interfaz. Si suponemos que la interfaz se llama PongRepository. 

``` java

RequestQueue requestQueue = Volley.newRequestQueue(this);
PongRepositoryImpl.builder()
                .addHttpClient(ClientVolley.create(requestQueue))
                .build();

```

## Definir Custom HTTP Client
Para definir un Cliente HTTP para usar con Modernfit debemos crear una clase que cumpla con 2 requisitos:

- Debe contar con método estatico `#!java public static ClientCustom create();`
- Debe implementar la interfaz HttpClient

Una vez tengamos nuestro ClientCustom ya podremos usarlo en la anotación @Modernfit.
``` java
@Modernfit(client = ClientCustom.class)
public interface PongRepository {
}
```



A continuación se deja el esqueleto de un ejemplo
``` java
public class ClientCustom implements HttpClient {


  public static ClientCustom create() {
    // Creacion de todo lo necesario
  }


  @Override
  public ResponseContent callMethod(RequestInfo requestInfo, DiscreteBody body)
      throws ModernfitException {

    //código para la petición http
  }

  @Override
  public <T> void callMethod(RequestInfo requestInfo, DiscreteBody body,
      ResponseCallback<T> callback) throws ModernfitException {

    //código para la petición http usando callback
  }

  @Override
  public ResponseContent callMethod(RequestInfo requestInfo, MultipartBody body)
      throws ModernfitException {


    //código para la petición http multipart
  }

  @Override
  public <T> void callMethod(RequestInfo requestInfo, MultipartBody body,
      ResponseCallback<T> callback) throws ModernfitException {

    //código para la petición http multipart usando callback
  }
}
```

## Definir Custom Converter
CUANDO EXPLICAR

Para definir un Converter para usar con Modernfit debemos crear una clase que cumpla con 2 requisitos:

- Debe contar con método estatico `#!java public static CustomConverterFactory create();`
- Debe implementar la interfaz Converter.Factory

Converter.Factory obliga a implementar 3 métodos. Estos métodos deben devolver un Converter.

- `#!java public <T> Converter<T, BodyContent> getRequestConverter(T zombie, CustomType<T> customType);` Este Coverter se usará para convertir el Objeto anotado con @Body de tipo T.
- `#!java public <T> Converter<ResponseContent, T> getResponseConverter(T zombie, CustomType<T> customType);` Este Converter se usará para convertir la respuesta de la petición HTTP al objeto a devolver.
- `#!java public <T> Converter<T, String> getUrlConverter(T zombie, CustomType<T> customType);` Este Converter se usará para convertir los parámetros anotados con `#!java @Path`, `#!java @Query` y `#!java @QueryMap` para convertirlos a URL.


A continuación se deja el esqueleto de un ejemplo
```java
public class CustomConverterFactory extends BaseConverterFactory implements Converter.Factory {

  public static CustomConverterFactory create(){
    // Creacion de todo lo necesario
  }
  
  @Override
  public <T> Converter<T, BodyContent> getRequestConverter(T zombie, CustomType<T> customType) {
    //código para crear el converter
  }

  @Override
  public <T> Converter<ResponseContent, T> getResponseConverter(T zombie,
      CustomType<T> customType) {
    //código para crear el converter
  }

  @Override
  public <T> Converter<T, String> getUrlConverter(T zombie, CustomType<T> customType) {
    //código para crear el converter
  }

}
```
El parámetro `#!java CustomType<T>` customType contiene toda la información necesaria que necesitan las principales librerias de Converters.
El parámetro `#!java T zombie` ha sido agregado para poder hacer Method Overloading, este parámetro solo recibirá un objeto de ese tipo seteado a null. Gracias al Method Overloading podemos generar converters específicos para ciertos tipos de datos, por ejemplo, la clase BaseConverterFactory de la que extiende el ejemplo anterior se encarga de generar todos los Converters para los casos en los que la respuesta o la petición sean scalares (enteros, flotantes, un string sin formato en vez de los JSON que espera Jackson o Gson por ejemplo).

## FormUrlEncoded
Si se utiliza la anotación `#!java @FormUrlEncoded` la petición HTTP será de tipo `"application/x-www-form-urlencoded"`. Cada clave valor está definido por la anotación `#!java @Field`, la clave viene dada por el nombre de la variable o a trav'es del campo de la anotacion `#!java @Field("key")`, el objeto provee el valor.

Los campos también se pueden proporcionar con un Map anotado con `#!java @FieldMap`

``` java
@FormUrlEncoded
@POST("/user/create")
User createUser(@Field String name, @Field String login);

@FormUrlEncoded
@POST("/user/create")
User createUser(@FieldMap Map<String, String> user);
```

##Multipart
Si se utiliza la anotación `#!java @Multipart` la petición HTTP será de tipo Multipart.
Cada part se declara utilizando la anotación `#!java @Part`. 

También se pueden incluir varios parts de una vez utilizando un Map y la anotación `#!java @PartMap`

``` java
@Multipart
@POST("/user/create")
User createUser(@Part User user, @Part String entry);

@Multipart
@POST("/users/createHeavy")
Collection<User> createUsers(@PartMap Map<String, User> users);

```
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

### gradle

``` gradle
implementation 'com.ygmodesto.modernfit:modernfit:1.0.0-SNAPSHOT'
annotationProcessor 'com.ygmodesto.modernfit:modernfit-compiler:1.0.0-SNAPSHOT'
```