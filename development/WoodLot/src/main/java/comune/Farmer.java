package comune;

import java.util.Objects;
import java.util.Random;

public class Farmer {
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
        ;
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

    public void incrementPenalties() {
        penalties++;
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
}
