package virtualstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import virtualstore.entity.Order;
import virtualstore.entity.Order.OrderStatus;
import virtualstore.service.OrderService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  @Autowired
  private OrderService orderService;

  // CREATE - POST /api/orders
  @PostMapping
  public ResponseEntity<Order> createOrder(@RequestBody Order order) {
    try {
      Order created = orderService.createOrder(order);
      return new ResponseEntity<>(created, HttpStatus.CREATED);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  // READ - GET /api/orders (все заказы)
  @GetMapping
  public ResponseEntity<List<Order>> getAllOrders() {
    List<Order> orders = orderService.getAllOrders();
    return ResponseEntity.ok(orders);
  }

  // READ - GET /api/orders/{id}
  @GetMapping("/{id}")
  public ResponseEntity<Order> getOrderById(@PathVariable UUID id) {
    return orderService.getOrderById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  // READ - GET /api/orders/customer/{customerId}
  @GetMapping("/customer/{customerId}")
  public ResponseEntity<List<Order>> getOrdersByCustomer(@PathVariable UUID customerId) {
    List<Order> orders = orderService.getOrdersByCustomer(customerId);
    return ResponseEntity.ok(orders);
  }

  // READ - GET /api/orders/status/{status}
  @GetMapping("/status/{status}")
  public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable OrderStatus status) {
    List<Order> orders = orderService.getOrdersByStatus(status);
    return ResponseEntity.ok(orders);
  }

  // READ - GET
  // /api/orders/date-range?start=2025-01-01T00:00:00&end=2025-12-31T23:59:59
  @GetMapping("/date-range")
  public ResponseEntity<List<Order>> getOrdersByDateRange(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
    List<Order> orders = orderService.getOrdersByDateRange(start, end);
    return ResponseEntity.ok(orders);
  }

  // READ - GET /api/orders/customer/{customerId}/stats
  @GetMapping("/customer/{customerId}/stats")
  public ResponseEntity<CustomerOrderStats> getCustomerStats(@PathVariable UUID customerId) {
    long count = orderService.getOrderCountByCustomer(customerId);
    Double totalSpent = orderService.getTotalSpentByCustomer(customerId);

    CustomerOrderStats stats = new CustomerOrderStats(count, totalSpent);
    return ResponseEntity.ok(stats);
  }

  // UPDATE - PATCH /api/orders/{id}/status?status=SHIPPED
  @PatchMapping("/{id}/status")
  public ResponseEntity<Order> updateOrderStatus(
      @PathVariable UUID id,
      @RequestParam OrderStatus status) {
    try {
      Order updated = orderService.updateOrderStatus(id, status);
      return ResponseEntity.ok(updated);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // UPDATE - PUT /api/orders/{id}
  @PutMapping("/{id}")
  public ResponseEntity<Order> updateOrder(
      @PathVariable UUID id,
      @RequestBody Order order) {
    try {
      Order updated = orderService.updateOrder(id, order);
      return ResponseEntity.ok(updated);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // DELETE - DELETE /api/orders/{id}/cancel (мягкое удаление)
  @DeleteMapping("/{id}/cancel")
  public ResponseEntity<Order> cancelOrder(@PathVariable UUID id) {
    try {
      Order cancelled = orderService.cancelOrder(id);
      return ResponseEntity.ok(cancelled);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // DELETE - DELETE /api/orders/{id} (полное удаление)
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
    try {
      orderService.deleteOrder(id);
      return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // DTO для статистики покупателя
  public static class CustomerOrderStats {
    public long orderCount;
    public Double totalSpent;

    public CustomerOrderStats(long orderCount, Double totalSpent) {
      this.orderCount = orderCount;
      this.totalSpent = totalSpent;
    }
  }
}
