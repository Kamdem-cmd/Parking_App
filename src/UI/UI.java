package ui;

import javax.swing.*;
import java.awt.*;
import model.*;
import events.Event;

public class UI extends JFrame {
    private Parking parking;
    private Event eventManager;
    private JPanel panelGrille;
    private JLabel labelStatus;

    public UI(Parking parking) {
        this.parking = parking;
        this.eventManager = new Event();

        // Configuration de la fenêtre
        setTitle("Système de Gestion de Garage");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panneau supérieur : Statistiques
        JPanel panelInfo = new JPanel(new GridLayout(1, 2));
        labelStatus = new JLabel("", SwingConstants.CENTER);
        mettreAJourStats();
        panelInfo.add(labelStatus);
        add(panelInfo, BorderLayout.NORTH);

        // Panneau central : La Grille de places
        panelGrille = new JPanel(new GridLayout(0, 4, 10, 10)); // 4 colonnes
        dessinerParking();
        add(new JScrollPane(panelGrille), BorderLayout.CENTER);

        // Bouton de rafraîchissement en bas
        JButton btnRefresh = new JButton("Actualiser la vue");
        btnRefresh.addActionListener(e -> dessinerParking());
        add(btnRefresh, BorderLayout.SOUTH);
    }

    private void mettreAJourStats() {
        labelStatus.setText("Places Libres : " + parking.compterPlacesLibres() +
                " | CA Total : " + String.format("%.2f", parking.getChiffreAffaires()) + "€");
    }

    private void dessinerParking() {
        panelGrille.removeAll();
        for (Place p : parking.getPlaces()) {
            JButton btn = new JButton("Place " + p.getNumero());

            if (p.estOccupee()) {
                btn.setBackground(new Color(255, 100, 100)); // Rouge
                btn.setToolTipText(p.getVehicule().toString());
                btn.addActionListener(e -> actionSortie(p.getNumero()));
            } else {
                btn.setBackground(new Color(100, 255, 100)); // Vert
                btn.addActionListener(e -> actionEntree());
            }
            panelGrille.add(btn);
        }
        mettreAJourStats();
        panelGrille.revalidate();
        panelGrille.repaint();
    }

    private void actionEntree() {
        // Formulaire simple via Dialogues
        String immat = JOptionPane.showInputDialog(this, "Immatriculation du véhicule :");
        if (immat == null || immat.isEmpty())
            return;

        String marque = JOptionPane.showInputDialog(this, "Marque du véhicule :");

        String[] types = { "VOITURE", "MOTO", "CAMION" };
        String typeChoisi = (String) JOptionPane.showInputDialog(this, "Type :", "Selection",
                JOptionPane.QUESTION_MESSAGE, null, types, types[0]);

        String msg = eventManager.gererEntree(parking, immat, marque, typeChoisi);
        JOptionPane.showMessageDialog(this, msg);
        dessinerParking();
    }

    private void actionSortie(int numPlace) {
        int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous libérer la place n°" + numPlace + " ?");
        if (confirm == JOptionPane.YES_OPTION) {
            String ticketInfo = eventManager.gererSortie(parking, numPlace);
            JOptionPane.showMessageDialog(this, ticketInfo, "Ticket de Sortie", JOptionPane.INFORMATION_MESSAGE);
            dessinerParking();
        }
    }
}