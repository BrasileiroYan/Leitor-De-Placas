package com.example.plateReader.config;

import com.example.plateReader.model.Crime;
import com.example.plateReader.model.CriminalRecord;
import com.example.plateReader.model.Person;
import com.example.plateReader.repository.CrimeRepository;
import com.example.plateReader.repository.CriminalRecordRepository;
import com.example.plateReader.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CrimeRepository crimeRepository;

    @Autowired
    private CriminalRecordRepository criminalRecordRepository;

    @Override
    public void run(String... args) throws Exception {

        Person p1 = new Person("ABC1234", "João Silva", "12345678", "123.456.789-00", LocalDate.parse("1990-01-15"));
        Person p2 = new Person("XYZ9876", "Maria Souza", "87654321", "987.654.321-00", LocalDate.parse("1985-06-30"));
        Person p3 = new Person("LMN4567", "Carlos Pereira", "45678912", "456.789.123-00", LocalDate.parse("2000-11-20"));

        personRepository.save(p1);
        personRepository.save(p2);
        personRepository.save(p3);

        Crime c1 = new Crime("Furto", LocalDateTime.parse("2023-01-10T10:30:00"), "Furto de veículo");
        Crime c2 = new Crime("Assalto", LocalDateTime.parse("2023-05-12T15:00:00"), "Assalto à mão armada");
        Crime c3 = new Crime("Fraude", LocalDateTime.parse("2022-08-25T09:00:00"), "Fraude bancária");
        crimeRepository.save(c1);
        crimeRepository.save(c2);
        crimeRepository.save(c3);


        CriminalRecord cr1 = new CriminalRecord(p1);
        cr1.addCrime(c1);

        CriminalRecord cr2 = new CriminalRecord(p2);
        cr2.addCrime(c2);

        CriminalRecord cr3 = new CriminalRecord(p3);
        cr3.addCrime(c3);

        criminalRecordRepository.save(cr1);
        criminalRecordRepository.save(cr2);
        criminalRecordRepository.save(cr3);
    }
}
