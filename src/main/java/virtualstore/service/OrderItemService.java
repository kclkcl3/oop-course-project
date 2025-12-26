package virtualstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import virtualstore.entity.OrderItem;
import virtualstore.repository.OrderItemRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderItemService {

  @Autowired
  private OrderItemRepository orderItemRepository;

  // CREATE - Добавить позицию в заказ
  @Transactional
  public OrderItem createOrderItem(OrderItem orderItem) {
    return orderItemRepository.save(orderItem);
  }

  // READ - Все позиции заказов
  public List<OrderItem> getAllOrderItems() {
    return orderItemRepository.findAll();
  }

  // READ - Позиция по ID
  public Optional<OrderItem> getOrderItemById(Long id) {
    return orderItemRepository.findById(id);
  }

  // READ - Позиции конкретного заказа
  public List<OrderItem> getOrderItemsByOrderId(UUID orderId) {
    return orderItemRepository.findByOrderId(orderId);
  }

  // READ - Позиции с информацией о товаре (оптимизированный запрос)
  public List<OrderItem> getOrderItemsWithProduct(UUID orderId) {
    return orderItemRepository.findByOrderIdWithProduct(orderId);
  }

  // READ - Все заказы с конкретным товаром
  public List<OrderItem> getOrderItemsByProductId(UUID productId) {
    return orderItemRepository.findByProductId(productId);
  }

  // READ - Общее количество проданных единиц товара
  public Long getTotalSoldQuantity(UUID productId) {
    Long total = orderItemRepository.getTotalQuantitySoldByProduct(productId);
    return total != null ? total : 0L;
  }

  // READ - Самые популярные товары
  public List<Object[]> getMostPopularProducts() {
    return orderItemRepository.findMostPopularProducts();
  }

  // UPDATE - Обновить позицию заказа
  @Transactional
  public OrderItem updateOrderItem(Long id, OrderItem updatedItem) {
    return orderItemRepository.findById(id)
        .map(item -> {
          item.setProductId(updatedItem.getProductId());
          item.setQuantity(updatedItem.getQuantity());
          item.setPrice(updatedItem.getPrice());
          return orderItemRepository.save(item);
        })
        .orElseThrow(() -> new RuntimeException("OrderItem not found with id: " + id));
  }

  // DELETE - Удалить позицию
  @Transactional
  public void deleteOrderItem(Long id) {
    if (!orderItemRepository.existsById(id)) {
      throw new RuntimeException("OrderItem not found with id: " + id);
    }
    orderItemRepository.deleteById(id);
  }

  // DELETE - Удалить все позиции заказа
  @Transactional
  public void deleteOrderItemsByOrderId(UUID orderId) {
    orderItemRepository.deleteByOrderId(orderId);
  }
}
