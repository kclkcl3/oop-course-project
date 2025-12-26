package virtualstore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

  @Id
  @Column(name = "review_id")
  private UUID reviewId;

  @Column(name = "product_id", nullable = false)
  private UUID productId;

  @Column(name = "customer_id", nullable = false)
  private UUID customerId;

  @Column(name = "rating", precision = 3, scale = 1, nullable = false)
  private BigDecimal rating;

  @Column(name = "comment", columnDefinition = "TEXT")
  private String comment;

  @Column(name = "review_date", nullable = false)
  private LocalDateTime reviewDate;

  // Связь N:1 - много отзывов к одному товару
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", insertable = false, updatable = false)
  @JsonIgnore
  private Product product;

  // Связь N:1 - много отзывов от одного покупателя
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", insertable = false, updatable = false)
  @JsonIgnore
  private Customer customer;

  @PrePersist
  public void prePersist() {
    if (reviewId == null) {
      reviewId = UUID.randomUUID();
    }
    if (reviewDate == null) {
      reviewDate = LocalDateTime.now();
    }
  }
}
