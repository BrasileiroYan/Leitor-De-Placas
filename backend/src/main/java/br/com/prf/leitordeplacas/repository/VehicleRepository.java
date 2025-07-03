package br.com.prf.leitordeplacas.repository;

import br.com.prf.leitordeplacas.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByPlate(String plate);

    @Query("SELECT v FROM Vehicle v JOIN FETCH v.owner o LEFT JOIN FETCH o.address a LEFT JOIN FETCH o.criminalRecord cr LEFT JOIN FETCH cr.crimeList WHERE v.plate = :plate")
    Optional<Vehicle> findByPlateWithDetails(@Param("plate") String plate);
}
