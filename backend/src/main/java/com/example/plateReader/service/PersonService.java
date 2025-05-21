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

    public PersonResponseDTO findById(Long id) {
        Person person = personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));

        return new PersonResponseDTO(person);
    }
}
