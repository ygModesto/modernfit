# Migrating from Retrofit

Modernfit uses the same annotation syntax as Retrofit to facilitate migration.

What will we gain from Retrofit?

  - Code generation at compile time instead of at run time.
  - Detection of errors before executing.
  - Independence of the HTTP Client, you can choose one or use the one you want.
  - No code obfuscation by encapsulating the returned objects in a `Call` object

Below are two migration examples, one from Retrofit + RxJava to Modernfit with RxJava and another going from Retrofit. 

!!! note
    In Android Studio we must give the first time we have our interface to `Build -> Make Project` 
    to generate the implementation so that the editor recognizes the `UserRepositoryImpl` class 
    referenced in the examples.



## From Call.enqueue

### Retrofit

=== "UserRepository"

    ``` java
    import retrofit2.Call;
    import retrofit2.http.Body;
    import retrofit2.http.GET;
    import retrofit2.http.POST;
    import retrofit2.http.Path;

    public interface UserRepository {
        
        @GET("user/{id}")
        Call<User> getUser(@Path("id") Long id);


        @POST("user/{id}/update")
        Call<User> updateUser(@Path("id") Long id, @Body UpdateUserTO updateUserTO);
    }
    ```


=== "MainActivity"

    ``` java
    Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://remotehost.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    UserRepository userRepository = retrofit.create(UserRepository.class);

    userRepository.updateUser(23L, updateUserTO)
        .enqueue(new Callback<User>() {
                
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                //code
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                //code
            }
        });
    ```

=== "gradle"

    ``` groovy
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    ```
### Modernfit

=== "UserRepository"

    ``` java
    import com.ygmodesto.modernfit.annotations.Body;
    import com.ygmodesto.modernfit.annotations.GET;
    import com.ygmodesto.modernfit.annotations.Modernfit;
    import com.ygmodesto.modernfit.annotations.POST;
    import com.ygmodesto.modernfit.converters.GsonConverterFactory;

    @Modernfit(value = "https://remotehost.com/api/", converterFactory = GsonConverterFactory.class)
    public interface UserRepository {
        
        @GET("user/{id}")
        void getUser(@Path Long id, ResponseCallback<User> responseCallback);


        @POST("user/{id}/update")
        void updateUser(@Path Long id, @Body UpdateUserTO updateUserTO, ResponseCallback<User> responseCallback);
    }
    ```


=== "MainActivity"

    ``` java
    UserRepository userRepository = UserRepositoryImpl.builder().build();

    userRepository.updateUser(23L, updateUserTO, new ResponseCallback<User>(){
        public void onSuccess(User user) {
            //code
        }

        public void onFailure(ModernfitException e) {
            //code
        }
    });
    ```

=== "gradle"

    ``` groovy
    implementation 'com.ygmodesto.modernfit:modernfit:1.0.0'
    annotationProcessor 'com.ygmodesto.modernfit:modernfit-processor:1.0.0'
    ```

## From RxJava3

### Retrofit

=== "UserRepository"

    ``` java
    import io.reactivex.rxjava3.core.Observable;
    import retrofit2.Call;
    import retrofit2.http.Body;
    import retrofit2.http.GET;
    import retrofit2.http.POST;
    import retrofit2.http.Path;

    public interface UserRepository {
        
        @GET("user/{id}")
        Observable<User> getUser(@Path("id") Long id);


        @POST("user/{id}/update")
        Observable<User> updateUser(@Path("id") Long id, @Body UpdateUserTO updateUserTO);
    }
    ```


=== "MainActivity"

    ``` java
    Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://remotehost.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    UserRepository userRepository = retrofit.create(UserRepository.class);

    userRepository.updateUser(23L, updateUserTO)
        .subscribeOn(Schedulers.io())
        .subscribe(e -> //code
                    );
    ```

=== "gradle"

    ``` groovy
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.retrofit2:adapter-rxjava3:2.9.0'
    implementation "io.reactivex.rxjava3:rxjava:3.1.2"
    ```



### Modernfit

=== "UserRepository"

    ``` java
    import io.reactivex.rxjava3.core.Observable;
    import com.ygmodesto.modernfit.annotations.Body;
    import com.ygmodesto.modernfit.annotations.GET;
    import com.ygmodesto.modernfit.annotations.Modernfit;
    import com.ygmodesto.modernfit.annotations.POST;
    import com.ygmodesto.modernfit.converters.GsonConverterFactory;

    @Modernfit(value = "https://remotehost.com/api/", converterFactory = GsonConverterFactory.class)
    public interface UserRepository {
        
        @GET("user/{id}")
        Observable<User> getUser(@Path Long id);


        @POST("user/{id}/update")
        Observable<User> updateUser(@Path Long id, @Body UpdateUserTO updateUserTO);
    }
    ```


=== "MainActivity"

    ``` java
    UserRepository userRepository = UserRepositoryImpl.builder().build();

    userRepository.getPongResponse()
                .subscribeOn(Schedulers.io())
                .subscribe(e -> Log.d("Migration", e.toString()));

    userRepository.updateUser(23L, updateUserTO)
        .subscribeOn(Schedulers.io())
        .subscribe(e -> //code
                    );
    ```

=== "gradle"

    ``` groovy
    implementation 'com.ygmodesto.modernfit:modernfit:1.0.0'
    implementation "io.reactivex.rxjava3:rxjava:3.1.2"
    annotationProcessor 'com.ygmodesto.modernfit:modernfit-processor:1.0.0'
    ```

## From Call.execute

In this example it is assumed that both in Retrofit and Modernfit the developer manages on his own that the request is thrown in another Thread since Android will throw the exception `android.os.NetworkOnMainThreadException`

### Retrofit

=== "UserRepository"

    ``` java
    import retrofit2.Call;
    import retrofit2.http.Body;
    import retrofit2.http.GET;
    import retrofit2.http.POST;
    import retrofit2.http.Path;

    public interface UserRepository {
        
        @GET("user/{id}")
        Call<User> getUser(@Path("id") Long id);


        @POST("user/{id}/update")
        Call<User> updateUser(@Path("id") Long id, @Body UpdateUserTO updateUserTO);
    }
    ```


=== "MainActivity"

    ``` java
    Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://remotehost.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    UserRepository userRepository = retrofit.create(UserRepository.class);

    User user = userRepository.updateUser(23L, updateUserTO).execute().body();
    ```

=== "gradle"

    ``` groovy
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    ```
### Modernfit

=== "UserRepository"

    ``` java
    import com.ygmodesto.modernfit.annotations.Body;
    import com.ygmodesto.modernfit.annotations.GET;
    import com.ygmodesto.modernfit.annotations.Modernfit;
    import com.ygmodesto.modernfit.annotations.POST;
    import com.ygmodesto.modernfit.converters.GsonConverterFactory;

    @Modernfit(value = "https://remotehost.com/api/", converterFactory = GsonConverterFactory.class)
    public interface UserRepository {
        
        @GET("user/{id}")
        User getUser(@Path Long id);


        @POST("user/{id}/update")
        User updateUser(@Path Long id, @Body UpdateUserTO updateUserTO);
    }
    ```


=== "MainActivity"

    ``` java
    UserRepository userRepository = UserRepositoryImpl.builder().build();

    User user = userRepository.updateUser(23L, updateUserTO);
    ```

=== "gradle"

    ``` groovy
    implementation 'com.ygmodesto.modernfit:modernfit:1.0.0'
    annotationProcessor 'com.ygmodesto.modernfit:modernfit-processor:1.0.0'
    ```