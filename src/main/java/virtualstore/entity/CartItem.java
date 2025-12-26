package virtualstore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cart_item_id")
  private Long cartItemId;

  @Column(name = "cart_id", nullable = false)
  private UUID cartId;

  @Column(name = "product_id", nullable = false)
  private UUID productId;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @Column(name = "price", precision = 10, scale = 2, nullable = false)
  private BigDecimal price;

  // Связь N:1 - много позиций принадлежит одной корзине
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cart_id", insertable = false, updatable = false)
  @JsonIgnore
  private Cart cart;

  // Связь N:1 - много позиций ссылается на один товар
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", insertable = false, updatable = false)
  @JsonIgnore
  private Product product;
}
