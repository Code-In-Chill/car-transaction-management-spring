package fis.baolm2.ctm.spring.services;

import fis.baolm2.ctm.spring.models.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    List<Transaction> getAllTransactions();

    Optional<Transaction> addTransaction(Transaction transaction);

    List<Transaction> getAllTransactionsByUserId(String userId);

    Optional<Transaction> findTransactionById(String id);

    boolean deleteTransaction(String id);

    Optional<Transaction> updateTransaction(Transaction transaction);
}
