package es.bytescolab.ms_accounts.transfer.repository;

import es.bytescolab.ms_accounts.transfer.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {
    List<Transfer> findByOriginAccountId(UUID accountId);
    List<Transfer> findByDestinationAccountId(UUID accountId);
}
