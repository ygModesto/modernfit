package com.ygmodesto.modernfit.sample.repository;

import com.ygmodesto.modernfit.annotations.GET;
import com.ygmodesto.modernfit.annotations.Modernfit;
import com.ygmodesto.modernfit.annotations.Multipart;
import com.ygmodesto.modernfit.annotations.POST;
import com.ygmodesto.modernfit.annotations.PUT;
import com.ygmodesto.modernfit.annotations.Part;
import com.ygmodesto.modernfit.annotations.PartMap;
import com.ygmodesto.modernfit.annotations.Path;
import com.ygmodesto.modernfit.converters.JacksonConverterFactory;
import com.ygmodesto.modernfit.sample.MultipartExampleApp.Product;
import com.ygmodesto.modernfit.sample.MultipartExampleApp.ProductAndManual;
import com.ygmodesto.modernfit.services.HttpInfo;
import com.ygmodesto.modernfit.services.TypedContent;
import java.util.Collection;
import java.util.Map;

@Modernfit(value = "http://localhost:8080/api", converterFactory = JacksonConverterFactory.class)
public interface MultipartProductModernfit {
  
  
  @Multipart
  @GET("/product/{barcode}")
  Product getProduct(@Path Long barcode);
  
  @Multipart
  @POST("/multipart/product/create")
  ProductAndManual createProduct(@Part Product product, @Part TypedContent manual);
  
  @Multipart
  @POST("/multipart/product/create/decomposed")
  ProductAndManual createProduct(@PartMap Map<String, String> productAsMap);
  
  @Multipart
  @PUT("/product/{barcode}")
  Product updateProduct(@Path("barcode") Long identifier, @Part String name);

  @Multipart
  @GET("/product/{barcode}")
  HttpInfo<Product> getHttpInfoAndProduct(@Path Long barcode);
  
  @Multipart
  @GET("/products")
  Collection<Product> listProducts();
}
