package es.bytescolab.ms_accounts.transaction.services.impl;

import es.bytescolab.ms_accounts.account.entity.Account;
import es.bytescolab.ms_accounts.account.enums.AccountStatus;
import es.bytescolab.ms_accounts.account.repository.AccountRepository;
import es.bytescolab.ms_accounts.transaction.dto.response.TransactionResponse;
import es.bytescolab.ms_accounts.transaction.entity.Transaction;
import es.bytescolab.ms_accounts.transaction.enums.TransactionType;
import es.bytescolab.ms_accounts.transaction.mapper.TransactionMapper;
import es.bytescolab.ms_accounts.transaction.repository.TransactionRepository;
import es.bytescolab.ms_accounts.transaction.services.TransactionService;
import es.bytescolab.ms_accounts.utils.exception.AccountNotFoundException;
import es.bytescolab.ms_accounts.utils.exception.InsufficientFundsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    @Transactional
    public TransactionResponse deposit(UUID accountId, UUID customerId, BigDecimal amount, String description) {
        log.info("Depósito solicitado: cuenta={}, cliente={}, monto={}", accountId, customerId, amount);

        // 1. Validar ownership y estado de la cuenta
        Account account = accountRepository.findByIdAndCustomerId(accountId, customerId)
                .orElseThrow(() -> new AccountNotFoundException(
                        "No se encontró la cuenta proporcionada para este cliente"
                ));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalArgumentException("La cuenta no está activa para realizar depósitos");
        }

        // 2. Actualizar saldo
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);
        accountRepository.save(account);
        log.info("Saldo actualizado: {} + {} = {}", account.getBalance().subtract(amount), amount, newBalance);

        // 3. Registrar transacción
        Transaction transaction = Transaction.builder()
                .account(account)
                .type(TransactionType.DEPOSIT)
                .amount(amount)
                .description(description)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transacción de depósito registrada: {}", savedTransaction.getId());

        return transactionMapper.toResponse(savedTransaction);
    }

    @Override
    @Transactional
    public TransactionResponse withdraw(UUID accountId, UUID customerId, BigDecimal amount) {
        log.info("Retiro solicitado: cuenta={}, cliente={}, monto={}", accountId, customerId, amount);

        // 1. Validar ownership y estado de la cuenta
        Account account = accountRepository.findByIdAndCustomerId(accountId, customerId)
                .orElseThrow(() -> new AccountNotFoundException(
                        "No se encontró la cuenta proporcionada para este cliente"
                ));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalArgumentException("La cuenta no está activa para realizar retiros");
        }

        // 2. Validar saldo suficiente
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(
                    "Saldo insuficiente. Saldo disponible: " + account.getBalance()
            );
        }

        // 3. Validar límite diario de retiro
        // TODO: Validar límite acumulado diario (sumar retiros del día actual)
        BigDecimal dailyLimit = account.getDailyWithdrawalLimit();
        if (dailyLimit != null && amount.compareTo(dailyLimit) > 0) {
            throw new IllegalArgumentException(
                    "El monto supera el límite diario de retiro: " + dailyLimit
            );
        }

        // 4. Actualizar saldo
        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);
        accountRepository.save(account);
        log.info("Saldo actualizado: {} - {} = {}", account.getBalance().add(amount), amount, newBalance);

        // 5. Registrar transacción
        Transaction transaction = Transaction.builder()
                .account(account)
                .type(TransactionType.WITHDRAWAL)
                .amount(amount)
                .description("Retiro de efectivo")
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transacción de retiro registrada: {}", savedTransaction.getId());

        return transactionMapper.toResponse(savedTransaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByAccountId(UUID accountId, UUID customerId) {
        log.info("Consultando transacciones: cuenta={}, cliente={}", accountId, customerId);

        // Validar ownership
        Account account = accountRepository.findByIdAndCustomerId(accountId, customerId)
                .orElseThrow(() -> new AccountNotFoundException(
                        "No se encontró la cuenta proporcionada para este cliente"
                ));

        List<Transaction> transactions = transactionRepository.findByAccountId(account.getId());
        log.info("Encontradas {} transacciones para la cuenta {}", transactions.size(), accountId);

        return transactionMapper.toResponseList(transactions);
    }
}
