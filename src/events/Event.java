package events;

import model.*;
import java.util.Scanner;

public class Event {

    // Logique pour traiter l'entrée d'un véhicule
    public void traiterEntreeVehicule(Parking parking, String immatriculation, String marque, String typeStr) {
        // 1. Vérifier disponibilité
        Place placeDispo = parking.trouverPlaceDisponible();

        if (placeDispo == null) {
            System.out.println("ERREUR: Le parking est complet !");
            return;
        }

        // 2. Convertir le type (String -> Enum)
        TypeVehicule type;
        try {
            type = TypeVehicule.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("ERREUR: Type de véhicule invalide (VOITURE, MOTO, CAMION).");
            return;
        }

        // 3. Créer le véhicule et garer
        Vehicule nouveauVehicule = new Vehicule(immatriculation, marque, type);
        placeDispo.garerVehicule(nouveauVehicule);

        System.out.println("SUCCÈS: Véhicule garé à la place numéro " + placeDispo.getNumero());
    }

    // Logique pour traiter la sortie d'un véhicule par numéro de place
    public void traiterSortieVehicule(Parking parking, int numeroPlace) {
        // Trouver la place
        Place placeConcernee = null;
        for (Place p : parking.getPlaces()) {
            if (p.getNumero() == numeroPlace) {
                placeConcernee = p;
                break;
            }
        }

        if (placeConcernee == null || !placeConcernee.estOccupee()) {
            System.out.println("ERREUR: Place introuvable ou déjà libre.");
            return;
        }

        // Libérer et générer ticket
        Vehicule v = placeConcernee.libererPlace();
        Ticket ticket = new Ticket(v, placeConcernee);

        // Mise à jour CA
        // Note: Ici nous n'avons pas accès direct au montant du ticket car il est privé
        // dans Ticket,
        // Vos coéquipiers devront peut-être ajouter un getter dans Ticket.java.
        // Pour l'instant, on affiche juste le ticket.

        System.out.println(ticket.toString());
        System.out.println("Au revoir !");
    }
}