package br.com.prf.leitordeplacas.repository;

import br.com.prf.leitordeplacas.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Override
    Optional<Address> findById(Long id);
}
