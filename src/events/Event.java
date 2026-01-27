package events;

import model.*;
import java.util.*;
import java.io.*;

public class Event {

    private final String FILE_PATH = "assets/garage_data.txt";

    // --- 1. SAUVEGARDE AMÉLIORÉE ---
    // On sauvegarde tout : Immat, Marque, Type pour pouvoir recréer l'objet
    public void sauvegarderDonnees(List<Parking> parkings) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            // 1. On calcule et on sauvegarde le Revenu Total en haut du fichier
            double totalGlobal = 0;
            for (Parking p : parkings)
                totalGlobal += p.getChiffreAffaires();
            writer.println("REVENU_GLOBAL:" + totalGlobal);

            // 2. On sauvegarde les véhicules pour chaque garage
            for (Parking p : parkings) {
                writer.println("GARAGE:" + p.getNom());
                for (Place pl : p.getPlaces()) {
                    if (pl.getVehicule() != null) {
                        Vehicule v = pl.getVehicule();
                        String res = pl.estReservee() ? "YES" : "NO";
                        // Format: VOITURE;Num;Immat;Marque;Type;EstReservee
                        writer.println("VOITURE;" + pl.getNumero() + ";" + v.getImmatriculation() + ";" +
                                v.getMarque() + ";" + v.getType() + ";" + res);
                    } else if (pl.estReservee()) {
                        writer.println("RESERVE;" + pl.getNumero());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur de sauvegarde: " + e.getMessage());
        }
    }

    // --- CHARGEMENT : Restaure l'argent et les véhicules ---
    public void chargerDonnees(List<Parking> parkings) {
        File file = new File(FILE_PATH);
        if (!file.exists())
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Parking parkingActuel = null;

            while ((line = reader.readLine()) != null) {
                // A. Charger le Revenu Global
                if (line.startsWith("REVENU_GLOBAL:")) {
                    double money = Double.parseDouble(line.split(":")[1]);
                    // On peut distribuer l'argent au premier parking ou le diviser
                    // Ici on le met sur le premier pour garder le compte global exact
                    if (!parkings.isEmpty())
                        parkings.get(0).setChiffreAffaires(money);
                }
                // B. Identifier le Garage
                else if (line.startsWith("GARAGE:")) {
                    String nom = line.split(":")[1];
                    for (Parking p : parkings) {
                        if (p.getNom().equals(nom))
                            parkingActuel = p;
                    }
                }
                // C. Recréer les voitures
                else if (line.startsWith("VOITURE;") && parkingActuel != null) {
                    String[] d = line.split(";");
                    Vehicule v = new Vehicule(d[2], d[3], TypeVehicule.valueOf(d[4]));
                    Place pl = parkingActuel.getPlaces().get(Integer.parseInt(d[1]) - 1);
                    pl.garerVehicule(v);
                    if (d[5].equals("YES"))
                        pl.setReservee(true);
                }
                // D. Recréer les réservations vides
                else if (line.startsWith("RESERVE;") && parkingActuel != null) {
                    int num = Integer.parseInt(line.split(";")[1]);
                    parkingActuel.getPlaces().get(num - 1).setReservee(true);
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur de chargement: " + e.getMessage());
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