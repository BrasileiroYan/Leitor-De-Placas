package com.example.plateReader.model.enums;

public enum VehicleType {
    CARRO("Carro"),
    MOTOCICLETA("Motocicleta"),
    MOTONETA("Motoneta"),
    CICLOMOTOR("Ciclomotor"),
    CAMINHAO("Caminhão"),
    ONIBUS("Ônibus"),
    TRATOR("Trator"),
    TRAILER("Trailer"),
    OUTRO("Outro");

    private final String displayName;

    VehicleType(String displayName) {this.displayName = displayName;}

    public String getDisplayName() {
        return displayName;
    }
}
