package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Ticket {
    private Vehicule vehicule;
    private Place place;
    private LocalDateTime heureSortie;
    private double montant;

    public Ticket(Vehicule vehicule, Place place) {
        this.vehicule = vehicule;
        this.place = place;
        this.heureSortie = LocalDateTime.now();
        this.montant = calculerPrix();
    }

    private double calculerPrix() {
        // Calcul de la durée en minutes
        long minutes = Duration.between(vehicule.getHeureEntree(), heureSortie).toMinutes();
        if (minutes == 0)
            minutes = 1; // Minimum 1 minute de facturation

        // Tarif simple : 0.05€ par minute
        return minutes * 0.05;
    }

    public double getMontant() {
        return montant;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return "----------------------------------\n" +
                "       TICKET DE SORTIE           \n" +
                "----------------------------------\n" +
                " Parking   : " + place.getNumero() + "\n" +
                " Immat     : " + vehicule.getImmatriculation() + "\n" +
                " Modèle    : " + vehicule.getMarque() + "\n" + // <-- NOUVEAU
                " Type      : " + vehicule.getType() + "\n" +
                " Entrée    : " + vehicule.getHeureEntree().format(formatter) + "\n" +
                " Sortie    : " + heureSortie.format(formatter) + "\n" +
                "----------------------------------\n" +
                " TOTAL À PAYER : " + String.format("%.2f", montant) + " €\n" +
                "----------------------------------\n" +
                "      Merci de votre visite !     ";
    }
}