package br.com.prf.leitordeplacas.model.enums;

public enum LicenseCategory {
    A("A"),
    B("B"),
    C("C"),
    D("D"),
    E("E"),
    ACC("ACC"),
    AB("AB"),
    NENHUM("Nenhum");

    private final String displayName;

    LicenseCategory(String displayName) {this.displayName = displayName;}

    public String getDisplayName() {
        return displayName;
    }
}
