package virtualstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import virtualstore.entity.Payment;
import virtualstore.entity.Payment.PaymentStatus;
import virtualstore.service.PaymentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

  @Autowired
  private PaymentService paymentService;

  @PostMapping
  public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
    Payment created = paymentService.createPayment(payment);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<Payment>> getAllPayments() {
    return ResponseEntity.ok(paymentService.getAllPayments());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Payment> getPaymentById(@PathVariable UUID id) {
    return paymentService.getPaymentById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/order/{orderId}")
  public ResponseEntity<List<Payment>> getPaymentsByOrderId(@PathVariable UUID orderId) {
    return ResponseEntity.ok(paymentService.getPaymentsByOrderId(orderId));
  }

  @GetMapping("/status/{status}")
  public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
    return ResponseEntity.ok(paymentService.getPaymentsByStatus(status));
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<Payment> updatePaymentStatus(@PathVariable UUID id, @RequestParam PaymentStatus status) {
    try {
      Payment updated = paymentService.updatePaymentStatus(id, status);
      return ResponseEntity.ok(updated);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePayment(@PathVariable UUID id) {
    paymentService.deletePayment(id);
    return ResponseEntity.noContent().build();
  }
}
