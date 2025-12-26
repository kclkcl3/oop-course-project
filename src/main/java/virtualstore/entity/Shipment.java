package virtualstore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "shipments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {

  @Id
  @Column(name = "shipment_id")
  private UUID shipmentId;

  @Column(name = "order_id", nullable = false)
  private UUID orderId;

  @Column(name = "shipment_date", nullable = false)
  private LocalDateTime shipmentDate;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private ShipmentStatus status;

  @Column(name = "tracking_number")
  private String trackingNumber;

  // Связь N:1 - много отправок к одному заказу
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", insertable = false, updatable = false)
  @JsonIgnore
  private Order order;

  @PrePersist
  public void prePersist() {
    if (shipmentId == null) {
      shipmentId = UUID.randomUUID();
    }
    if (shipmentDate == null) {
      shipmentDate = LocalDateTime.now();
    }
    if (status == null) {
      status = ShipmentStatus.SHIPPED;
    }
  }

  public enum ShipmentStatus {
    SHIPPED, // Отправлен
    IN_TRANSIT, // В пути
    DELIVERED // Доставлен
  }
}
