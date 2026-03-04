package es.bytescolab.ms_accounts.account.mapper;

import es.bytescolab.ms_accounts.account.dto.response.AccountResponse;
import es.bytescolab.ms_accounts.account.dto.response.AccountSummaryResponse;
import es.bytescolab.ms_accounts.account.entity.Account;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountResponse toResponse(Account account);
    
    AccountSummaryResponse toSummaryResponse(Account account);

    List<AccountSummaryResponse> toSummaryResponseList(List<Account> accounts);
}
