package es.bytescolab.ms_accounts.transaction.repository;

import es.bytescolab.ms_accounts.transaction.entity.Transaction;
import es.bytescolab.ms_accounts.transaction.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {
    List<Transaction> findByAccountId(UUID accountId);

    @Query("SELECT t FROM Transaction t WHERE t.account.id = :accountId " +
           "AND (:startDate IS NULL OR t.createdAt >= :startDate) " +
           "AND (:endDate IS NULL OR t.createdAt <= :endDate) " +
           "AND (:type IS NULL OR t.type = :type)")
    Page<Transaction> findByAccountIdWithFilters(
            @Param("accountId") UUID accountId,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            @Param("type") TransactionType type,
            Pageable pageable
    );
}
