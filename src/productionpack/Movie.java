package productionpack;
import enumpack.Genre;
import java.util.List;
import usefulpack.UtilFunction;

public class Movie extends Production {
    private String duration;
    private int year_of_release;
    public Movie (String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings,
                  String subject_description, double average_rating, String duration, int year_of_release) {
        super(title,directors, actors, genres, ratings, subject_description, average_rating);
        this.duration = duration;
        this.year_of_release = year_of_release;
    }
    public String getDuration() {
        return duration;
    }
    public int getYearOfRelease() {
        return year_of_release;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public void setYearOfRelease(int year_of_release) {
        this.year_of_release = year_of_release;
    }
    @Override
    public String toString() {
        StringBuilder aux_string = new StringBuilder();
        aux_string.append("Movie: ").append(getTitle()).append("\n");
        aux_string.append("Directors: ");
        for (String director : getDirectors()) {
            aux_string.append(director).append("  ");
        }
        aux_string.append("\nActors: ");
        for (String actor : getActors()) {
            aux_string.append(actor).append("  ");
        }
        aux_string.append("\nGenres: ");
        for (Genre genre : getGenres()) {
            aux_string.append(genre).append("  ");
        }
        if (getRatings() == null) {
            aux_string.append("\nNo ratings yet\n");
        } else {
            aux_string.append("\nRatings:\n");
            for (Rating rating : getRatings()) {
                aux_string.append("   ").append(rating.toString());
            }
            aux_string.append("Average rating: ").append(UtilFunction.getOnlyTwoDecimals(getAverageRating())).append("\n");
        }
        aux_string.append("Description: ").append(getSubjectDescription()).append("\n");
        aux_string.append("Duration: ").append(duration).append("\nYear of release: ").append(year_of_release).append("\n");
        aux_string.append("Contributed by: ").append(getContributor().getUsername()).append("\n\n");
        return aux_string.toString();
    }
    @Override
    public void displayInfo() {
        System.out.print(this);
    }
    @Override
    public void updateFieldsFrom(Production production) {
        if (!(production instanceof Movie)) {
            throw new IllegalArgumentException("Production to update from is not a movie");
        }
        super.updateFieldsFrom(production);
        this.duration = ((Movie) production).duration;
        this.year_of_release = ((Movie) production).year_of_release;
    }
}
