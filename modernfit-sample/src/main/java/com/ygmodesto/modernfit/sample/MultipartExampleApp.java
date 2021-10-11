package com.ygmodesto.modernfit.sample;

import com.ygmodesto.modernfit.sample.repository.MultipartProductModernfit;
import com.ygmodesto.modernfit.sample.repository.MultipartProductModernfitImpl;
import com.ygmodesto.modernfit.services.TypedContent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;

public class MultipartExampleApp {

  private static MultipartProductModernfit productModernfit;

  public static void main(String[] args) throws IOException {
    System.out.println("Multipart Example");

    productModernfit = MultipartProductModernfitImpl.builder().build();

    File file = new ClassPathResource("manual.txt").getFile();
    TypedContent manual = TypedContent.create("text/plain", file);
    System.out.println("Product: " + productModernfit.createProduct(new Product(4231414L, "chair"), manual));
    Map<String, String> productAsMap = new HashMap<String, String>();
    productAsMap.put("barcode", "832131");
    productAsMap.put("name", "lamp");
    System.out.println("Product: " + productModernfit.createProduct(productAsMap));
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

  public static class ProductAndManual extends Product {
    private byte[] manual;
    
    public ProductAndManual() {
    }
    
    public ProductAndManual(Long id, String name, byte[] manual) {
      super(id, name);
      this.manual = manual;
    }
  
    public byte[] getManual() {
      return manual;
    }
  
    public void setManual(byte[] manual) {
      this.manual = manual;
    }
  
    @Override
    public String toString() {
      String manualValue = (manual == null) ? "" : new String(manual);
      return "ProductAndManual [manual="
          + manualValue
          + ", getBarcode()="
          + getBarcode()
          + ", getName()="
          + getName()
          + "]";
    }
    
  }
}
