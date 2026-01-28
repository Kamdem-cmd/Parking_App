package events;

import java.io.*;
import java.util.*;
import model.*;

public class Event {

    private final String FILE_PATH = "assets/garage_data.txt";

    // --- 1. SAUVEGARDE AMÉLIORÉE ---
    // On sauvegarde tout : Immat, Marque, Type pour pouvoir recréer l'objet
    public void sauvegarderDonnees(List<Parking> parkings) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Parking p : parkings) {
                // Marqueur pour dire qu'on change de garage
                writer.println("GARAGE:" + p.getNom());

                for (Place pl : p.getPlaces()) {
                    if (pl.getVehicule() != null) {
                        Vehicule v = pl.getVehicule();
                        // Format: VOITURE:NumPlace;Immat;Marque;Type
                        writer.println("VOITURE:" + pl.getNumero() + ";" +
                                v.getImmatriculation() + ";" +
                                v.getMarque() + ";" +
                                v.getType());
                    } else if (pl.estReservee()) {
                        // Format: RESERVE:NumPlace
                        writer.println("RESERVE:" + pl.getNumero());
                    }
                }
            }
            System.out.println("Données sauvegardées avec succès.");
        } catch (IOException e) {
            System.err.println("Erreur de sauvegarde : " + e.getMessage());
        }
    }

    // --- 2. CHARGEMENT (NOUVEAU) ---
    // Cette méthode lit le fichier et remplit les garages au démarrage
    public void chargerDonnees(List<Parking> parkings) {
        File file = new File(FILE_PATH);
        if (!file.exists())
            return; // Si pas de fichier, on ne fait rien

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Parking parkingActuel = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("GARAGE:")) {
                    // On trouve le bon parking dans la liste
                    String nomGarage = line.split(":")[1];
                    for (Parking p : parkings) {
                        if (p.getNom().equals(nomGarage)) {
                            parkingActuel = p;
                            break;
                        }
                    }
                } else if (line.startsWith("VOITURE:") && parkingActuel != null) {
                    // On recrée la voiture
                    try {
                        String[] data = line.split(":")[1].split(";");
                        int numPlace = Integer.parseInt(data[0]);
                        String immat = data[1];
                        String marque = data[2];
                        String typeStr = data[3];

                        Vehicule v = new Vehicule(immat, marque, TypeVehicule.valueOf(typeStr));

                        // On la met dans la bonne place (Index = num - 1)
                        parkingActuel.getPlaces().get(numPlace - 1).garerVehicule(v);
                    } catch (Exception e) {
                        System.err.println("Erreur lecture voiture : " + line);
                    }
                } else if (line.startsWith("RESERVE:") && parkingActuel != null) {
                    // On remet la réservation
                    int numPlace = Integer.parseInt(line.split(":")[1]);
                    parkingActuel.getPlaces().get(numPlace - 1).setReservee(true);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur de chargement : " + e.getMessage());
        }
    }

    // --- LES AUTRES MÉTHODES (Inchangées) ---

    public String garerVehiculeSpecifique(Place place, String immat, String marque, String typeStr) {
        if (place.estOccupee())
            return "Erreur : Place occupée !";
        try {
            Vehicule v = new Vehicule(immat, marque, TypeVehicule.valueOf(typeStr.toUpperCase()));
            place.garerVehicule(v);
            return "Véhicule garé sur place " + place.getNumero();
        } catch (Exception e) {
            return "Erreur : " + e.getMessage();
        }
    }

    public String gererSortie(Parking parking, int numeroPlace) {
        for (Place p : parking.getPlaces()) {
            if (p.getNumero() == numeroPlace && p.estOccupee()) {
                Vehicule v = p.libererPlace();
                Ticket t = new Ticket(v, p);
                parking.ajouterChiffreAffaires(t.getMontant());
                t.setNomParking(parking.getNom());
                return t.toString();
            }
        }
        return "Erreur.";
    }

    public String[] rechercherVehicule(List<Parking> parkings, String immat) {
        for (Parking p : parkings) {
            for (Place place : p.getPlaces()) {
                if (place.getVehicule() != null && place.getVehicule().getImmatriculation().equalsIgnoreCase(immat)) {
                    return new String[] { p.getNom(), String.valueOf(place.getNumero()) };
                }
            }
        }
        return null;
    }

    public String remplissageAleatoire(Parking p, int quantite) {
        int placesLibres = p.compterPlacesLibres();

        if (quantite > placesLibres)
            return "Pas assez de places !";

        String[] marques = { "Toyota", "Ford", "BMW", "Audi" };
        TypeVehicule[] types = TypeVehicule.values();
        Random rand = new Random();

        for (int i = 0; i < quantite; i++) {
            String immat = "AUTO-" + (1000 + rand.nextInt(9000));
            Vehicule v = new Vehicule(immat, marques[rand.nextInt(marques.length)], types[rand.nextInt(types.length)]);
            Place placeLibre = p.trouverPlaceDisponible();

            if (placeLibre != null)
                placeLibre.garerVehicule(v);
        }
        
        return "Ajouté " + quantite + " véhicules.";
    }
}