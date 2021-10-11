package com.ygmodesto.modernfit.sample;

import com.ygmodesto.modernfit.sample.repository.RxJava3ProductModernfit;
import com.ygmodesto.modernfit.sample.repository.RxJava3ProductModernfitImpl;

public class RxJava3ExampleApp {

  private static RxJava3ProductModernfit productModernfit;
  
  public static void main(String[] args) {
    System.out.println("RxJava3 Example");
    
    productModernfit = RxJava3ProductModernfitImpl.builder().build();

    System.out.println("Product: ");
    productModernfit.createProduct(new Product(4231414L, "chair")).subscribe(System.out::println);
    System.out.println("Product: ");
    productModernfit.createProduct(new Product(832131L, "lamp")).subscribe(System.out::println);
    System.out.println("Product: ");
    productModernfit.getProduct(4231414L).subscribe(System.out::println);
    System.out.println("Response: ");
    productModernfit.getHttpInfoAndProduct(4231414L).subscribe(System.out::println);
    System.out.println("Products: ");
    productModernfit.listProducts().subscribe(System.out::println);
    
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
