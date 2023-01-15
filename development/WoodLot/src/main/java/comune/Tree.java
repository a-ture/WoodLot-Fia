package comune;

public class Tree {
    private int id;
    private String country;


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

    public Tree(int id, String country) {
        this.id = id;
        this.country = country;
    }
}
