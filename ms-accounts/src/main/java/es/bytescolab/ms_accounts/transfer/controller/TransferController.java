package es.bytescolab.ms_accounts.transfer.controller;

import es.bytescolab.ms_accounts.transfer.dto.request.TransferRequest;
import es.bytescolab.ms_accounts.transfer.dto.response.TransferResponse;
import es.bytescolab.ms_accounts.transfer.services.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferResponse> transfer(
            @RequestHeader("X-User-Id") UUID customerId,
            @Valid @RequestBody TransferRequest request
    ) {
        log.info("POST /api/transfers — customerId: {}", customerId);
        TransferResponse response = transferService.transfer(customerId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
