package com.ygmodesto.modernfit.sample.repository;

import com.ygmodesto.modernfit.annotations.Body;
import com.ygmodesto.modernfit.annotations.GET;
import com.ygmodesto.modernfit.annotations.Modernfit;
import com.ygmodesto.modernfit.annotations.POST;
import com.ygmodesto.modernfit.annotations.PUT;
import com.ygmodesto.modernfit.annotations.Path;
import com.ygmodesto.modernfit.converters.JacksonConverterFactory;
import com.ygmodesto.modernfit.sample.AsyncrhonousExampleApp.Product;
import com.ygmodesto.modernfit.services.HttpResponseCallback;
import com.ygmodesto.modernfit.services.ResponseCallback;
import java.util.Collection;

@Modernfit(value = "http://localhost:8080/api", converterFactory = JacksonConverterFactory.class)
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
