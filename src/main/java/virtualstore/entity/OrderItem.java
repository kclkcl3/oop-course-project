package virtualstore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_item_id")
  private Long orderItemId;

  @Column(name = "order_id", nullable = false)
  private UUID orderId;

  @Column(name = "product_id", nullable = false)
  private UUID productId;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  // Цена фиксируется на момент заказа (может отличаться от текущей цены товара)
  @Column(name = "price", precision = 10, scale = 2, nullable = false)
  private BigDecimal price;

  // Связь N:1 - много позиций принадлежит одному заказу
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", insertable = false, updatable = false)
  @JsonIgnore
  private Order order;

  // Связь N:1 - много позиций ссылается на один товар
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", insertable = false, updatable = false)
  @JsonIgnore
  private Product product;
}
