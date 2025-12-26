package virtualstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import virtualstore.entity.Shipment;
import virtualstore.entity.Shipment.ShipmentStatus;
import virtualstore.repository.ShipmentRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ShipmentService {

  @Autowired
  private ShipmentRepository shipmentRepository;

  // CREATE - Создать отправку
  @Transactional
  public Shipment createShipment(Shipment shipment) {
    return shipmentRepository.save(shipment);
  }

  // READ - Все отправки
  public List<Shipment> getAllShipments() {
    return shipmentRepository.findAll();
  }

  // READ - Отправка по ID
  public Optional<Shipment> getShipmentById(UUID id) {
    return shipmentRepository.findById(id);
  }

  // READ - Отправки заказа
  public List<Shipment> getShipmentsByOrderId(UUID orderId) {
    return shipmentRepository.findByOrderId(orderId);
  }

  // READ - Отправки по статусу
  public List<Shipment> getShipmentsByStatus(ShipmentStatus status) {
    return shipmentRepository.findByStatus(status);
  }

  // READ - Отправка по трек-номеру
  public Optional<Shipment> getShipmentByTrackingNumber(String trackingNumber) {
    return shipmentRepository.findByTrackingNumber(trackingNumber);
  }

  // UPDATE - Обновить статус отправки
  @Transactional
  public Shipment updateShipmentStatus(UUID id, ShipmentStatus newStatus) {
    return shipmentRepository.findById(id)
        .map(shipment -> {
          shipment.setStatus(newStatus);
          return shipmentRepository.save(shipment);
        })
        .orElseThrow(() -> new RuntimeException("Shipment not found with id: " + id));
  }

  // DELETE - Удалить отправку
  @Transactional
  public void deleteShipment(UUID id) {
    shipmentRepository.deleteById(id);
  }
}
