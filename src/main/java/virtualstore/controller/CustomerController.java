package virtualstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import virtualstore.entity.Customer;
import virtualstore.service.CustomerService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

  @Autowired
  private CustomerService customerService;

  // CREATE - POST /api/customers
  @PostMapping
  public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
    try {
      Customer created = customerService.createCustomer(customer);
      return new ResponseEntity<>(created, HttpStatus.CREATED);
    } catch (RuntimeException e) {
      return new ResponseEntity<>(null, HttpStatus.CONFLICT);
    }
  }

  // READ - GET /api/customers (все покупатели)
  @GetMapping
  public ResponseEntity<List<Customer>> getAllCustomers() {
    List<Customer> customers = customerService.getAllCustomers();
    return ResponseEntity.ok(customers);
  }

  // READ - GET /api/customers/{id}
  @GetMapping("/{id}")
  public ResponseEntity<Customer> getCustomerById(@PathVariable UUID id) {
    return customerService.getCustomerById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  // READ - GET /api/customers/email/{email}
  @GetMapping("/email/{email}")
  public ResponseEntity<Customer> getCustomerByEmail(@PathVariable String email) {
    return customerService.getCustomerByEmail(email)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  // READ - GET /api/customers/search?query=
  @GetMapping("/search")
  public ResponseEntity<List<Customer>> searchCustomers(@RequestParam String query) {
    List<Customer> customers = customerService.searchCustomers(query);
    return ResponseEntity.ok(customers);
  }

  // UPDATE - PUT /api/customers/{id}
  @PutMapping("/{id}")
  public ResponseEntity<Customer> updateCustomer(
      @PathVariable UUID id,
      @RequestBody Customer customer) {
    try {
      Customer updated = customerService.updateCustomer(id, customer);
      return ResponseEntity.ok(updated);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // DELETE - DELETE /api/customers/{id}
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
    try {
      customerService.deleteCustomer(id);
      return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
