package br.com.prf.leitordeplacas.model;

import br.com.prf.leitordeplacas.model.enums.IpvaStatus;
import br.com.prf.leitordeplacas.model.enums.VehicleType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.io.Serializable;

@Entity
@EqualsAndHashCode(of = "id")
public class Vehicle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Setter
    private String plate;

    @Setter private String brand;
    @Setter private String model;
    @Setter private String color;
    @Setter private Integer fabricationYear;

    @Enumerated(EnumType.STRING)
    @Setter private IpvaStatus ipvaStatus;

    @Enumerated(EnumType.STRING)
    @Setter private VehicleType vehicleType;

    @ManyToOne
    @JoinColumn(name = "person_id")
    @Setter private Person owner;

    public Vehicle() {
    }

    public Vehicle(String plate, String brand, String model, String color, Integer fabricationYear, IpvaStatus ipvaStatus, VehicleType vehicleType, Person owner) {
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.fabricationYear = fabricationYear;
        this.ipvaStatus = ipvaStatus;
        this.vehicleType = vehicleType;
        this.owner = owner;
    }

    public Long getId() {
        return id;
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

    public IpvaStatus getIpvaStatus() {
        return ipvaStatus;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public Person getOwner() {
        return owner;
    }
}
