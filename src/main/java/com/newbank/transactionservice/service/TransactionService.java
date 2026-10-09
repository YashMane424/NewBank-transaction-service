package com.newbank.transactionservice.service;

import com.newbank.transactionservice.dto.TransactionResponse;
import com.newbank.transactionservice.dto.TransferRequest;
import com.newbank.transactionservice.entity.Transaction;
import com.newbank.transactionservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionResponse transfer(TransferRequest request) {
        log.info("Processing transfer from {} to {} amount {}", request.getFromAccountId(), request.getToAccountId(), request.getAmount());

        Transaction transaction = Transaction.builder()
                .transactionId(UUID.randomUUID().toString())
                .fromAccountId(request.getFromAccountId())
                .toAccountId(request.getToAccountId())
                .amount(request.getAmount())
                .type("TRANSFER")
                .status("SUCCESS")
                .description(request.getDescription() != null ? request.getDescription() : "Funds transfer")
                .build();

        Transaction saved = transactionRepository.save(transaction);
        return convertToResponse(saved);
    }

    public List<TransactionResponse> getHistory(Long accountId) {
        return transactionRepository.findByFromAccountIdOrToAccountId(accountId, accountId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private TransactionResponse convertToResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .transactionId(transaction.getTransactionId())
                .fromAccountId(transaction.getFromAccountId())
                .toAccountId(transaction.getToAccountId())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .status(transaction.getStatus())
                .description(transaction.getDescription())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }
}
