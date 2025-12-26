package virtualstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import virtualstore.entity.OrderItem;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

  // Все позиции конкретного заказа
  List<OrderItem> findByOrderId(UUID orderId);

  // Все заказы, содержащие конкретный товар
  List<OrderItem> findByProductId(UUID productId);

  // Удалить все позиции заказа
  void deleteByOrderId(UUID orderId);

  // Количество проданных единиц товара
  @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.productId = :productId")
  Long getTotalQuantitySoldByProduct(@Param("productId") UUID productId);

  // Самые популярные товары (для аналитики)
  @Query("SELECT oi.productId, SUM(oi.quantity) as totalSold " +
      "FROM OrderItem oi " +
      "GROUP BY oi.productId " +
      "ORDER BY totalSold DESC")
  List<Object[]> findMostPopularProducts();

  // Позиции заказа с информацией о товаре (JOIN)
  @Query("SELECT oi FROM OrderItem oi " +
      "JOIN FETCH oi.product " +
      "WHERE oi.orderId = :orderId")
  List<OrderItem> findByOrderIdWithProduct(@Param("orderId") UUID orderId);
}
