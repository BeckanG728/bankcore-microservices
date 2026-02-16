package es.bytescolab.ms_customers.entity;

import es.bytescolab.ms_customers.utils.enums.CustomerStatus;
import es.bytescolab.ms_customers.utils.enums.UserRole;
import jakarta.persistence.Entity;

import java.time.Instant;
import java.util.UUID;

@Entity
public class Customer {
    private UUID id;
    private String dni;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String addres;
    private Enum<CustomerStatus> status;
    private Enum<UserRole> role;
    private Instant createdAt;
    private Instant updatedAt;
}
