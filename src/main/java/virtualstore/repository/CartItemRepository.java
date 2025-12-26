package virtualstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import virtualstore.entity.CartItem;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

  List<CartItem> findByCartId(UUID cartId);

  List<CartItem> findByProductId(UUID productId);

  void deleteByCartId(UUID cartId);
}
