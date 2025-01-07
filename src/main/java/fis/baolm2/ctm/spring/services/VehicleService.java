package fis.baolm2.ctm.spring.services;

import fis.baolm2.ctm.spring.models.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleService {

    List<Vehicle> getAllVehicles();

    Optional<Vehicle> findVehicleById(String id);

    Optional<Vehicle> addVehicle(Vehicle vehicle);

    Optional<Vehicle> updateVehicle(Vehicle vehicle);

    boolean deleteVehicle(String vehicleId);

    List<Vehicle> getAllVehiclesByUserId(String userId);
}
