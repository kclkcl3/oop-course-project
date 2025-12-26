package virtualstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import virtualstore.entity.Payment;
import virtualstore.entity.Payment.PaymentStatus;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

  List<Payment> findByOrderId(UUID orderId);

  List<Payment> findByStatus(PaymentStatus status);
}
