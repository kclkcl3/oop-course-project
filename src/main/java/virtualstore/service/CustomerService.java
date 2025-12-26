package virtualstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import virtualstore.entity.Customer;
import virtualstore.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

  @Autowired
  private CustomerRepository customerRepository;

  // CREATE - Создание нового покупателя
  @Transactional
  public Customer createCustomer(Customer customer) {
    // Проверка на дубликат email
    if (customerRepository.existsByEmail(customer.getEmail())) {
      throw new RuntimeException("Customer with email " + customer.getEmail() + " already exists");
    }
    return customerRepository.save(customer);
  }

  // READ - Получить всех покупателей
  public List<Customer> getAllCustomers() {
    return customerRepository.findAll();
  }

  // READ - Получить покупателя по ID
  public Optional<Customer> getCustomerById(UUID id) {
    return customerRepository.findById(id);
  }

  // READ - Поиск по email
  public Optional<Customer> getCustomerByEmail(String email) {
    return customerRepository.findByEmail(email);
  }

  // READ - Поиск по имени или фамилии
  public List<Customer> searchCustomers(String query) {
    return customerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
        query, query);
  }

  // UPDATE - Обновление данных покупателя
  @Transactional
  public Customer updateCustomer(UUID id, Customer updatedCustomer) {
    return customerRepository.findById(id)
        .map(customer -> {
          customer.setFirstName(updatedCustomer.getFirstName());
          customer.setLastName(updatedCustomer.getLastName());
          customer.setPhone(updatedCustomer.getPhone());
          customer.setAddress(updatedCustomer.getAddress());
          // Email не обновляем для безопасности
          return customerRepository.save(customer);
        })
        .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
  }

  // DELETE - Удаление покупателя
  @Transactional
  public void deleteCustomer(UUID id) {
    if (!customerRepository.existsById(id)) {
      throw new RuntimeException("Customer not found with id: " + id);
    }
    customerRepository.deleteById(id);
  }

  // Проверка существования email
  public boolean emailExists(String email) {
    return customerRepository.existsByEmail(email);
  }
}
