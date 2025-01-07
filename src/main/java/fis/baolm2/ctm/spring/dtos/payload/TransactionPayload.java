package fis.baolm2.ctm.spring.dtos.payload;

public record TransactionPayload(
        Float amount,
        String carId,
        String userId,
        String category,
        String paidDate,
        String note
) {
}
