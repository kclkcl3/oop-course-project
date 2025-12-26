package virtualstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import virtualstore.entity.Order;
import virtualstore.entity.Order.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

  // Все заказы покупателя
  List<Order> findByCustomerId(UUID customerId);

  // Заказы по статусу
  List<Order> findByStatus(OrderStatus status);

  // Заказы покупателя по статусу
  List<Order> findByCustomerIdAndStatus(UUID customerId, OrderStatus status);

  // Заказы за период
  List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);

  // Заказы покупателя за период
  List<Order> findByCustomerIdAndOrderDateBetween(
      UUID customerId, LocalDateTime startDate, LocalDateTime endDate);

  // Количество заказов покупателя
  long countByCustomerId(UUID customerId);

  // Заказы, отсортированные по дате (новые сначала)
  List<Order> findByCustomerIdOrderByOrderDateDesc(UUID customerId);

  // Кастомный запрос: общая сумма заказов покупателя
  @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.customerId = :customerId")
  Double getTotalAmountByCustomer(@Param("customerId") UUID customerId);

  // Заказы для аналитики (для линейной регрессии)
  @Query("SELECT o FROM Order o WHERE o.orderDate >= :startDate ORDER BY o.orderDate ASC")
  List<Order> findOrdersForAnalytics(@Param("startDate") LocalDateTime startDate);
}
