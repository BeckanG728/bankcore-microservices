package es.bytescolab.ms_customers.customer.repository;

import es.bytescolab.ms_customers.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    Optional<Customer> findByDni(String dni);

    Optional<Customer> findByEmail(String email);

    boolean existsByDni(String dni);

    boolean existsByEmail(String email);
}
