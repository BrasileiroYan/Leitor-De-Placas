package br.com.prf.leitordeplacas.dto;

import br.com.prf.leitordeplacas.model.Vehicle;
import br.com.prf.leitordeplacas.service.exception.InvalidDataException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class VehicleResponseDTO {

    private String plate;
    private String vehicleType;
    private String brand;
    private String model;
    private String color;
    private Integer fabricationYear;
    private String ipvaStatus;
    private PersonResponseDTO owner;

    public VehicleResponseDTO(Vehicle vehicle) {
        if(vehicle.getOwner() == null) {
            throw new InvalidDataException("O veículo com placa [" + vehicle.getPlate() + "] não possui proprietário.");
        }

        this.plate = vehicle.getPlate();
        this.vehicleType = vehicle.getVehicleType().getDisplayName();
        this.brand = vehicle.getBrand();
        this.model = vehicle.getModel();
        this.color = vehicle.getColor();
        this.fabricationYear = vehicle.getFabricationYear();
        this.ipvaStatus = vehicle.getIpvaStatus().getDisplayName();
        this.owner = new PersonResponseDTO(vehicle.getOwner());
    }

    public String getPlate() {
        return plate;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public Integer getFabricationYear() {
        return fabricationYear;
    }

    public String getIpvaStatus() {
        return ipvaStatus;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public PersonResponseDTO getOwner() {
        return owner;
    }
}
