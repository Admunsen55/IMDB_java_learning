package productionpack;

public class Episode {
    private String name;
    private String duration;
    public Episode(String name, String duration) {
        this.name = name;
        this.duration = duration;
    }

    public String toString() {
        return name + " " + duration;
    }
}
