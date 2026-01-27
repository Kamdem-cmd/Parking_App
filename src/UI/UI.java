package ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import model.*;
import events.Event;

public class UI extends JFrame {
    private List<Parking> mesParkings;
    private Parking parkingActuel;
    private Event eventManager;

    // UI Components
    private JPanel panelAffichage;
    private JLabel labelTitre;
    private JLabel labelRevenu; // NOUVEAU : Pour afficher l'argent
    private String immatAchercher = "";

    // NOUVEAU : Réglage de la taille des images
    private final int LARGEUR_IMG = 140; // Augmenté (avant 70)
    private final int HAUTEUR_IMG = 90; // Augmenté (avant 50)

    public UI(List<Parking> parkings) {
        this.mesParkings = parkings;
        this.parkingActuel = parkings.get(0);
        this.eventManager = new Event();

        setTitle("Système de Gestion de Garage Pro");
        setSize(1250, 850); // Fenêtre un peu plus grande pour accueillir les grandes images
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- DASHBOARD (Gauche) ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(240, 850)); // Sidebar un peu plus large
        sidebar.setBackground(new Color(52, 73, 94));
        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 1. Titre du Menu
        JLabel menuTitle = new JLabel("MENU PRINCIPAL");
        menuTitle.setForeground(Color.WHITE);
        menuTitle.setFont(new Font("Arial", Font.BOLD, 20));
        menuTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(menuTitle);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        // 2. Sélecteur de Parking
        String[] choix = { "Garage 1", "Garage 2", "Garage 3" };
        JComboBox<String> selector = new JComboBox<>(choix);
        selector.setMaximumSize(new Dimension(220, 35));
        selector.addActionListener(e -> {
            parkingActuel = mesParkings.get(selector.getSelectedIndex());
            immatAchercher = "";
            rafraichirVue();
        });
        sidebar.add(creerLabelMenu("Choisir Parking :"));
        sidebar.add(selector);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        // 3. Affichage du Revenu (NOUVEAU)
        labelRevenu = new JLabel("CA Global: 0.00€");
        labelRevenu.setForeground(new Color(46, 204, 113)); // Vert "Argent"
        labelRevenu.setFont(new Font("Arial", Font.BOLD, 18));
        labelRevenu.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel panelRevenu = new JPanel(); // Petit conteneur pour le style
        panelRevenu.setBackground(new Color(44, 62, 80));
        panelRevenu.setMaximumSize(new Dimension(220, 50));
        panelRevenu.add(labelRevenu);
        sidebar.add(creerLabelMenu("💰 Revenus Totaux :"));
        sidebar.add(panelRevenu);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        // 4. Boutons d'action
        sidebar.add(creerBoutonMenu("Place disponible", e -> infoGlobales()));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(creerBoutonMenu("Ajoute auto (Massif)", e -> popupAjoutMassif()));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(creerBoutonMenu("Réserver place", e -> popupReservation()));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(creerBoutonMenu("Rechercher", e -> popupRecherche()));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(creerBoutonMenu("Libérer tout", e -> viderGarage()));

        sidebar.add(Box.createVerticalGlue()); // Pousse le reste vers le bas

        sidebar.add(creerBoutonMenu("💾 Sauvegarder", e -> {
            eventManager.sauvegarderDonnees(mesParkings);
            JOptionPane.showMessageDialog(this, "Sauvegardé !");
        }));

        add(sidebar, BorderLayout.WEST);

        // --- GRILLE (Centre) ---
        JPanel mainContent = new JPanel(new BorderLayout());
        labelTitre = new JLabel("Affichage : " + parkingActuel.getNom(), SwingConstants.CENTER);
        labelTitre.setFont(new Font("Arial", Font.BOLD, 24));
        labelTitre.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainContent.add(labelTitre, BorderLayout.NORTH);

        // GridGap réduit (5,5) pour laisser plus de place aux boutons/images
        panelAffichage = new JPanel(new GridLayout(4, 4, 5, 5));
        panelAffichage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainContent.add(new JScrollPane(panelAffichage), BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);
        rafraichirVue();
    }

    // Helper pour créer les titres blancs dans la sidebar
    private JLabel creerLabelMenu(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.LIGHT_GRAY);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }

    private JButton creerBoutonMenu(String nom, java.awt.event.ActionListener action) {
        JButton b = new JButton(nom);
        b.setMaximumSize(new Dimension(220, 45)); // Boutons un peu plus larges
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setFocusPainted(false); // Enlève le contour de focus moche
        b.setFont(new Font("Arial", Font.PLAIN, 14));
        b.addActionListener(action);
        return b;
    }

    public void rafraichirVue() {
        panelAffichage.removeAll();
        labelTitre.setText("Affichage : " + parkingActuel.getNom());

        // Calcul du chiffre d'affaires TOTAL (Garage 1 + 2 + 3)
        double totalCA = 0;
        for (Parking p : mesParkings) {
            totalCA += p.getChiffreAffaires();
        }
        labelRevenu.setText(String.format("%.2f €", totalCA));

        for (Place p : parkingActuel.getPlaces()) {
            JButton spot = new JButton();
            spot.setVerticalTextPosition(SwingConstants.BOTTOM);
            spot.setHorizontalTextPosition(SwingConstants.CENTER);
            spot.setMargin(new Insets(2, 2, 2, 2)); // Réduit les marges internes du bouton

            // GESTION DES IMAGES
            if (p.estReservee()) {
                spot.setIcon(chargerIcone("assets/reserve.jpg"));
                spot.setBackground(new Color(255, 200, 0));
                spot.setText("RÉSERVÉ");
            } else if (p.getVehicule() != null) {
                Vehicule v = p.getVehicule();
                String typeImg = "assets/" + v.getType().toString().toLowerCase() + ".jpg";
                spot.setIcon(chargerIcone(typeImg));

                // Texte plus petit pour laisser la place à l'image
                spot.setText("<html><center><font size='3'>" + v.getImmatriculation() + "</font></center></html>");

                if (v.getImmatriculation().equalsIgnoreCase(immatAchercher)) {
                    spot.setBorder(BorderFactory.createLineBorder(Color.CYAN, 5));
                }
            } else {
                spot.setIcon(chargerIcone("assets/libre.png"));
                spot.setText("Libre " + p.getNumero());
            }

            // Click Listener (Ciblé ou Sortie)
            spot.addActionListener(e -> actionClicPlace(p));

            panelAffichage.add(spot);
        }
        panelAffichage.revalidate();
        panelAffichage.repaint();
    }

    private void actionClicPlace(Place p) {
        if (p.estOccupee()) {
            // Sortie
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Place " + p.getNumero() + " occupée.\nSortir le véhicule et encaisser ?",
                    "Sortie Véhicule", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                String ticket = eventManager.gererSortie(parkingActuel, p.getNumero());
                JOptionPane.showMessageDialog(this, ticket);
                rafraichirVue(); // Cela mettra à jour le CA automatiquement !
            }
        } else {
            // Entrée ciblée
            String immat = JOptionPane.showInputDialog(this, "Entrée Place " + p.getNumero() + "\nImmatriculation :");
            if (immat == null || immat.trim().isEmpty())
                return;

            String marque = JOptionPane.showInputDialog(this, "Marque :");
            String[] types = { "VOITURE", "MOTO", "CAMION" };
            String type = (String) JOptionPane.showInputDialog(this, "Type :", "Choix",
                    JOptionPane.QUESTION_MESSAGE, null, types, types[0]);

            if (type != null) {
                String msg = eventManager.garerVehiculeSpecifique(p, immat, marque, type);
                JOptionPane.showMessageDialog(this, msg);
                rafraichirVue();
            }
        }
    }

    // --- MODIFICATION TAILLE IMAGE ---
    private ImageIcon chargerIcone(String path) {
        try {
            ImageIcon icon = new ImageIcon(path);
            // ICI : On utilise les nouvelles constantes définies en haut
            Image img = icon.getImage().getScaledInstance(LARGEUR_IMG, HAUTEUR_IMG, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            return null;
        }
    }

    // --- DASHBOARD FUNCTIONS ---
    private void infoGlobales() {
        StringBuilder sb = new StringBuilder("Rapport d'occupation :\n");
        for (Parking p : mesParkings) {
            sb.append(p.getNom()).append(" : ").append(p.compterPlacesLibres()).append(" places libres\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString());
    }

    private void popupAjoutMassif() {
        String nStr = JOptionPane.showInputDialog(this, "Nombre de véhicules aléatoires :");
        if (nStr != null) {
            String result = eventManager.remplissageAleatoire(parkingActuel, Integer.parseInt(nStr));
            JOptionPane.showMessageDialog(this, result);
            rafraichirVue();
        }
    }

    private void popupReservation() {
        String nStr = JOptionPane.showInputDialog(this, "Numéro de place à réserver :");
        if (nStr != null) {
            try {
                int n = Integer.parseInt(nStr);
                if (n > 0 && n <= 16) {
                    Place p = parkingActuel.getPlaces().get(n - 1);
                    if (!p.estOccupee()) {
                        p.setReservee(true);
                        rafraichirVue();
                    } else
                        JOptionPane.showMessageDialog(this, "Place déjà prise.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Nombre invalide");
            }
        }
    }

    private void popupRecherche() {
        String immat = JOptionPane.showInputDialog(this, "Immatriculation à chercher :");
        if (immat != null) {
            String[] loc = eventManager.rechercherVehicule(mesParkings, immat);
            if (loc != null) {
                JOptionPane.showMessageDialog(this, "Trouvé : " + loc[0] + " | Place " + loc[1]);
                immatAchercher = immat;
                for (Parking p : mesParkings) {
                    if (p.getNom().equals(loc[0]))
                        parkingActuel = p;
                }
                rafraichirVue();
            } else {
                JOptionPane.showMessageDialog(this, "Non trouvé.");
            }
        }
    }

    private void viderGarage() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous libérer tous les véhicules et encaisser les revenus ?",
                "Vider tout", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            double totalEncaisseCetteFois = 0;

            for (Parking p : mesParkings) {
                for (Place pl : p.getPlaces()) {
                    if (pl.getVehicule() != null) {
                        // Créer un ticket virtuel pour calculer le montant
                        Ticket t = new Ticket(pl.getVehicule(), pl);
                        double montant = t.getMontant();

                        // Ajouter l'argent au parking
                        p.ajouterChiffreAffaires(montant);
                        totalEncaisseCetteFois += montant;

                        // Libérer la place
                        pl.libererPlace();
                    } else if (pl.estReservee()) {
                        // Si c'est une réservation, on libère juste la place
                        pl.libererPlace();
                    }
                }
            }

            rafraichirVue(); // Met à jour la grille et le compteur "CA Global"
            JOptionPane.showMessageDialog(this,
                    "Tous les parkings ont été vidés !\nMontant encaissé lors de cette opération : "
                            + String.format("%.2f", totalEncaisseCetteFois) + " €");
        }
    }
}