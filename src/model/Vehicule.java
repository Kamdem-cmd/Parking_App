package model;

import java.time.LocalDateTime;

public class Vehicule {
    private String immatriculation;
    private String marque;
    private TypeVehicule type;
    private LocalDateTime heureEntree;

    // Constructeur
    public Vehicule(String immatriculation, String marque, TypeVehicule type) {
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.type = type;
        this.heureEntree = LocalDateTime.now(); // L'heure est définie à la création
    }

    // Getters
    public String getImmatriculation() {
        return immatriculation;
    }

    public String getMarque() {
        return marque;
    }

    public TypeVehicule getType() {
        return type;
    }

    public LocalDateTime getHeureEntree() {
        return heureEntree;
    }

    @Override
    public String toString() {
        return "Véhicule [Immat=" + immatriculation + ", Marque=" + marque + ", Type=" + type + "]";
    }
}
