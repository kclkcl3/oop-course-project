package virtualstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import virtualstore.entity.Product;
import virtualstore.service.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  @Autowired
  private ProductService productService;

  // CREATE - POST /api/products
  @PostMapping
  public ResponseEntity<Product> createProduct(@RequestBody Product product) {
    Product created = productService.createProduct(product);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  // READ - GET /api/products (все товары)
  @GetMapping
  public ResponseEntity<List<Product>> getAllProducts() {
    List<Product> products = productService.getAllProducts();
    return ResponseEntity.ok(products);
  }

  // READ - GET /api/products/{id}
  @GetMapping("/{id}")
  public ResponseEntity<Product> getProductById(@PathVariable UUID id) {
    return productService.getProductById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  // READ - GET /api/products/category/{category}
  @GetMapping("/category/{category}")
  public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
    List<Product> products = productService.getProductsByCategory(category);
    return ResponseEntity.ok(products);
  }

  // READ - GET /api/products/search?query=
  @GetMapping("/search")
  public ResponseEntity<List<Product>> searchProducts(@RequestParam String query) {
    List<Product> products = productService.searchProducts(query);
    return ResponseEntity.ok(products);
  }

  // READ - GET /api/products/price-range?min=100&max=500
  @GetMapping("/price-range")
  public ResponseEntity<List<Product>> getProductsByPriceRange(
      @RequestParam BigDecimal min,
      @RequestParam BigDecimal max) {
    List<Product> products = productService.getProductsByPriceRange(min, max);
    return ResponseEntity.ok(products);
  }

  // READ - GET /api/products/top-rated?minRating=4.0
  @GetMapping("/top-rated")
  public ResponseEntity<List<Product>> getTopRatedProducts(
      @RequestParam(defaultValue = "4.0") BigDecimal minRating) {
    List<Product> products = productService.getTopRatedProducts(minRating);
    return ResponseEntity.ok(products);
  }

  // READ - GET /api/products/low-stock?threshold=10
  @GetMapping("/low-stock")
  public ResponseEntity<List<Product>> getLowStockProducts(
      @RequestParam(defaultValue = "10") Integer threshold) {
    List<Product> products = productService.getLowStockProducts(threshold);
    return ResponseEntity.ok(products);
  }

  // READ - GET /api/products/categories
  @GetMapping("/categories")
  public ResponseEntity<List<String>> getAllCategories() {
    List<String> categories = productService.getAllCategories();
    return ResponseEntity.ok(categories);
  }

  // UPDATE - PUT /api/products/{id}
  @PutMapping("/{id}")
  public ResponseEntity<Product> updateProduct(
      @PathVariable UUID id,
      @RequestBody Product product) {
    try {
      Product updated = productService.updateProduct(id, product);
      return ResponseEntity.ok(updated);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // UPDATE - PATCH /api/products/{id}/stock?quantity=50
  @PatchMapping("/{id}/stock")
  public ResponseEntity<Product> updateStock(
      @PathVariable UUID id,
      @RequestParam Integer quantity) {
    try {
      Product updated = productService.updateStock(id, quantity);
      return ResponseEntity.ok(updated);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // DELETE - DELETE /api/products/{id}
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
    try {
      productService.deleteProduct(id);
      return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
