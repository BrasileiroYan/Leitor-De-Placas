package br.com.prf.leitordeplacas.service;

import br.com.prf.leitordeplacas.dto.PersonResponseDTO;
import br.com.prf.leitordeplacas.service.exception.PersonNotFoundException;
import br.com.prf.leitordeplacas.model.Person;
import br.com.prf.leitordeplacas.repository.PersonRepository;
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
