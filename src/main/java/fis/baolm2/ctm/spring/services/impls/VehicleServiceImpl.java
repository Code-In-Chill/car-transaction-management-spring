package fis.baolm2.ctm.spring.services.impls;

import fis.baolm2.ctm.spring.models.Vehicle;
import fis.baolm2.ctm.spring.repos.VehicleRepository;
import fis.baolm2.ctm.spring.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vr;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vr) {
        this.vr = vr;
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return vr.findAll();
    }

    @Override
    public Optional<Vehicle> findVehicleById(String vehicleId) {
        return vr.findById(vehicleId);
    }

    @Override
    public Optional<Vehicle> addVehicle(Vehicle vehicle) {
        return Optional.of(vr.save(vehicle));
    }

    @Override
    public Optional<Vehicle> updateVehicle(Vehicle vehicle) {
        return Optional.of(vr.save(vehicle));
    }

    @Override
    public boolean deleteVehicle(String vehicleId) {
        vr.deleteById(vehicleId);
        return true;
    }

    @Override
    public List<Vehicle> getAllVehiclesByUserId(String userId) {
        return vr.findAllByUserId(userId);
    }
}
