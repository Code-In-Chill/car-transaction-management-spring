package fis.baolm2.ctm.spring.controllers;

import fis.baolm2.ctm.spring.dtos.payload.VehiclePayload;
import fis.baolm2.ctm.spring.models.Vehicle;
import fis.baolm2.ctm.spring.services.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
        return ResponseEntity.ok(vs.getAllVehiclesByUserId(getUserId()));
    }

    @PutMapping("/create")
    public ResponseEntity<?> createVehicle(@Valid @RequestBody VehiclePayload vehiclePayload, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result);
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setBrand(vehiclePayload.brand());
        vehicle.setModel(vehiclePayload.model());
        vehicle.setRegistrationPlate(vehiclePayload.registrationPlate());
        vehicle.setUserId(getUserId());

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
            Vehicle vehicle = oVehicle.get();
            vehicle.setBrand(vehiclePayload.brand());
            vehicle.setModel(vehiclePayload.model());
            vehicle.setRegistrationPlate(vehiclePayload.registrationPlate());
            vehicle.setUserId(getUserId());

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

    private String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationToken oauthToken = (JwtAuthenticationToken) authentication;
        return oauthToken.getToken().getSubject();
    }
}
