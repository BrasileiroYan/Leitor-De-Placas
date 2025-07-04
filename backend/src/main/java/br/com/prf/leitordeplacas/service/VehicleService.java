package br.com.prf.leitordeplacas.service;

import br.com.prf.leitordeplacas.dto.CrimeResponseDTO;
import br.com.prf.leitordeplacas.dto.CriminalRecordResponseDTO;
import br.com.prf.leitordeplacas.dto.PersonResponseDTO;
import br.com.prf.leitordeplacas.dto.VehicleResponseDTO;
import br.com.prf.leitordeplacas.model.Crime;
import br.com.prf.leitordeplacas.model.CriminalRecord;
import br.com.prf.leitordeplacas.model.Person;
import br.com.prf.leitordeplacas.model.Vehicle;
import br.com.prf.leitordeplacas.repository.VehicleRepository;
import br.com.prf.leitordeplacas.service.exception.CrimeNotFoundException;
import br.com.prf.leitordeplacas.service.exception.CriminalRecordNotFoundException;
import br.com.prf.leitordeplacas.service.exception.PersonNotFoundException;
import br.com.prf.leitordeplacas.service.exception.VehicleNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<VehicleResponseDTO> findAll() {
        return vehicleRepository.findAll()
                .stream().map(VehicleResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public VehicleResponseDTO findByPlate(String plate) {
        Vehicle vehicle = vehicleRepository.findByPlateWithDetails(plate)
                .orElseThrow(() -> new VehicleNotFoundException(plate));

        return new VehicleResponseDTO(vehicle);
    }

    public PersonResponseDTO findOwnerByPlate(String plate) {
        Vehicle vehicle = getVehicleByPlateOrThrow(plate);
        Person owner = getOwnerOrThrow(vehicle);

        return new PersonResponseDTO(owner);
    }

    public CriminalRecordResponseDTO findCriminalRecordByPlate(String plate) {
        Vehicle vehicle = getVehicleByPlateOrThrow(plate);
        Person owner = getOwnerOrThrow(vehicle);
        CriminalRecord criminalRecord = getCriminalRecordOrThrow(owner);

        return new CriminalRecordResponseDTO(criminalRecord);
    }

    public List<CrimeResponseDTO> findAllCrimesByPlate(String plate) {
        Vehicle vehicle = getVehicleByPlateOrThrow(plate);
        Person owner = getOwnerOrThrow(vehicle);
        CriminalRecord criminalRecord = getCriminalRecordOrThrow(owner);
        List<Crime> crimeList = getCrimeListOrThrow(criminalRecord);

        return crimeList.stream().map(CrimeResponseDTO::new).collect(Collectors.toList());
    }

    public CrimeResponseDTO findCrimeByPlateAndId(String plate, Long id) {
        Vehicle vehicle = getVehicleByPlateOrThrow(plate);
        Person owner = getOwnerOrThrow(vehicle);
        CriminalRecord criminalRecord = getCriminalRecordOrThrow(owner);
        List<Crime> crimeList = getCrimeListOrThrow(criminalRecord);

        Crime crime = crimeList.stream().filter(c -> c.getId().equals(id)).
                findFirst().orElseThrow(() -> new CrimeNotFoundException(id, plate));

        return new CrimeResponseDTO(crime);
    }

    // Métodos Auxiliares privados

    private Vehicle getVehicleByPlateOrThrow(String plate) {
        return vehicleRepository.findByPlate(plate)
                .orElseThrow(() -> new VehicleNotFoundException(plate));
    }

    private Person getOwnerOrThrow(Vehicle vehicle) {
        Person owner = vehicle.getOwner();

        if (owner == null) {
            throw new PersonNotFoundException("Proprietário não encontrado para o veículo com placa [" + vehicle.getPlate() + "].");
        }
        return owner;
    }

    private CriminalRecord getCriminalRecordOrThrow(Person owner) {
        CriminalRecord criminalRecord = owner.getCriminalRecord();

        if (criminalRecord == null) {
            throw new CriminalRecordNotFoundException("Ficha Criminal não encontrada para o proprietário [" + owner.getFullName() + "].");
        }
        return criminalRecord;
    }

    private List<Crime> getCrimeListOrThrow(CriminalRecord criminalRecord) {
        List<Crime> crimeList = criminalRecord.getCrimeList();

        if (crimeList == null || crimeList.isEmpty()) {
            throw new CrimeNotFoundException("Não há crimes registrados para proprietário com ficha [" + criminalRecord.getId() + "].");
        }
        return crimeList;
    }
}
