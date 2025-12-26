package virtualstore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

  @Id
  @Column(name = "customer_id")
  private UUID customerId;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Column(name = "phone")
  private String phone;

  @Column(name = "address")
  private String address;

  @Column(name = "registration_date")
  private LocalDate registrationDate;

  // Связь 1:N - один покупатель может иметь много заказов
  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List<Order> orders;

  @PrePersist
  public void prePersist() {
    if (customerId == null) {
      customerId = UUID.randomUUID();
    }
    if (registrationDate == null) {
      registrationDate = LocalDate.now();
    }
  }
}
