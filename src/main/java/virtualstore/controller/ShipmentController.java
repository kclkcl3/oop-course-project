package virtualstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import virtualstore.entity.Shipment;
import virtualstore.entity.Shipment.ShipmentStatus;
import virtualstore.service.ShipmentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

  @Autowired
  private ShipmentService shipmentService;

  @PostMapping
  public ResponseEntity<Shipment> createShipment(@RequestBody Shipment shipment) {
    Shipment created = shipmentService.createShipment(shipment);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<Shipment>> getAllShipments() {
    return ResponseEntity.ok(shipmentService.getAllShipments());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Shipment> getShipmentById(@PathVariable UUID id) {
    return shipmentService.getShipmentById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/order/{orderId}")
  public ResponseEntity<List<Shipment>> getShipmentsByOrderId(@PathVariable UUID orderId) {
    return ResponseEntity.ok(shipmentService.getShipmentsByOrderId(orderId));
  }

  @GetMapping("/status/{status}")
  public ResponseEntity<List<Shipment>> getShipmentsByStatus(@PathVariable ShipmentStatus status) {
    return ResponseEntity.ok(shipmentService.getShipmentsByStatus(status));
  }

  @GetMapping("/tracking/{trackingNumber}")
  public ResponseEntity<Shipment> getShipmentByTrackingNumber(@PathVariable String trackingNumber) {
    return shipmentService.getShipmentByTrackingNumber(trackingNumber)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<Shipment> updateShipmentStatus(@PathVariable UUID id, @RequestParam ShipmentStatus status) {
    try {
      Shipment updated = shipmentService.updateShipmentStatus(id, status);
      return ResponseEntity.ok(updated);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteShipment(@PathVariable UUID id) {
    shipmentService.deleteShipment(id);
    return ResponseEntity.noContent().build();
  }
}
