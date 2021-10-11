package com.ygmodesto.modernfit.sample.repository;

import com.ygmodesto.modernfit.annotations.Body;
import com.ygmodesto.modernfit.annotations.GET;
import com.ygmodesto.modernfit.annotations.Modernfit;
import com.ygmodesto.modernfit.annotations.POST;
import com.ygmodesto.modernfit.annotations.PUT;
import com.ygmodesto.modernfit.annotations.Path;
import com.ygmodesto.modernfit.converters.JacksonConverterFactory;
import com.ygmodesto.modernfit.sample.SyncrhonousExampleApp.Product;
import com.ygmodesto.modernfit.services.HttpInfo;
import java.util.Collection;

@Modernfit(value = "http://localhost:8080/api", converterFactory = JacksonConverterFactory.class)
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
