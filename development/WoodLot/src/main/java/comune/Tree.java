package comune;

public class Tree implements Cloneable {
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

    @Override
    public Tree clone() {
        try {
            return (Tree) super.clone();
        } catch (CloneNotSupportedException e) {
            // Questa eccezione non dovrebbe verificarsi in quanto Tree implementa Cloneable
            throw new RuntimeException("Errore durante la clonazione dell'oggetto Tree", e);
        }
    }

}
