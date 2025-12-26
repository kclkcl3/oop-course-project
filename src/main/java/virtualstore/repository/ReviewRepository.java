package virtualstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import virtualstore.entity.Review;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

  List<Review> findByProductId(UUID productId);

  List<Review> findByCustomerId(UUID customerId);

  List<Review> findByRatingGreaterThanEqual(BigDecimal rating);

  @Query("SELECT AVG(r.rating) FROM Review r WHERE r.productId = :productId")
  Double getAverageRatingByProduct(@Param("productId") UUID productId);

  long countByProductId(UUID productId);
}
