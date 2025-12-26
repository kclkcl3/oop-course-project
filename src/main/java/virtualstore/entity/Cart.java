package virtualstore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

  @Id
  @Column(name = "cart_id")
  private UUID cartId;

  @Column(name = "customer_id", nullable = false)
  private UUID customerId;

  // Связь N:1 - много корзин принадлежит одному покупателю
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", insertable = false, updatable = false)
  @JsonIgnore
  private Customer customer;

  // Связь 1:N - одна корзина содержит много позиций
  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CartItem> items;

  @PrePersist
  public void prePersist() {
    if (cartId == null) {
      cartId = UUID.randomUUID();
    }
  }
}
