package comune;

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

}
