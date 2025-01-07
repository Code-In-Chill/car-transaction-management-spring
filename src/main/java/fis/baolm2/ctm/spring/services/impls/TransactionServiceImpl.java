package fis.baolm2.ctm.spring.services.impls;

import fis.baolm2.ctm.spring.models.Transaction;
import fis.baolm2.ctm.spring.repos.TransactionRepository;
import fis.baolm2.ctm.spring.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository tr;

    @Autowired
    public TransactionServiceImpl(TransactionRepository tr) {
        this.tr = tr;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return tr.findAll();
    }

    @Override
    public Optional<Transaction> addTransaction(Transaction transaction) {
        return Optional.of(tr.save(transaction));
    }

    @Override
    public List<Transaction> getAllTransactionsByUserId(String userId) {
        return tr.findAllByUserId(userId);
    }

    @Override
    public Optional<Transaction> findTransactionById(String id) {
        return tr.findById(id);
    }

    @Override
    public boolean deleteTransaction(String id) {
        tr.deleteById(id);
        return true;
    }

    @Override
    public Optional<Transaction> updateTransaction(Transaction transaction) {
        return Optional.of(tr.save(transaction));
    }
}
