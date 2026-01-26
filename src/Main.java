import javax.swing.SwingUtilities;
import model.Parking;
import ui.UI;

public class Main {
    public static void main(String argv[]) {
        // Création du parking avec 16 places par exemple
        Parking monParking = new Parking(16);

        // Lancement de l'interface graphique dans le thread approprié
        SwingUtilities.invokeLater(() -> {
            UI gui = new UI(monParking);
            gui.setVisible(true);
            gui.setLocationRelativeTo(null); // Centrer l'écran
        });
    }
}