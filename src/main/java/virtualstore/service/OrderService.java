package virtualstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import virtualstore.entity.Order;
import virtualstore.entity.Order.OrderStatus;
import virtualstore.entity.OrderItem;
import virtualstore.repository.OrderRepository;
import virtualstore.repository.OrderItemRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private OrderItemRepository orderItemRepository;

  @Autowired
  private ProductService productService;

  // CREATE - Создание заказа
  @Transactional
  public Order createOrder(Order order) {
    // Автоматически рассчитываем общую сумму
    if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
      BigDecimal totalAmount = order.getOrderItems().stream()
          .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
          .reduce(BigDecimal.ZERO, BigDecimal::add);
      order.setTotalAmount(totalAmount);
    }

    // Сохраняем заказ
    Order savedOrder = orderRepository.save(order);

    // Уменьшаем остатки товаров
    if (order.getOrderItems() != null) {
      for (OrderItem item : order.getOrderItems()) {
        productService.decreaseStock(item.getProductId(), item.getQuantity());
      }
    }

    return savedOrder;
  }

  // READ - Все заказы
  public List<Order> getAllOrders() {
    return orderRepository.findAll();
  }

  // READ - Заказ по ID
  public Optional<Order> getOrderById(UUID id) {
    return orderRepository.findById(id);
  }

  // READ - Заказы покупателя
  public List<Order> getOrdersByCustomer(UUID customerId) {
    return orderRepository.findByCustomerId(customerId);
  }

  // READ - Заказы по статусу
  public List<Order> getOrdersByStatus(OrderStatus status) {
    return orderRepository.findByStatus(status);
  }

  // READ - Заказы покупателя по статусу
  public List<Order> getOrdersByCustomerAndStatus(UUID customerId, OrderStatus status) {
    return orderRepository.findByCustomerIdAndStatus(customerId, status);
  }

  // READ - Заказы за период
  public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
    return orderRepository.findByOrderDateBetween(startDate, endDate);
  }

  // READ - Заказы покупателя (сортировка по дате)
  public List<Order> getOrdersByCustomerSorted(UUID customerId) {
    return orderRepository.findByCustomerIdOrderByOrderDateDesc(customerId);
  }

  // READ - Количество заказов покупателя
  public long getOrderCountByCustomer(UUID customerId) {
    return orderRepository.countByCustomerId(customerId);
  }

  // READ - Общая сумма заказов покупателя
  public Double getTotalSpentByCustomer(UUID customerId) {
    Double total = orderRepository.getTotalAmountByCustomer(customerId);
    return total != null ? total : 0.0;
  }

  // UPDATE - Обновить статус заказа
  // UPDATE - Обновить статус заказа
  @Transactional
  public Order updateOrderStatus(UUID id, Order.OrderStatus newStatus) {
    return orderRepository.findById(id)
        .map(order -> {
          order.setStatus(newStatus);
          return orderRepository.save(order);
        })
        .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
  }

  // UPDATE - Полное обновление заказа
  @Transactional
  public Order updateOrder(UUID id, Order updatedOrder) {
    return orderRepository.findById(id)
        .map(order -> {
          order.setStatus(updatedOrder.getStatus());
          order.setTotalAmount(updatedOrder.getTotalAmount());
          return orderRepository.save(order);
        })
        .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
  }

  // DELETE - Отмена заказа (мягкое удаление через статус)
  @Transactional
  public Order cancelOrder(UUID id) {
    return updateOrderStatus(id, OrderStatus.CANCELLED);
  }

  // DELETE - Полное удаление заказа
  @Transactional
  public void deleteOrder(UUID id) {
    if (!orderRepository.existsById(id)) {
      throw new RuntimeException("Order not found with id: " + id);
    }
    orderRepository.deleteById(id);
  }

  // Для аналитики (линейная регрессия)
  public List<Order> getOrdersForAnalytics(LocalDateTime startDate) {
    return orderRepository.findOrdersForAnalytics(startDate);
  }

}
