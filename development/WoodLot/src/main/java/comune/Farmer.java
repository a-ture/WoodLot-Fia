package comune;

import java.util.Objects;
import java.util.Random;

public class Farmer implements Cloneable {
    private int id;
    private String country;
    private int treesPlanted;
    private int penalties;
    private Random random = new Random();

    public Farmer(int id, String country, int treesPlanted) {
        this.id = id;
        this.country = country;
        this.treesPlanted = treesPlanted;
        this.penalties = random.nextInt(10);
    }

    public int getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public int getTreesPlanted() {
        return treesPlanted;
    }

    public int getPenalties() {
        return penalties;
    }

    public void plantTree() {
        treesPlanted++;
    }

    @Override
    public String toString() {
        return "Farmer{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", treesPlanted=" + treesPlanted +
                ", penalties=" + penalties +
                '}';
    }

    @Override
    public Farmer clone() {
        try {
            Farmer cloned = (Farmer) super.clone();
            cloned.random = new Random(); // Crea una nuova istanza di Random per il clone
            return cloned;
        } catch (CloneNotSupportedException e) {
            // Questa eccezione non dovrebbe verificarsi in quanto Farmer implementa Cloneable
            throw new RuntimeException("Errore durante la clonazione dell'oggetto Farmer", e);
        }
    }
}
