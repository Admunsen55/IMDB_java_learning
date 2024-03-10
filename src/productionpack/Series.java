package productionpack;

import enumpack.Genre;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import usefulpack.UtilFunction;

public class Series extends Production {
    private int year_of_release;
    private int number_of_seasons;
    private Map<String, List<Episode>> episodes;
    public Series (String title, List<String> directors, List<String> actors, List<Genre> genres,
                   List<Rating> ratings, String subject_description, double average_rating, int year_of_release, int number_of_seasons) { /*,
                    Map<String, List<Episode>> episodes) { */
        super(title,directors, actors, genres, ratings, subject_description, average_rating);
        this.year_of_release = year_of_release;
        this.number_of_seasons = number_of_seasons;
        //this.episodes = episodes;
    }
    public int getYearOfRelease() {
        return year_of_release;
    }
    public void setYearOfRelease(int year_of_release) {
        this.year_of_release = year_of_release;
    }
    public int getNumberOfSeasons () {
        return number_of_seasons;
    }
    public void setNumberOfSeasons(int number_of_seasons) {
        this.number_of_seasons = number_of_seasons;
    }
    public Map<String, List<Episode>> getEpisodes() {
        return episodes;
    }
    public void setEpisodes(Map<String, List<Episode>> episodes) {
        this.episodes = episodes;
    }
    public void addEpisodesForSeason(String season, List<Episode> episodes_for_season) {
       if (episodes == null) {
           episodes = new HashMap<>();
       }
       episodes.put(season, episodes_for_season);
    }
    @Override
    public String toString() {
        StringBuilder aux_string = new StringBuilder();
        aux_string.append("Series: ").append(getTitle()).append("\n");
        aux_string.append("Directors: ");
        for (String director : getDirectors()) {
            aux_string.append(director).append(", ");
        }
        aux_string.append("\nActors: ");
        for (String actor : getActors()) {
            aux_string.append(actor).append(", ");
        }
        aux_string.append("\nGenres: ");
        for (Genre genre : getGenres()) {
            aux_string.append(genre).append(", ");
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
            aux_string.append("Contributed by: ").append(getContributor().getUsername()).append("\n");
        aux_string.append("Year of release: ").append(year_of_release).append("\nNumber of seasons: ").append(number_of_seasons).append("\n\n");
        episodes.forEach((season, episodes_for_season) -> {
            aux_string.append(season).append(":\n");
            for (Episode episode : episodes_for_season) {
                aux_string.append("   ").append(episode.toString()).append("\n");
            }
            aux_string.append("\n");
        });
        aux_string.append("\n");
        return aux_string.toString();
    }
    @Override
    public void displayInfo() {
        System.out.print(this);
    }
    @Override
    public void updateFieldsFrom(Production production) {
        if (!(production instanceof Series)) {
            throw new IllegalArgumentException("Production to update from is not a series");
        }
        super.updateFieldsFrom(production);
        this.year_of_release = ((Series) production).year_of_release;
        this.number_of_seasons = ((Series) production).number_of_seasons;
        this.episodes = ((Series) production).episodes;
    }
}