package com.example.plateReader.repository;

import com.example.plateReader.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    public Optional<Vehicle> findByPlate(String plate);
}
