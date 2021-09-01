## Introducción

Define tu interfaz y Modernfit la implementa para conectarse con tu API

 * En tiempo de compilación
 * Genera codigo fuente legible
 * Errores en tiempo de compilación, no esperes a ejecutar
 * Usa OkHttp o Volley, el qué prefieras sin modificar el código


``` java
@Modernfit(value = "http://localhost:8080/api")
public interface UserModernfit {

    //Síncrona
	@GET("/user/{id}")
	User getUser(@Path int id);

    //Asíncrona con callback
    @GET("/user/{id}")
	void getUser(@Path int id, CustomCallback<User> customCallback);


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


    @PUT(value = "/updateUser/{email}", charset = "ISO-8859-1")
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

O si lo prefieres también puedes pasar los Query paramenters como un Map con @QueryMap


``` java
    @GET("/{enterprise}/users")
    List<User> getUsersByEnterprise(@Path String enterprise, @QueryMap Map<String, String> queries);
```


## Request Body

Puedes utilizar [OkHttp](https://square.github.io/okhttp/) o [Volley](https://developer.android.com/training/volley) sin modificar tú aplicación
La única diferencia es que Volley necesita el Context

``` java
@Modernfit(value = "http://localhost:8080/api", 
                    client = ClientOkHttp.class)
public interface UserModernfit {

    @GET("/user/{idUser}")
    User getUser(@Path("idUser") int id);

    @GET("/user/{id}")
	void getUser(@Path int id, CustomCallback<User> customCallback);
}
```
