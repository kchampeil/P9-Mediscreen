package com.mediscreen.commons.constants;

public enum RiskLevel {
    NONE("Aucun risque"),
    BORDERLINE("Risque limité"),
    IN_DANGER("Danger"),
    EARLY_ONSET("Apparition précoce"),
    NOT_APPLICABLE("Non applicable \n (plus de 30 ans et un seul facteur" +
                   " \n OU homme de moins de 30 ans et 1 ou 2 facteurs" +
                   " \n OU femme de moins de 30 ans et 1 à 3 facteurs");

    private final String description;

    RiskLevel(String s) {
        this.description = s;
    }

    public String getDescription() {
        return description;
    }
}
