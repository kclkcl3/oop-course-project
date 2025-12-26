package virtualstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import virtualstore.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

  // Поиск по категории
  List<Product> findByCategory(String category);

  // Поиск по названию (частичное совпадение, игнорируя регистр)
  List<Product> findByNameContainingIgnoreCase(String name);

  // Поиск товаров дороже указанной цены
  List<Product> findByPriceGreaterThan(BigDecimal price);

  // Поиск товаров в диапазоне цен
  List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

  // Поиск товаров с рейтингом выше указанного
  List<Product> findByRatingGreaterThanEqual(BigDecimal rating);

  // Поиск товаров с количеством меньше указанного (для контроля остатков)
  List<Product> findByQuantityLessThan(Integer quantity);

  // Получить все категории (кастомный запрос)
  @Query("SELECT DISTINCT p.category FROM Product p WHERE p.category IS NOT NULL")
  List<String> findAllCategories();

  // Топ товаров по рейтингу
  @Query("SELECT p FROM Product p WHERE p.rating IS NOT NULL ORDER BY p.rating DESC")
  List<Product> findTopRatedProducts();
}
