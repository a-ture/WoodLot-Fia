package comune;

import java.util.Objects;

public class Farmer {
    private int id;
    private String country;

    private int treesPlanted;

    public Farmer(int id, String country, int treesPlanted) {
        this.id = id;
        this.country = country;
        this.treesPlanted = treesPlanted;
    }

    public int getTreesPlanted() {
        return treesPlanted;
    }

    public void setTreesPlanted(int treesPlanted) {
        this.treesPlanted = treesPlanted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void plantTree() {
        this.treesPlanted++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Farmer)) return false;
        Farmer farmer = (Farmer) o;
        return id == farmer.id && treesPlanted == farmer.treesPlanted && country.equals(farmer.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, country, treesPlanted);
    }

    @Override
    public String toString() {
        return "Farmer{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", treesPlanted=" + treesPlanted +
                '}';
    }
}
