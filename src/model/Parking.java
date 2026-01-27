package model;

import java.util.*;

public class Parking {
    private String nom; // Nouveau: Pour identifier Garage 1, 2, etc.
    private int taille;
    private List<Place> places;
    private double chiffreAffaires;

    public Parking(String nom, int taille) {
        this.nom = nom;
        this.taille = taille;
        this.places = new ArrayList<>();
        this.chiffreAffaires = 0.0;
        for (int i = 1; i <= taille; i++) {
            places.add(new Place(i));
        }
    }

    public void setChiffreAffaires(double montant) {
        this.chiffreAffaires = montant;
    }

    public String getNom() {
        return nom;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public int compterPlacesLibres() {
        int count = 0;
        for (Place p : places) {
            if (!p.estOccupee())
                count++;
        }
        return count;
    }

    public Place trouverPlaceDisponible() {
        for (Place p : places) {
            if (!p.estOccupee())
                return p;
        }
        return null;
    }

    public void ajouterChiffreAffaires(double montant) {
        this.chiffreAffaires += montant;
    }

    public double getChiffreAffaires() {
        return chiffreAffaires;
    }

    public void resetChiffreAffaires() {
        this.chiffreAffaires = 0;
    }
}