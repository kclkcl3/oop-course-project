package virtualstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import virtualstore.entity.Review;
import virtualstore.repository.ReviewRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewService {

  @Autowired
  private ReviewRepository reviewRepository;

  @Autowired
  private ProductService productService;

  // CREATE - Создать отзыв
  @Transactional
  public Review createReview(Review review) {
    Review savedReview = reviewRepository.save(review);
    // Автоматически обновляем рейтинг товара
    productService.updateProductRating(review.getProductId());
    return savedReview;
  }

  // READ - Все отзывы
  public List<Review> getAllReviews() {
    return reviewRepository.findAll();
  }

  // READ - Отзыв по ID
  public Optional<Review> getReviewById(UUID id) {
    return reviewRepository.findById(id);
  }

  // READ - Отзывы на товар
  public List<Review> getReviewsByProductId(UUID productId) {
    return reviewRepository.findByProductId(productId);
  }

  // READ - Отзывы покупателя
  public List<Review> getReviewsByCustomerId(UUID customerId) {
    return reviewRepository.findByCustomerId(customerId);
  }

  // READ - Средний рейтинг товара
  public Double getAverageRating(UUID productId) {
    Double avg = reviewRepository.getAverageRatingByProduct(productId);
    return avg != null ? avg : 0.0;
  }

  // READ - Количество отзывов на товар
  public long getReviewCount(UUID productId) {
    return reviewRepository.countByProductId(productId);
  }

  // UPDATE - Обновить отзыв
  @Transactional
  public Review updateReview(UUID id, Review updatedReview) {
    return reviewRepository.findById(id)
        .map(review -> {
          review.setRating(updatedReview.getRating());
          review.setComment(updatedReview.getComment());
          Review saved = reviewRepository.save(review);
          // Пересчитываем рейтинг товара
          productService.updateProductRating(review.getProductId());
          return saved;
        })
        .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
  }

  // DELETE - Удалить отзыв
  @Transactional
  public void deleteReview(UUID id) {
    reviewRepository.findById(id).ifPresent(review -> {
      UUID productId = review.getProductId();
      reviewRepository.deleteById(id);
      // Пересчитываем рейтинг товара после удаления
      productService.updateProductRating(productId);
    });
  }
}
