package es.bytescolab.ms_accounts.transfer.services;

import es.bytescolab.ms_accounts.transfer.dto.request.TransferRequest;
import es.bytescolab.ms_accounts.transfer.dto.response.TransferResponse;

import java.util.UUID;

public interface TransferService {
    TransferResponse transfer(UUID customerId, TransferRequest request);
}
