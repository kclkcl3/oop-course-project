package virtualstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import virtualstore.entity.OrderItem;
import virtualstore.service.OrderItemService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

  @Autowired
  private OrderItemService orderItemService;

  // CREATE - POST /api/order-items
  @PostMapping
  public ResponseEntity<OrderItem> createOrderItem(@RequestBody OrderItem orderItem) {
    OrderItem created = orderItemService.createOrderItem(orderItem);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  // READ - GET /api/order-items (все позиции)
  @GetMapping
  public ResponseEntity<List<OrderItem>> getAllOrderItems() {
    List<OrderItem> items = orderItemService.getAllOrderItems();
    return ResponseEntity.ok(items);
  }

  // READ - GET /api/order-items/{id}
  @GetMapping("/{id}")
  public ResponseEntity<OrderItem> getOrderItemById(@PathVariable Long id) {
    return orderItemService.getOrderItemById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  // READ - GET /api/order-items/order/{orderId}
  @GetMapping("/order/{orderId}")
  public ResponseEntity<List<OrderItem>> getOrderItemsByOrderId(@PathVariable UUID orderId) {
    List<OrderItem> items = orderItemService.getOrderItemsByOrderId(orderId);
    return ResponseEntity.ok(items);
  }

  // READ - GET /api/order-items/product/{productId}
  @GetMapping("/product/{productId}")
  public ResponseEntity<List<OrderItem>> getOrderItemsByProductId(@PathVariable UUID productId) {
    List<OrderItem> items = orderItemService.getOrderItemsByProductId(productId);
    return ResponseEntity.ok(items);
  }

  // READ - GET /api/order-items/product/{productId}/total-sold
  @GetMapping("/product/{productId}/total-sold")
  public ResponseEntity<Long> getTotalSoldQuantity(@PathVariable UUID productId) {
    Long total = orderItemService.getTotalSoldQuantity(productId);
    return ResponseEntity.ok(total);
  }

  // READ - GET /api/order-items/popular-products
  @GetMapping("/popular-products")
  public ResponseEntity<List<Object[]>> getMostPopularProducts() {
    List<Object[]> products = orderItemService.getMostPopularProducts();
    return ResponseEntity.ok(products);
  }

  // UPDATE - PUT /api/order-items/{id}
  @PutMapping("/{id}")
  public ResponseEntity<OrderItem> updateOrderItem(
      @PathVariable Long id,
      @RequestBody OrderItem orderItem) {
    try {
      OrderItem updated = orderItemService.updateOrderItem(id, orderItem);
      return ResponseEntity.ok(updated);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // DELETE - DELETE /api/order-items/{id}
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
    try {
      orderItemService.deleteOrderItem(id);
      return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // DELETE - DELETE /api/order-items/order/{orderId}
  @DeleteMapping("/order/{orderId}")
  public ResponseEntity<Void> deleteOrderItemsByOrderId(@PathVariable UUID orderId) {
    orderItemService.deleteOrderItemsByOrderId(orderId);
    return ResponseEntity.noContent().build();
  }
}
