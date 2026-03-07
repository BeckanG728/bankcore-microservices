package es.bytescolab.ms_accounts.transfer.services.impl;

import es.bytescolab.ms_accounts.account.entity.Account;
import es.bytescolab.ms_accounts.account.enums.AccountStatus;
import es.bytescolab.ms_accounts.account.repository.AccountRepository;
import es.bytescolab.ms_accounts.feign.CustomerFeignClient;
import es.bytescolab.ms_accounts.feign.dto.CustomerResponse;
import es.bytescolab.ms_accounts.transaction.dto.response.TransactionResponse;
import es.bytescolab.ms_accounts.transaction.entity.Transaction;
import es.bytescolab.ms_accounts.transaction.enums.TransactionType;
import es.bytescolab.ms_accounts.transaction.mapper.TransactionMapper;
import es.bytescolab.ms_accounts.transaction.repository.TransactionRepository;
import es.bytescolab.ms_accounts.transfer.dto.request.TransferRequest;
import es.bytescolab.ms_accounts.transfer.dto.response.TransferResponse;
import es.bytescolab.ms_accounts.transfer.entity.Transfer;
import es.bytescolab.ms_accounts.transfer.mapper.TransferMapper;
import es.bytescolab.ms_accounts.transfer.repository.TransferRepository;
import es.bytescolab.ms_accounts.transfer.services.TransferService;
import es.bytescolab.ms_accounts.utils.exception.AccountNotFoundException;
import es.bytescolab.ms_accounts.utils.exception.InsufficientFundsException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;
    private final TransactionRepository transactionRepository;
    private final TransferMapper transferMapper;
    private final TransactionMapper transactionMapper;
    private final CustomerFeignClient customerFeignClient;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public TransferResponse transfer(UUID customerId, TransferRequest request) {
        log.info("Transferencia solicitada: origen={}, destino={}, monto={}, cliente={}", 
                request.originAccountId(), request.destinationAccountNumber(), request.amount(), customerId);

        // 1. Validar ownership de cuenta origen
        Account originAccount = accountRepository.findByIdAndCustomerId(
                request.originAccountId(), customerId
        ).orElseThrow(() -> new AccountNotFoundException(
                "No se encontró la cuenta origen para este cliente"
        ));

        if (originAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalArgumentException("La cuenta origen no está activa");
        }

        // 2. Validar saldo suficiente
        if (originAccount.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException(
                    "Saldo insuficiente. Saldo disponible: " + originAccount.getBalance()
            );
        }

        // 3. Validar límite diario de retiro
        BigDecimal dailyLimit = originAccount.getDailyWithdrawalLimit();
        if (dailyLimit != null && request.amount().compareTo(dailyLimit) > 0) {
            throw new IllegalArgumentException(
                    "El monto supera el límite diario de retiro: " + dailyLimit
            );
        }

        // 4. Buscar cuenta destino por número (IBAN)
        Account destinationAccount = accountRepository.findByAccountNumber(request.destinationAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException(
                        "No existe una cuenta con el número proporcionado: " + request.destinationAccountNumber()
                ));

        if (destinationAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalArgumentException("La cuenta destino no está activa");
        }

        // 5. Obtener nombre del beneficiario si es cliente interno
        String beneficiaryName = getBeneficiaryName(destinationAccount.getCustomerId());
        log.info("Beneficiario: {} (cliente: {})", beneficiaryName, destinationAccount.getCustomerId());

        // 6. Actualizar saldos
        originAccount.setBalance(originAccount.getBalance().subtract(request.amount()));
        destinationAccount.setBalance(destinationAccount.getBalance().add(request.amount()));
        accountRepository.save(originAccount);
        accountRepository.save(destinationAccount);
        entityManager.flush(); // Forzar escritura inmediata en BD
        log.info("Saldos actualizados: origen={}, destino={}",
                originAccount.getBalance(), destinationAccount.getBalance());

        // 7. Registrar transacciones (TRANSFER_OUT en origen, TRANSFER_IN en destino)
        Transaction debitTransaction = Transaction.builder()
                .account(originAccount)
                .type(TransactionType.TRANSFER_OUT)
                .amount(request.amount())
                .description(request.description() != null ? request.description() : "Transferencia a " + request.destinationAccountNumber())
                .build();
        transactionRepository.save(debitTransaction);

        Transaction creditTransaction = Transaction.builder()
                .account(destinationAccount)
                .type(TransactionType.TRANSFER_IN)
                .amount(request.amount())
                .description(request.description() != null ? request.description() : "Transferencia recibida de " + originAccount.getAccountNumber())
                .build();
        transactionRepository.save(creditTransaction);
        log.info("Transacciones registradas: debit={}, credit={}", 
                debitTransaction.getId(), creditTransaction.getId());

        // 8. Registrar transferencia
        Transfer transfer = Transfer.builder()
                .originAccount(originAccount)
                .destinationAccount(destinationAccount)
                .amount(request.amount())
                .description(request.description())
                .build();

        Transfer savedTransfer = transferRepository.save(transfer);
        log.info("Transferencia registrada: {}", savedTransfer.getId());

        TransferResponse response = transferMapper.toResponse(savedTransfer);
        // Retornar response con nombre del beneficiario
        return new TransferResponse(
                response.id(),
                response.originAccountId(),
                response.originAccountNumber(),
                response.destinationAccountId(),
                response.destinationAccountNumber(),
                beneficiaryName,
                response.amount(),
                response.description(),
                response.createdAt()
        );
    }

    private String getBeneficiaryName(UUID customerId) {
        try {
            log.debug("Llamando a ms-customers para obtener cliente: {}", customerId);
            CustomerResponse customer = customerFeignClient.getCustomer(customerId);
            log.debug("Cliente obtenido: {}", customer);
            if (customer == null) {
                log.warn("Cliente {} no encontrado (null response)", customerId);
                return "Cliente externo";
            }
            String name = customer.getFullName();
            log.debug("Nombre del beneficiario: {}", name);
            return name != null ? name : "Cliente externo";
        } catch (Exception e) {
            log.error("Error al obtener información del cliente {}: {}", customerId, e.getMessage(), e);
            return "Cliente externo";
        }
    }
}
