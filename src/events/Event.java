package events;

import model.*;

public class Event {

    // Retourne un message pour l'UI après avoir tenté de garer un véhicule
    public String gererEntree(Parking parking, String immat, String marque, String typeStr) {
        Place placeDispo = parking.trouverPlaceDisponible();

        if (placeDispo == null) {
            return "Désolé, le parking est complet !";
        }

        try {
            TypeVehicule type = TypeVehicule.valueOf(typeStr.toUpperCase());
            Vehicule v = new Vehicule(immat, marque, type);
            placeDispo.garerVehicule(v);
            return "Succès ! Véhicule garé à la place n°" + placeDispo.getNumero();
        } catch (Exception e) {
            return "Erreur lors de l'entrée : " + e.getMessage();
        }
    }

    // Retourne le contenu du ticket sous forme de texte pour la sortie
    public String gererSortie(Parking parking, int numeroPlace) {
        for (Place p : parking.getPlaces()) {
            if (p.getNumero() == numeroPlace && p.estOccupee()) {
                Vehicule v = p.libererPlace();
                Ticket t = new Ticket(v, p);
                parking.ajouterChiffreAffaires(t.getMontant());
                return t.toString();
            }
        }
        return "Erreur : Place déjà vide ou introuvable.";
    }
}