package fis.baolm2.ctm.spring.controllers;

import fis.baolm2.ctm.spring.dtos.payload.TransactionPayload;
import fis.baolm2.ctm.spring.models.Transaction;
import fis.baolm2.ctm.spring.models.Vehicle;
import fis.baolm2.ctm.spring.services.TransactionService;
import fis.baolm2.ctm.spring.services.VehicleService;
import fis.baolm2.ctm.spring.utils.UserUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService ts;
    private final VehicleService vs;

    @Autowired
    public TransactionController(TransactionService ts, VehicleService vs) {
        this.ts = ts;
        this.vs = vs;
    }

    @GetMapping({"", "/"})
    public ResponseEntity<?> getTransactions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userID = UserUtil.getUserId(authentication);
        return ResponseEntity.ok(ts.getAllTransactionsByUserId(userID));
    }

    @PutMapping("/create")
    public ResponseEntity<?> createTransaction(@Valid @RequestBody TransactionPayload transactionPayload, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result);
        }

        Optional<Vehicle> vehicle = vs.findVehicleById(transactionPayload.carId());
        if (vehicle.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userID = UserUtil.getUserId(authentication);

        DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate paidDate = LocalDate.parse(transactionPayload.paidDate(), DATEFORMATTER);

        Transaction transaction = new Transaction();
        transaction.setUserId(userID);
        transaction.setVehicle(vehicle.get());
        transaction.setAmount(transactionPayload.amount());
        transaction.setCategory(transactionPayload.category());
        transaction.setPaidDate(paidDate);
        transaction.setNote(transactionPayload.note());

        Optional<Transaction> createdTransaction = ts.addTransaction(transaction);

        if (createdTransaction.isPresent()) {
            return ResponseEntity.ok(createdTransaction.get());
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateTransaction(@PathVariable String id, @Valid @RequestBody TransactionPayload transactionPayload, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result);
        }

        Optional<Transaction> oTransaction = ts.findTransactionById(id);

        if (oTransaction.isPresent()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userID = UserUtil.getUserId(authentication);

            DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate paidDate = LocalDate.parse(transactionPayload.paidDate(), DATEFORMATTER);

            Transaction transaction = oTransaction.get();
            transaction.setUserId(userID);
            transaction.setAmount(transactionPayload.amount());
            transaction.setCategory(transactionPayload.category());
            transaction.setPaidDate(paidDate);
            transaction.setNote(transactionPayload.note());

            Optional<Transaction> updatedTransaction = ts.updateTransaction(transaction);
            if (updatedTransaction.isPresent()) {
                return ResponseEntity.ok(updatedTransaction.get());
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable String id) {
        Optional<Transaction> oTransaction = ts.findTransactionById(id);
        if (oTransaction.isPresent()) {
            boolean result = ts.deleteTransaction(id);
            if (result) {
                return ResponseEntity.ok(Map.of("result", true));
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
