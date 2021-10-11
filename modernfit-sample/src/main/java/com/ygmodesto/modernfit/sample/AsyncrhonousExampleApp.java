package com.ygmodesto.modernfit.sample;

import com.ygmodesto.modernfit.sample.repository.AsyncProductModernfit;
import com.ygmodesto.modernfit.sample.repository.AsyncProductModernfitImpl;
import com.ygmodesto.modernfit.services.HttpInfo;
import com.ygmodesto.modernfit.services.HttpResponseCallback;
import com.ygmodesto.modernfit.services.ModernfitException;
import com.ygmodesto.modernfit.services.ResponseCallback;
import java.util.Collection;

public class AsyncrhonousExampleApp {



  private static AsyncProductModernfit productModernfit;
  

  public static void main(String[] args) throws Exception {
    System.out.println("Asynchronous Example");

    productModernfit = AsyncProductModernfitImpl.builder().build();

    productModernfit.createProduct(new Product(4231414L, "chair"), new ResponseCallback<Product>() {
      public void onSuccess(Product product) {
        System.out.println("Product: " +  product);
      }

      public void onFailure(ModernfitException e) {
        System.err.println(e);
      }
    });

    Thread.sleep(500L);
    productModernfit.createProduct(new Product(832131L, "lamp"), new ResponseCallback<Product>() {
      public void onSuccess(Product product) {
        System.out.println("Product: " +  product);
      }

      public void onFailure(ModernfitException e) {
        System.err.println(e);
      }
    });
    
    Thread.sleep(500L);
    productModernfit.getProduct(4231414L, new ResponseCallback<Product>() {
      public void onSuccess(Product product) {
        System.out.println("Product: " +  product);
      }

      public void onFailure(ModernfitException e) {
        System.err.println(e);
      }
    });
    
    Thread.sleep(500L);
    productModernfit.getHttpInfoAndProduct(4231414L, new HttpResponseCallback<Product>() {
      public void onSuccess(HttpInfo<Product> response) {
        System.out.println("Response: " +  response);
      }

      public void onFailure(ModernfitException e) {
        System.err.println(e);
      }
    });
    
    Thread.sleep(500L);
    productModernfit.listProducts(new ResponseCallback<Collection<Product>>() {
      public void onSuccess(Collection<Product> response) {
        System.out.println("Products: " +  response);
      }

      public void onFailure(ModernfitException e) {
        System.err.println(e);
      }
    });
    
    Thread.sleep(1000L);
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
