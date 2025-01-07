package fis.baolm2.ctm.spring.dtos.payload;

public record VehiclePayload(
        String userId,
        String brand,
        String model,
        String registrationPlate
) {
}
