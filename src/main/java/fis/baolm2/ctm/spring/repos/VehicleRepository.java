package fis.baolm2.ctm.spring.repos;

import fis.baolm2.ctm.spring.models.Vehicle;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    List<Vehicle> findAllByUserId(@NotBlank(message = "User ID is required") String userId);
}