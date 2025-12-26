package virtualstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import virtualstore.entity.Product;
import virtualstore.repository.ProductRepository;
import virtualstore.repository.ReviewRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ReviewRepository reviewRepository;

  // CREATE - Создание товара
  @Transactional
  public Product createProduct(Product product) {
    if (product.getProductId() == null) {
      product.setProductId(UUID.randomUUID());
    }
    // Рейтинг устанавливается null при создании
    product.setRating(null);
    return productRepository.save(product);
  }

  // READ - Все товары
  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  // READ - Товар по ID
  public Optional<Product> getProductById(UUID id) {
    return productRepository.findById(id);
  }

  // READ - Товары по категории
  public List<Product> getProductsByCategory(String category) {
    return productRepository.findByCategory(category);
  }

  // READ - Поиск по названию
  public List<Product> searchProducts(String query) {
    return productRepository.findByNameContainingIgnoreCase(query);
  }

  // READ - Товары в диапазоне цен
  public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
    return productRepository.findByPriceBetween(minPrice, maxPrice);
  }

  // READ - Товары с рейтингом выше указанного
  public List<Product> getTopRatedProducts(BigDecimal minRating) {
    return productRepository.findByRatingGreaterThanEqual(minRating);
  }

  // READ - Товары с низким остатком (для предупреждения)
  public List<Product> getLowStockProducts(Integer threshold) {
    return productRepository.findByQuantityLessThan(threshold);
  }

  // READ - Все категории
  public List<String> getAllCategories() {
    return productRepository.findAllCategories();
  }

  // READ - Топ товаров по рейтингу
  public List<Product> getTopRated() {
    return productRepository.findTopRatedProducts();
  }

  // UPDATE - Обновление товара
  @Transactional
  public Product updateProduct(UUID id, Product updatedProduct) {
    return productRepository.findById(id)
        .map(product -> {
          product.setName(updatedProduct.getName());
          product.setCategory(updatedProduct.getCategory());
          product.setPrice(updatedProduct.getPrice());
          product.setQuantity(updatedProduct.getQuantity());
          // Рейтинг не обновляем вручную
          return productRepository.save(product);
        })
        .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
  }

  // UPDATE - Обновить остаток товара
  @Transactional
  public Product updateStock(UUID id, Integer newQuantity) {
    return productRepository.findById(id)
        .map(product -> {
          product.setQuantity(newQuantity);
          return productRepository.save(product);
        })
        .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
  }

  // UPDATE - Уменьшить остаток (при продаже)
  @Transactional
  public Product decreaseStock(UUID id, Integer quantity) {
    return productRepository.findById(id)
        .map(product -> {
          int currentStock = product.getQuantity();
          if (currentStock < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
          }
          product.setQuantity(currentStock - quantity);
          return productRepository.save(product);
        })
        .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
  }

  // UPDATE - Пересчитать рейтинг товара на основе отзывов
  @Transactional
  public Product updateProductRating(UUID productId) {
    return productRepository.findById(productId)
        .map(product -> {
          Double averageRating = reviewRepository.getAverageRatingByProduct(productId);
          if (averageRating != null && averageRating > 0) {
            // Округляем до 1 знака после запятой
            BigDecimal rating = BigDecimal.valueOf(averageRating)
                .setScale(1, RoundingMode.HALF_UP);
            product.setRating(rating);
          } else {
            product.setRating(null);
          }
          return productRepository.save(product);
        })
        .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
  }

  // DELETE - Удаление товара
  @Transactional
  public void deleteProduct(UUID id) {
    if (!productRepository.existsById(id)) {
      throw new RuntimeException("Product not found with id: " + id);
    }
    productRepository.deleteById(id);
  }
}
