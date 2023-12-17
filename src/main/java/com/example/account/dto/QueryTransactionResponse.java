package com.example.account.dto;

import com.example.account.type.TransactionType;
import com.example.account.type.TransactionalResultType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryTransactionResponse {

    private String accountNumber;
    private TransactionType transactionType;
    private TransactionalResultType transactionalResultType;
    private String transactionId;
    private Long amount;
    private LocalDateTime transactedAt;

    public static QueryTransactionResponse from(TransactionDto transactionDto){
        return QueryTransactionResponse.builder()
                .accountNumber(transactionDto.getAccountNumber())
                .transactionType(transactionDto.getTransactionType())
                .transactionalResultType(transactionDto.getTransactionalResultType())
                .amount(transactionDto.getAmount())
                .transactionId(transactionDto.getTransactionId())
                .transactedAt(transactionDto.getTransactedAt())
                .build();
    }
}
