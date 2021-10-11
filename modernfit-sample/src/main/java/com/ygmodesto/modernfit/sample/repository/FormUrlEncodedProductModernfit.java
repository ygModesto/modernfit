package com.ygmodesto.modernfit.sample.repository;

import com.ygmodesto.modernfit.annotations.Field;
import com.ygmodesto.modernfit.annotations.FieldMap;
import com.ygmodesto.modernfit.annotations.FormUrlEncoded;
import com.ygmodesto.modernfit.annotations.GET;
import com.ygmodesto.modernfit.annotations.Modernfit;
import com.ygmodesto.modernfit.annotations.POST;
import com.ygmodesto.modernfit.annotations.PUT;
import com.ygmodesto.modernfit.annotations.Path;
import com.ygmodesto.modernfit.converters.JacksonConverterFactory;
import com.ygmodesto.modernfit.sample.FormUrlEncodedExampleApp.Product;
import com.ygmodesto.modernfit.services.HttpInfo;
import java.util.Collection;
import java.util.Map;

@Modernfit(value = "http://localhost:8080/api", converterFactory = JacksonConverterFactory.class)
public interface FormUrlEncodedProductModernfit {
  
  
  @FormUrlEncoded
  @GET("/product/{barcode}")
  Product getProduct(@Path Long barcode);
  
  @FormUrlEncoded
  @POST("/formurlencoded/product/create")
  Product createProduct(@Field Long barcode, @Field String name);
  
  @FormUrlEncoded
  @POST("/formurlencoded/product/create")
  Product createProduct(@FieldMap Map<String, String> productAsMap);
  
  @FormUrlEncoded
  @PUT("/product/{barcode}")
  Product updateProduct(@Path("barcode") Long identifier, @Field String name);

  @FormUrlEncoded
  @GET("/product/{barcode}")
  HttpInfo<Product> getHttpInfoAndProduct(@Path Long barcode);
  
  @FormUrlEncoded
  @GET("/products")
  Collection<Product> listProducts();
}
