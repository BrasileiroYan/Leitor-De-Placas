package com.example.plateReader.dto;

import com.example.plateReader.model.Address;
import com.example.plateReader.model.Person;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
public class PersonResponseDTO {

    private String fullName;
    private String rg;
    private String cpf;
    private String birthDate;
    private String gender;
    private String licenseCategory;
    private AddressResponseDTO address;
    private CriminalRecordResponseDTO criminalRecord;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PersonResponseDTO(Person person) {
        this.fullName = person.getFullName();
        this.rg = person.getRg();
        this.cpf = person.getCpf();
        this.birthDate = person.getBirthDate().format(formatter);
        this.gender = person.getGender();
        this.licenseCategory = person.getLicenseCategory().name();
        this.address = new AddressResponseDTO(person.getAddress());
        this.criminalRecord = new CriminalRecordResponseDTO(person.getCriminalRecord());
    }

    public String getFullName() {
        return fullName;
    }

    public String getGender() {
        return gender;
    }

    public String getRg() {
        return rg;
    }

    public String getCpf() {
        return cpf;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getLicenseCategory() {
        return licenseCategory;
    }

    public AddressResponseDTO getAddress() {
        return address;
    }

    public CriminalRecordResponseDTO getCriminalRecord() {
        return criminalRecord;
    }
}
