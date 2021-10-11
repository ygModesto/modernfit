package com.ygmodesto.modernfit.sample.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/api")
public class ProductREST {
  
  private static Collection<Product> products = new ArrayList<>();
  private static Map<Long, MultipartFile> manuals = new HashMap<>();
  
  @GetMapping("/products")
  public Collection<Product> getProducts() {
    
    return products;
    
  }
  
  @GetMapping("/product/{barcode}")
  public Product getProduct(@PathVariable Long barcode) {
    
    return products.stream().filter(p -> p.barcode.equals(barcode)).findAny().orElse(null);
    
  }
  
  @PutMapping("/product/{barcode}")
  public Product updateProduct(@PathVariable Long barcode, @RequestBody String name) {
//    products.stream().filter(p -> p.barcode.equals(code)).map(p -> { p.setName(name); return p; } ).collect(Collectors.toList());
    for (Product product : products) {
       if (product.getBarcode().equals(barcode)) {
         product.setName(name);
         return product;
       }
    }
    return null;
  }
  
  @PostMapping("/product/create")
  public Product createProduct(@RequestBody Product product) {
    
    for (Product p : products) {
      if (p.getBarcode().equals(product.getBarcode())) {
        p.setName(product.getName());
        return p;
      }
   }
    products.add(product);
    
    return product;
  }
  
  @PostMapping("/formurlencoded/product/create")
  public Product createProduct(@RequestParam Map<String, String> params) {
    
    return createProduct(new Product(Long.valueOf(params.get("barcode")), params.get("name")));
  }
  
  @PostMapping(value = "/multipart/product/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ProductAndManual createProduct(
      @RequestPart(required = false) Product product,
      @RequestPart(required = false) MultipartFile manual) throws IOException {

    if (manual != null) {
      manuals.put(product.getBarcode(), manual);
      createProduct(product);
      return new ProductAndManual(product.getBarcode(), product.getName(), 
          manual.getBytes());
    }else {
      createProduct(product);
      return new ProductAndManual(product.getBarcode(), product.getName());
    }
    
    
  
  }
  
  @PostMapping(value = "/multipart/product/create/decomposed", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ProductAndManual createProductDecomposed(
      @RequestPart String barcode,
      @RequestPart String name,
      @RequestPart(required = false) MultipartFile manual) throws IOException {

    Product product = new Product(Long.valueOf(barcode), name);
    
    return createProduct(product, manual);
  
  }
  
  public static class Product {
    private Long barcode;
    private String name;
    
    public Product() {
    }
    
    public Product(Long barcode, String name) {
      this.barcode = barcode;
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
    
  }
  
  public static class ProductAndManual extends Product {
    private byte[] manual;
    
    public ProductAndManual() {
    }
    
    public ProductAndManual(Long id, String name) {
      super(id, name);
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

    }

}