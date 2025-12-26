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
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

  @Id
  @Column(name = "payment_id")
  private UUID paymentId;

  @Column(name = "order_id", nullable = false)
  private UUID orderId;

  @Column(name = "payment_date", nullable = false)
  private LocalDateTime paymentDate;

  @Column(name = "amount", precision = 10, scale = 2, nullable = false)
  private BigDecimal amount;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private PaymentStatus status;

  // Связь N:1 - много платежей к одному заказу
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", insertable = false, updatable = false)
  @JsonIgnore
  private Order order;

  @PrePersist
  public void prePersist() {
    if (paymentId == null) {
      paymentId = UUID.randomUUID();
    }
    if (paymentDate == null) {
      paymentDate = LocalDateTime.now();
    }
    if (status == null) {
      status = PaymentStatus.PENDING;
    }
  }

  public enum PaymentStatus {
    PENDING, // В ожидании
    COMPLETED, // Завершён
    FAILED // Ошибка
  }
}
