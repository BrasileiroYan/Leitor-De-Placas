package com.example.plateReader.service;

import com.example.plateReader.dto.CrimeResponseDTO;
import com.example.plateReader.dto.CriminalRecordResponseDTO;
import com.example.plateReader.dto.PersonResponseDTO;
import com.example.plateReader.model.Crime;
import com.example.plateReader.model.CriminalRecord;
import com.example.plateReader.service.exception.CrimeNotFoundException;
import com.example.plateReader.service.exception.CriminalRecordNotFoundException;
import com.example.plateReader.service.exception.PersonNotFoundException;
import com.example.plateReader.model.Person;
import com.example.plateReader.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<PersonResponseDTO> findAll() {
        return personRepository.findAll()
                .stream().map(PersonResponseDTO::new)
                .collect(Collectors.toList());
    }

    public PersonResponseDTO findByPlate(String plate) {
        Person person = getPersonByPlateOrThrow(plate);

        return new PersonResponseDTO(person);
    }

    public CriminalRecordResponseDTO findCriminalRecordByPlate(String plate) {
        Person person = getPersonByPlateOrThrow(plate);

        CriminalRecord criminalRecord = getCriminalRecordOrThrow(person);

        return new CriminalRecordResponseDTO(criminalRecord);
    }

    public List<CrimeResponseDTO> findAllCrimesByPlate(String plate) {
        Person person = getPersonByPlateOrThrow(plate);
        CriminalRecord criminalRecord = getCriminalRecordOrThrow(person);
        List<Crime> crimeList = getCrimeListOrThrow(criminalRecord);

        return crimeList.stream().map(CrimeResponseDTO::new).collect(Collectors.toList());
    }

    public CrimeResponseDTO findCrimeByPlateAndId(String plate, Long id) {
        Person person = getPersonByPlateOrThrow(plate);
        CriminalRecord criminalRecord = getCriminalRecordOrThrow(person);
        List<Crime> crimeList = getCrimeListOrThrow(criminalRecord);

        Crime crime = crimeList.stream().filter(c -> c.getId().equals(id)).
                findFirst().orElseThrow(() -> new CrimeNotFoundException(id, plate));

        return new CrimeResponseDTO(crime);
    }

    private Person getPersonByPlateOrThrow(String plate) {
        return personRepository.findByPlate(plate).orElseThrow(() -> new PersonNotFoundException(plate));
    }

    private CriminalRecord getCriminalRecordOrThrow(Person person) {
        CriminalRecord criminalRecord = person.getCriminalRecord();

        if (criminalRecord == null) {
            throw new CriminalRecordNotFoundException("Ficha Criminal não encontrada para placa [" + person.getPlate() + "].");
        }

        return criminalRecord;
    }

    private List<Crime> getCrimeListOrThrow(CriminalRecord criminalRecord) {
        List<Crime> crimeList = criminalRecord.getCrimeList();

        if (crimeList == null || crimeList.isEmpty()) {
            throw new CrimeNotFoundException("Não há crimes encontrados para placa [" + criminalRecord.getPerson().getPlate() + "].");
        }

        return crimeList;
    }
}
