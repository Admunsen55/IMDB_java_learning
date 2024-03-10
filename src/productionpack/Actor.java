package productionpack;
import java.util.List;
import usefulpack.Pair;
import usefulpack.ComparableItem;
import java.util.ArrayList;
import userpack.Staff;

public class Actor implements ComparableItem {
    private String name;
    //pereche tip(Movie, Series) si titlu
    private List<Pair<String, String>> filmography;
    private String biography;
    private Staff<ComparableItem> actor_contributor;
    private String imagePath = "images/default_no_image.png";
    public Actor(){}
    public Actor(String name, List<Pair<String, String>> filmography, String biography) {
        this.name = name;
        this.filmography = filmography;
        this.biography = biography;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBiography() {
        return biography;
    }
    public void setBiography(String biography) {
        this.biography = biography;
    }
    public Staff<ComparableItem> getContributor() {
        return actor_contributor;
    }
    public void setContributor(Staff<ComparableItem> actor_contributor) {
        this.actor_contributor = actor_contributor;
    }
    public void addFilmography(Pair<String, String> new_filmography) {
        if (filmography == null) {
            filmography = new ArrayList<>();
        }
        for (Pair<String, String> pair : this.filmography) {
            if (pair.equals(new_filmography)) {
                throw new IllegalArgumentException("Already in filmography");
            }
        }
        filmography.add(new_filmography);
    }
    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public static Actor getActorByName(List<Actor> actors, String name) {
        if (actors != null) {
            for (Actor actor : actors) {
                if (actor.getName().equals(name)) {
                    return actor;
                }
            }
        }
        return null;
    }
    public String toString() {
        StringBuilder aux_string = new StringBuilder();
        aux_string.append("Actor: ").append(getName()).append("\nFilmography:\n");
        for (Pair<String, String> pair : filmography) {
            aux_string.append(pair.toString()).append("\n");
        }
        if (biography != null) {
            aux_string.append("Biography: ").append(biography).append("\n");
        } else {
            aux_string.append("Biography: none\n");
        }
        aux_string.append("Contributed by: ").append(actor_contributor.getUsername()).append("\n");
        return aux_string.toString();
    }
    public void displayInfo() {
        System.out.println(this);
    }
    @Override
    public String getStringForSorting() {
        return name;
    }
    @Override
    public int compareTo(ComparableItem item) {
        return name.compareTo(item.getStringForSorting());
    }
    public void updateFieldsFrom(Actor actor) {
        this.name = actor.name;
        this.filmography = actor.filmography;
        this.biography = actor.biography;
        if (actor.getContributor() != null) {
            this.actor_contributor = actor.actor_contributor;
        }
    }
}