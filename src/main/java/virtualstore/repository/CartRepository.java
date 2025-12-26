package virtualstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import virtualstore.entity.Cart;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {

  Optional<Cart> findByCustomerId(UUID customerId);

  boolean existsByCustomerId(UUID customerId);
}
