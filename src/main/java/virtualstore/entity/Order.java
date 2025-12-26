package virtualstore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

  @Id
  @Column(name = "order_id")
  private UUID orderId;

  @Column(name = "customer_id", nullable = false)
  private UUID customerId;

  @Column(name = "order_date", nullable = false)
  private LocalDateTime orderDate;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @Column(name = "total_amount", precision = 10, scale = 2)
  private BigDecimal totalAmount;

  // Связь N:1 - много заказов принадлежит одному покупателю
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", insertable = false, updatable = false)
  @JsonIgnore
  private Customer customer;

  // Связь 1:N - один заказ содержит много позиций товаров
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> orderItems;

  @PrePersist
  public void prePersist() {
    if (orderId == null) {
      orderId = UUID.randomUUID();
    }
    if (orderDate == null) {
      orderDate = LocalDateTime.now();
    }
    if (status == null) {
      status = OrderStatus.PENDING;
    }
  }

  // Enum для статусов заказа
  public enum OrderStatus {
    PENDING, // В ожидании
    SHIPPED, // Отправлен
    DELIVERED, // Доставлен
    CANCELLED // Отменён
  }
}
