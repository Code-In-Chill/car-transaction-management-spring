package fis.baolm2.ctm.spring.repos;

import fis.baolm2.ctm.spring.models.Transaction;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findAllByUserId(@NotBlank(message = "User ID is required") String userId);
}