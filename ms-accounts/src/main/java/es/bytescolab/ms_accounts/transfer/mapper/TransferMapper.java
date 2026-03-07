package es.bytescolab.ms_accounts.transfer.mapper;

import es.bytescolab.ms_accounts.transfer.dto.response.TransferResponse;
import es.bytescolab.ms_accounts.transfer.entity.Transfer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransferMapper {
    @Mapping(source = "originAccount.id", target = "originAccountId")
    @Mapping(source = "originAccount.accountNumber", target = "originAccountNumber")
    @Mapping(source = "destinationAccount.id", target = "destinationAccountId")
    @Mapping(source = "destinationAccount.accountNumber", target = "destinationAccountNumber")
    TransferResponse toResponse(Transfer transfer);

    List<TransferResponse> toResponseList(List<Transfer> transfers);
}
