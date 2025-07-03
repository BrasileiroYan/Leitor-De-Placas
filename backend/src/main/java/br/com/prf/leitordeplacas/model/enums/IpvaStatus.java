package br.com.prf.leitordeplacas.model.enums;

public enum IpvaStatus {
    PAGO("Pago"),
    ATRASADO("Atrasado"),
    ISENTO("Isento"),
    NAO_PAGO("Não pago");

    private final String displayName;

    IpvaStatus(String displayName) {this.displayName = displayName;}

    public String getDisplayName() {
        return displayName;
    }
}
