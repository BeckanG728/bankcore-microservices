package es.bytescolab.ms_customers.repository;

import es.bytescolab.ms_customers.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<UUID, Customer> {
    
}
