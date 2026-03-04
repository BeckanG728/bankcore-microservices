package es.bytescolab.ms_accounts.account.services;

import es.bytescolab.ms_accounts.account.dto.request.CreateAccountRequest;
import es.bytescolab.ms_accounts.account.dto.response.AccountResponse;

import java.util.UUID;

public interface AccountService {
    AccountResponse createAccount(UUID customerId, CreateAccountRequest request);
}
