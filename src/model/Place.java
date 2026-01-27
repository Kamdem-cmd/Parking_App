package model;

public class Place {
    private int numero;
    private boolean estOccupee;
    private boolean estReservee; // Nouveau: Pour la fonctionnalité de réservation
    private Vehicule vehiculeGare;

    public Place(int numero) {
        this.numero = numero;
        this.estOccupee = false;
        this.estReservee = false;
    }

    public int getNumero() {
        return numero;
    }

    public boolean estOccupee() {
        return estOccupee || estReservee;
    }

    public boolean estReservee() {
        return estReservee;
    }

    public void setReservee(boolean r) {
        this.estReservee = r;
    }

    public Vehicule getVehicule() {
        return vehiculeGare;
    }

    public void garerVehicule(Vehicule v) {
        this.vehiculeGare = v;
        this.estOccupee = true;
    }

    public Vehicule libererPlace() {
        Vehicule v = this.vehiculeGare;
        this.vehiculeGare = null;
        this.estOccupee = false;
        this.estReservee = false;
        return v;
    }
}