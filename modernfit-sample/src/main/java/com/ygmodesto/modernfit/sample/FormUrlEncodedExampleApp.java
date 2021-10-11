package com.ygmodesto.modernfit.sample;

import com.ygmodesto.modernfit.sample.repository.FormUrlEncodedProductModernfit;
import com.ygmodesto.modernfit.sample.repository.FormUrlEncodedProductModernfitImpl;
import java.util.HashMap;
import java.util.Map;

public class FormUrlEncodedExampleApp {

  public static class Product {
    private Long barcode;
    private String name;
    
    public Product() {
    }
    
    public Product(Long id, String name) {
      this.barcode = id;
      this.name = name;
    }
  
    public Long getBarcode() {
      return barcode;
    }
    
    public void setBarcode(Long barcode) {
      this.barcode = barcode;
    }
  
    public String getName() {
      return name;
    }
    
    public void setName(String name) {
      this.name = name;
    }
  
    @Override
    public String toString() {
      return "Product [barcode=" + barcode + ", name=" + name + "]";
    }
    
  }

  private static FormUrlEncodedProductModernfit productModernfit;

  public static void main(String[] args) {
    System.out.println("FormUrlEncoded Example");

    productModernfit = FormUrlEncodedProductModernfitImpl.builder().build();

    System.out.println("Product: " + productModernfit.createProduct(4231414L, "chair"));
    Map<String, String> productAsMap = new HashMap<String, String>();
    productAsMap.put("barcode", "832131");
    productAsMap.put("name", "lamp");
    System.out.println("Product: " + productModernfit.createProduct(productAsMap));
    System.out.println("Product: " + productModernfit.getProduct(4231414L));
    System.out.println("Response: " + productModernfit.getHttpInfoAndProduct(4231414L));
    System.out.println("Products: " + productModernfit.listProducts());

  }
}
