import model.Parking;
import ui.UI;
import events.Event; // Import important !
import java.util.*;

public class Main {
    public static void main(String argv[]) {
        // 1. Initialisation des garages vides
        List<Parking> parkings = new ArrayList<>();
        parkings.add(new Parking("Garage 1", 16));
        parkings.add(new Parking("Garage 2", 16));
        parkings.add(new Parking("Garage 3", 16));

        // 2. CHARGEMENT DES DONNÉES (Le pont magique)
        Event chargeur = new Event();
        chargeur.chargerDonnees(parkings);

        // 3. Lancement de l'interface avec les données chargées
        javax.swing.SwingUtilities.invokeLater(() -> {
            UI gui = new UI(parkings);
            gui.setVisible(true);
            gui.setLocationRelativeTo(null);
        });
    }
}