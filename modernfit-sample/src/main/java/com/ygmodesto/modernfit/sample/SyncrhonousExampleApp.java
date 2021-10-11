package com.ygmodesto.modernfit.sample;

import com.ygmodesto.modernfit.sample.repository.SynchronousProductModernfit;
import com.ygmodesto.modernfit.sample.repository.SynchronousProductModernfitImpl;

public class SyncrhonousExampleApp {


  private static SynchronousProductModernfit productModernfit;
  
  public static void main(String[] args) {
    System.out.println("Synchronous Example");
    
    productModernfit = SynchronousProductModernfitImpl.builder().build();

    System.out.println("Product: " + productModernfit.createProduct(new Product(4231414L, "chair")));
    System.out.println("Product: " + productModernfit.createProduct(new Product(832131L, "lamp")));
    System.out.println("Product: " + productModernfit.getProduct(4231414L));
    System.out.println("Response: " + productModernfit.getHttpInfoAndProduct(4231414L));
    System.out.println("Products: " + productModernfit.listProducts());
    
  }

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
}
