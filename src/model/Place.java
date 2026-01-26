package model;

public class Place {
    private int numero;
    private boolean estOccupee;
    private Vehicule vehiculeGaré; // Référence au véhicule garé ici

    public Place(int numero) {
        this.numero = numero;
        this.estOccupee = false;
        this.vehiculeGaré = null;
    }

    public boolean estOccupee() {
        return estOccupee;
    }

    public int getNumero() {
        return numero;
    }

    public Vehicule getVehicule() {
        return vehiculeGaré;
    }

    // Méthode pour garer un véhicule
    public void garerVehicule(Vehicule v) {
        this.vehiculeGaré = v;
        this.estOccupee = true;
    }

    // Méthode pour libérer la place
    public Vehicule libererPlace() {
        Vehicule v = this.vehiculeGaré;
        this.vehiculeGaré = null;
        this.estOccupee = false;
        return v;
    }
}