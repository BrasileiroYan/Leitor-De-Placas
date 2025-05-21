package com.example.plateReader.config;

import com.example.plateReader.model.Crime;
import com.example.plateReader.model.CriminalRecord;
import com.example.plateReader.model.Person;
import com.example.plateReader.model.enums.CrimeStatus;
import com.example.plateReader.repository.CrimeRepository;
import com.example.plateReader.repository.CriminalRecordRepository;
import com.example.plateReader.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void run(String... args) throws Exception {

        Person p1 = new Person("ABC1234", "João Silva", "Masculino", "12345678", "123.456.789-00", LocalDate.parse("1990-01-15"));
        Person p2 = new Person("XYZ9876", "Maria Souza", "Feminino", "87654321", "987.654.321-00", LocalDate.parse("1985-06-30"));
        Person p3 = new Person("LMN4567", "Carlos Pereira", "Masculino", "45678912", "456.789.123-00", LocalDate.parse("2000-11-20"));
        Person p4 = new Person("PUM5050", "Regina Silva", "Feminino", "45018650", "376.019.188-00", LocalDate.parse("2002-05-14"));

        personRepository.save(p1);
        personRepository.save(p2);
        personRepository.save(p3);
        personRepository.save(p4);

        Crime c1 = new Crime("Furto", LocalDateTime.parse("2023-01-10T10:30:00"), "Furto de veículo", CrimeStatus.CONDENADO);
        Crime c2 = new Crime("Assalto", LocalDateTime.parse("2023-05-12T15:00:00"), "Assalto à mão armada", CrimeStatus.ABSOLVIDO);
        Crime c3 = new Crime("Fraude", LocalDateTime.parse("2022-08-25T09:00:00"), "Fraude bancária", CrimeStatus.ARQUIVADO);
        Crime c4 = new Crime("Fraude", LocalDateTime.parse("2019-10-21T07:30:00"), "Fraude bancária", CrimeStatus.SOB_INVESTIGACAO);
        Crime c5 = new Crime("Violência", LocalDateTime.parse("2017-03-11T10:30:00"), "Violência Doméstica", CrimeStatus.SUSPENSO);


        crimeRepository.save(c1);
        crimeRepository.save(c2);
        crimeRepository.save(c3);
        crimeRepository.save(c4);
        crimeRepository.save(c5);


        CriminalRecord cr1 = new CriminalRecord(p1);
        cr1.addCrime(c1);
        cr1.addCrime(c4);

        CriminalRecord cr2 = new CriminalRecord(p2);
        cr2.addCrime(c2);

        CriminalRecord cr3 = new CriminalRecord(p3);
        cr3.addCrime(c3);
        cr3.addCrime(c5);

        CriminalRecord cr4 = new CriminalRecord(p4);


        criminalRecordRepository.save(cr1);
        criminalRecordRepository.save(cr2);
        criminalRecordRepository.save(cr3);
        criminalRecordRepository.save(cr4);

    }
}
