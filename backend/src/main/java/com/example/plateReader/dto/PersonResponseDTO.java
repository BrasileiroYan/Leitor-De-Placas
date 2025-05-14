package com.example.plateReader.dto;

import com.example.plateReader.model.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
public class PersonResponseDTO {

    private Long id;
    private String plate;
    private String fullName;
    private String genero;
    private String rg;
    private String cpf;
    private String birthDate;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PersonResponseDTO(Person person) {
        this.id = person.getId();
        this.plate = person.getPlate();
        this.fullName = person.getFullName();
        this.genero = person.getGenero();
        this.rg = person.getRg();
        this.cpf = person.getCpf();
        this.birthDate = person.getBirthDate().format(formatter);
    }

    public Long getId() {
        return id;
    }

    public String getPlate() {
        return plate;
    }

    public String getFullName() {
        return fullName;
    }

    public String getGenero() {
        return genero;
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
}
