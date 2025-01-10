package fis.baolm2.ctm.spring.controllers;

import fis.baolm2.ctm.spring.dtos.payload.VehiclePayload;
import fis.baolm2.ctm.spring.models.Vehicle;
import fis.baolm2.ctm.spring.services.VehicleService;
import fis.baolm2.ctm.spring.utils.UserUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    private final VehicleService vs;

    public VehicleController(VehicleService vs) {
        this.vs = vs;
    }

    @GetMapping({"", "/"})
    public ResponseEntity<?> getVehicles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userID = UserUtil.getUserId(authentication);
        return ResponseEntity.ok(vs.getAllVehiclesByUserId(userID));
    }

    @PutMapping("/create")
    public ResponseEntity<?> createVehicle(@Valid @RequestBody VehiclePayload vehiclePayload, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userID = UserUtil.getUserId(authentication);

        Vehicle vehicle = new Vehicle();
        vehicle.setBrand(vehiclePayload.brand());
        vehicle.setModel(vehiclePayload.model());
        vehicle.setRegistrationPlate(vehiclePayload.registrationPlate());
        vehicle.setUserId(userID);

        Optional<Vehicle> createdVehicle = vs.addVehicle(vehicle);

        if (createdVehicle.isPresent()) {
            return ResponseEntity.ok(createdVehicle.get());
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateVehicle(@PathVariable String id, @Valid @RequestBody VehiclePayload vehiclePayload, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result);
        }

        Optional<Vehicle> oVehicle = vs.findVehicleById(id);

        if (oVehicle.isPresent()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userID = UserUtil.getUserId(authentication);

            Vehicle vehicle = oVehicle.get();
            vehicle.setBrand(vehiclePayload.brand());
            vehicle.setModel(vehiclePayload.model());
            vehicle.setRegistrationPlate(vehiclePayload.registrationPlate());
            vehicle.setUserId(userID);

            Optional<Vehicle> updatedVehicle = vs.updateVehicle(vehicle);
            if (updatedVehicle.isPresent()) {
                return ResponseEntity.ok(updatedVehicle.get());
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable String id) {
        Optional<Vehicle> oVehicle = vs.findVehicleById(id);
        if (oVehicle.isPresent()) {
            boolean result = vs.deleteVehicle(id);
            if (result) {
                return ResponseEntity.ok(Map.of("result", true));
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
