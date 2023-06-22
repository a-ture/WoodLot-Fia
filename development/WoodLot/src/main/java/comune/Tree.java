package comune;

public class Tree {
    private int id;
    private String country;

    public Tree(int id, String country) {
        this.id = id;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "Tree [id=" + id + ", country=" + country + "]";
    }

}
