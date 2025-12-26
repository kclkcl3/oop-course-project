package virtualstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import virtualstore.entity.Review;
import virtualstore.service.ReviewService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

  @Autowired
  private ReviewService reviewService;

  @PostMapping
  public ResponseEntity<Review> createReview(@RequestBody Review review) {
    Review created = reviewService.createReview(review);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<Review>> getAllReviews() {
    return ResponseEntity.ok(reviewService.getAllReviews());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Review> getReviewById(@PathVariable UUID id) {
    return reviewService.getReviewById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/product/{productId}")
  public ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable UUID productId) {
    return ResponseEntity.ok(reviewService.getReviewsByProductId(productId));
  }

  @GetMapping("/customer/{customerId}")
  public ResponseEntity<List<Review>> getReviewsByCustomerId(@PathVariable UUID customerId) {
    return ResponseEntity.ok(reviewService.getReviewsByCustomerId(customerId));
  }

  @GetMapping("/product/{productId}/average")
  public ResponseEntity<Double> getAverageRating(@PathVariable UUID productId) {
    return ResponseEntity.ok(reviewService.getAverageRating(productId));
  }

  @GetMapping("/product/{productId}/count")
  public ResponseEntity<Long> getReviewCount(@PathVariable UUID productId) {
    return ResponseEntity.ok(reviewService.getReviewCount(productId));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Review> updateReview(@PathVariable UUID id, @RequestBody Review review) {
    try {
      Review updated = reviewService.updateReview(id, review);
      return ResponseEntity.ok(updated);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteReview(@PathVariable UUID id) {
    reviewService.deleteReview(id);
    return ResponseEntity.noContent().build();
  }
}
