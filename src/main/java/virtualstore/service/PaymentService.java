package virtualstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import virtualstore.entity.Payment;
import virtualstore.entity.Payment.PaymentStatus;
import virtualstore.repository.PaymentRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

  @Autowired
  private PaymentRepository paymentRepository;

  // CREATE - Создать платёж
  @Transactional
  public Payment createPayment(Payment payment) {
    return paymentRepository.save(payment);
  }

  // READ - Все платежи
  public List<Payment> getAllPayments() {
    return paymentRepository.findAll();
  }

  // READ - Платёж по ID
  public Optional<Payment> getPaymentById(UUID id) {
    return paymentRepository.findById(id);
  }

  // READ - Платежи заказа
  public List<Payment> getPaymentsByOrderId(UUID orderId) {
    return paymentRepository.findByOrderId(orderId);
  }

  // READ - Платежи по статусу
  public List<Payment> getPaymentsByStatus(PaymentStatus status) {
    return paymentRepository.findByStatus(status);
  }

  // UPDATE - Обновить статус платежа
  @Transactional
  public Payment updatePaymentStatus(UUID id, PaymentStatus newStatus) {
    return paymentRepository.findById(id)
        .map(payment -> {
          payment.setStatus(newStatus);
          return paymentRepository.save(payment);
        })
        .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
  }

  // DELETE - Удалить платёж
  @Transactional
  public void deletePayment(UUID id) {
    paymentRepository.deleteById(id);
  }
}
