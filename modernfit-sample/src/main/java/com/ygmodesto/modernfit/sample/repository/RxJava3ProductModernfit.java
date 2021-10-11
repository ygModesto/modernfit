package com.ygmodesto.modernfit.sample.repository;

import com.ygmodesto.modernfit.annotations.Body;
import com.ygmodesto.modernfit.annotations.GET;
import com.ygmodesto.modernfit.annotations.Modernfit;
import com.ygmodesto.modernfit.annotations.POST;
import com.ygmodesto.modernfit.annotations.PUT;
import com.ygmodesto.modernfit.annotations.Path;
import com.ygmodesto.modernfit.converters.JacksonConverterFactory;
import com.ygmodesto.modernfit.sample.RxJava3ExampleApp.Product;
import com.ygmodesto.modernfit.services.HttpInfo;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.Collection;

@Modernfit(value = "http://localhost:8080/api", converterFactory = JacksonConverterFactory.class)
public interface RxJava3ProductModernfit {
  
  @GET("/product/{barcode}")
  Single<Product> getProduct(@Path Long barcode);
  
  @PUT("/product/{barcode}")
  Single<Product> updateProduct(@Path("barcode") Long identifier, @Body String name);
  
  @POST("/product/create")
  Single<Product> createProduct(@Body Product product);
  
  @GET("/products")
  Observable<Collection<Product>> listProducts();
  
  @GET("/product/{barcode}")
  Single<HttpInfo<Product>> getHttpInfoAndProduct(@Path Long barcode);
}
