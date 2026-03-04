package es.bytescolab.ms_accounts.account.repository;

import es.bytescolab.ms_accounts.account.entity.Account;
import es.bytescolab.ms_accounts.account.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    long countByCustomerIdAndStatusNot(UUID customerId, AccountStatus status);

    List<Account> findByCustomerId(UUID customerId);

    boolean existsByAccountNumber(String accountNumber);
}
