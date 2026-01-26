import model.Parking;
import ui.UI;

public class Main {
    public static void main(String argv[]) {
        // 1. Initialisation du Parking (par exemple 10 places)
        Parking monParking = new Parking(10);

        // 2. Initialisation de l'interface utilisateur
        UI interfaceUtilisateur = new UI();

        // 3. Lancement de l'application
        System.out.println("Démarrage du système Java Garage...");
        interfaceUtilisateur.demarrerInterface(monParking);
    }
}