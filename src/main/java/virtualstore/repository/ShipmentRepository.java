package virtualstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import virtualstore.entity.Shipment;
import virtualstore.entity.Shipment.ShipmentStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, UUID> {

  List<Shipment> findByOrderId(UUID orderId);

  List<Shipment> findByStatus(ShipmentStatus status);

  Optional<Shipment> findByTrackingNumber(String trackingNumber);
}
