package es.bytescolab.ms_accounts.account.services;

import es.bytescolab.ms_accounts.account.dto.request.CreateAccountRequest;
import es.bytescolab.ms_accounts.account.dto.response.AccountResponse;
import es.bytescolab.ms_accounts.account.dto.response.AccountSummaryResponse;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    AccountResponse createAccount(UUID customerId, CreateAccountRequest request);

    List<AccountSummaryResponse> getAccountsByCustomerId(UUID customerId);
}
