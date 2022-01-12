package com.mediscreen.riskAssessment.constants;

public enum DiabetesRiskFactors {
    HEMOGLOBINE_A1C("Hémoglobine A1C"),
    MICROALBUMINE("Microalbumine"),
    TAILLE("Taille"),
    POIDS("Poids"),
    FUMEUR("Fumeur"),
    ANORMAL("Anormal"),
    CHOLESTEROL("Cholestérol"),
    VERTIGE("Vertige"),
    RECHUTE("Rechute"),
    REACTION("Réaction"),
    ANTICORPS("Anticorps");

    private final String description;

    DiabetesRiskFactors(String s) {
        this.description = s;
    }

    public String getDescription() {
        return description;
    }
}
