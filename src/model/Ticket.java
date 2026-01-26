package model;

import java.time.LocalDateTime;
import java.time.Duration;

public class Ticket {
    private Vehicule vehicule;
    private Place place;
    private LocalDateTime heureSortie;
    private double montant;

    public Ticket(Vehicule vehicule, Place place) {
        this.vehicule = vehicule;
        this.place = place;
        this.heureSortie = LocalDateTime.now();
        this.montant = calculerMontant();
    }

    // Logique simple de calcul (peut être modifiée par les coéquipiers)
    private double calculerMontant() {
        Duration duree = Duration.between(vehicule.getHeureEntree(), heureSortie);
        long minutes = duree.toMinutes();
        // Exemple : 0.50€ par minute (juste pour tester)
        return minutes * 0.50;
    }

    @Override
    public String toString() {
        return "--- TICKET ---\n" +
                "Véhicule: " + vehicule.getImmatriculation() + "\n" +
                "Place: " + place.getNumero() + "\n" +
                "Montant à payer: " + montant + "€\n" +
                "----------------";
    }
}
