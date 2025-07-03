package br.com.prf.leitordeplacas.dto;

import br.com.prf.leitordeplacas.model.Address;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AddressResponseDTO {

    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;

    public AddressResponseDTO(Address address) {
        this.street = address.getStreet();
        this.number = address.getNumber();
        this.complement = address.getComplement();
        this.neighborhood = address.getNeighborhood();
        this.city = address.getCity();
        this.state = address.getState();
        this.zipCode = address.getZipCode();
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getComplement() {
        return complement;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }
}
