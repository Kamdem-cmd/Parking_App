public class Vehicule {
    private String matricule;
    private typeVehicule type;
    private Ticket ticket;
    
    public Vehicule(String matricule, typeVehicule type) {
        this.matricule = matricule;
        this.type = type;
    }
    public String getMatricule() {
        return matricule;
    }
    public typeVehicule getType() {
        return type;
    }
    @Override
    public String toString() {
        return "Vehicule{" +
                "matricule='" + matricule + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
