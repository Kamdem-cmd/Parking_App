package ui;

import java.util.Scanner;
import model.Parking;
import model.Place;
import events.Event;

public class UI {
    private Scanner scanner;
    private Event eventManager;

    public UI() {
        this.scanner = new Scanner(System.in);
        this.eventManager = new Event();
    }

    public void demarrerInterface(Parking parking) {
        boolean continuer = true;

        while (continuer) {
            afficherMenu();
            String choix = scanner.nextLine();

            switch (choix) {
                case "1":
                    afficherDisponibilite(parking);
                    break;
                case "2":
                    entrerNouveauVehicule(parking);
                    break;
                case "3":
                    sortirVehicule(parking);
                    break;
                case "4":
                    afficherEtatParking(parking);
                    break;
                case "Q":
                case "q":
                    continuer = false;
                    System.out.println("Fermeture du système...");
                    break;
                default:
                    System.out.println("Option invalide.");
            }
        }
    }

    private void afficherMenu() {
        System.out.println("\n=== GESTION PARKING ===");
        System.out.println("1. Vérifier le nombre de places libres");
        System.out.println("2. Garer un véhicule");
        System.out.println("3. Sortir un véhicule (Libérer une place)");
        System.out.println("4. Voir l'état de toutes les places");
        System.out.println("Q. Quitter");
        System.out.print("Votre choix : ");
    }

    private void afficherDisponibilite(Parking parking) {
        int libres = parking.compterPlacesLibres();
        System.out.println("Il reste " + libres + " places disponibles.");
    }

    private void entrerNouveauVehicule(Parking parking) {
        System.out.print("Entrez l'immatriculation : ");
        String immat = scanner.nextLine();
        System.out.print("Entrez la marque : ");
        String marque = scanner.nextLine();
        System.out.print("Entrez le type (VOITURE, MOTO, CAMION) : ");
        String type = scanner.nextLine();

        // Appel à la couche Event pour la logique
        eventManager.traiterEntreeVehicule(parking, immat, marque, type);
    }

    private void sortirVehicule(Parking parking) {
        System.out.print("Entrez le numéro de la place à libérer : ");
        try {
            int numero = Integer.parseInt(scanner.nextLine());
            eventManager.traiterSortieVehicule(parking, numero);
        } catch (NumberFormatException e) {
            System.out.println("Erreur : Veuillez entrer un numéro valide.");
        }
    }

    private void afficherEtatParking(Parking parking) {
        for (Place p : parking.getPlaces()) {
            String statut = p.estOccupee() ? "[Occupée - " + p.getVehicule().getImmatriculation() + "]" : "[Libre]";
            System.out.println("Place " + p.getNumero() + " : " + statut);
        }
    }
}