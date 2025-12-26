package virtualstore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

  @Id
  @Column(name = "product_id")
  private UUID productId;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "category")
  private String category;

  @Column(name = "price", precision = 10, scale = 2, nullable = false)
  private BigDecimal price;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @Column(name = "rating", precision = 3, scale = 1)
  private BigDecimal rating;

  // Связь 1:N - один товар может быть во многих позициях заказов
  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<OrderItem> orderItems;

  @PrePersist
  public void prePersist() {
    if (productId == null) {
      productId = UUID.randomUUID();
    }
    if (quantity == null) {
      quantity = 0;
    }
  }
}
