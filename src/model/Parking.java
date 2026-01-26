package model;

import java.util.ArrayList;
import java.util.List;

public class Parking {
    private int taille;
    private List<Place> places; // Liste des places (comme demandé)
    private double chiffreAffaires;

    public Parking(int taille) {
        this.taille = taille;
        this.places = new ArrayList<>();
        this.chiffreAffaires = 0.0;

        // Initialisation des places vides
        for (int i = 1; i <= taille; i++) {
            places.add(new Place(i));
        }
    }

    // Trouver une place libre (renvoie null si aucune place n'est libre)
    public Place trouverPlaceDisponible() {
        for (Place p : places) {
            if (!p.estOccupee()) {
                return p;
            }
        }
        return null;
    }

    // Compter les places libres
    public int compterPlacesLibres() {
        int count = 0;
        for (Place p : places) {
            if (!p.estOccupee())
                count++;
        }
        return count;
    }

    // Obtenir toutes les places (pour l'affichage)
    public List<Place> getPlaces() {
        return places;
    }

    // Ajouter au chiffre d'affaires
    public void ajouterChiffreAffaires(double montant) {
        this.chiffreAffaires += montant;
    }

    public double getChiffreAffaires() {
        return chiffreAffaires;
    }
}