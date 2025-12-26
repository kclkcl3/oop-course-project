package virtualstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import virtualstore.entity.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

  // Поиск по email
  Optional<Customer> findByEmail(String email);

  // Поиск по имени и фамилии (игнорируя регистр)
  List<Customer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
      String firstName, String lastName);

  // Проверка существования email
  boolean existsByEmail(String email);
}
