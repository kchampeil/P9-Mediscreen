package com.mediscreen.commons.constants;

public enum RiskLevel {
    NONE("Aucun risque"),
    BORDERLINE("Risque limité"),
    IN_DANGER("Danger"),
    EARLY_ONSET("Apparition précoce"),
    NOT_APPLICABLE("Non applicable");

    private final String description;

    RiskLevel(String s) {
        this.description = s;
    }

    public String getDescription() {
        return description;
    }
}
