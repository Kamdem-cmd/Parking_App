import java.util.ArrayList;
import java.util.List;

public class Parking {
        private int capacitéMax;
    private List<Place> Vehicules;
    public Parking(int capacitéMax) {
        this.capacitéMax = capacitéMax;
        this.Vehicules = new ArrayList<>();
    }
}
