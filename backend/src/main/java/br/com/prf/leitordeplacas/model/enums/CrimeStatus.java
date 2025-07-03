package br.com.prf.leitordeplacas.model.enums;

public enum CrimeStatus {
    SOB_INVESTIGACAO("Sob investigação"),
    DENUNCIADO("Denunciado"),
    EM_JULGAMENTO("Em julgamento"),
    CONDENADO("Condenado"),
    CUMPRINDO_PENA("Cumprindo pena"),
    PENA_CUMPRIDA("Pena cumprida"),
    ABSOLVIDO("Absolvido"),
    ARQUIVADO("Arquivado"),
    SUSPENSO("Suspenso"),
    DESCONHECIDO("Desconhecido");

    private final String displayName;

    CrimeStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
