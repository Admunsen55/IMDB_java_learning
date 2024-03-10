package productionpack;
import appstructure.IMDB;
import enumpack.Genre;
import experiencepack.ExperienceCalculator;
import java.util.List;
import java.util.LinkedList;
import observerpack.NotificationHub;
import usefulpack.ComparableItem;
import userpack.Staff;
import userpack.User;

public abstract class Production implements ComparableItem {
    private String title;
    private List<String> directors;
    private List<String> actors;
    private List<Genre> genres;
    private List<Rating> ratings;
    private String subject_description;
    private double average_rating;
    private String image_path = "images/default_no_image.png";
    private Staff<ComparableItem> production_contributor;
    public Production(String title, List<String> directors, List<String> actors, List<Genre> genres,
                      List<Rating> ratings, String subject_description, double average_rating) {
        this.title = title;
        this.directors = directors;
        this.actors = actors;
        this.genres = genres;
        this.ratings = ratings;
        this.subject_description = subject_description;
        this.average_rating = average_rating;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<String> getDirectors() {
        return directors;
    }
    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }
    public List<String> getActors() {
        return actors;
    }
    public void setActors(List<String> actors) {
        this.actors = actors;
    }
    public List<Genre> getGenres() {
        return genres;
    }
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
        average_rating = calculateAverageGrade();
    }
    public List<Rating> getRatings() {
        return ratings;
    }
    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
        average_rating = calculateAverageGrade();
    }
    public String getSubjectDescription() {
        return subject_description;
    }
    public void setSubjectDescription(String subject_description) {
        this.subject_description = subject_description;
    }
    public Double getAverageRating() {
        average_rating = calculateAverageGrade();
        return average_rating;
    }
    public Staff<ComparableItem> getContributor() {
        return production_contributor;
    }
    public void setContributor(Staff<ComparableItem> production_contributor) {
        this.production_contributor = production_contributor;
    }
    public void addOrUpdateRating(Rating rating) {
        if (ratings == null) {
            ratings = new LinkedList<>();
        }
        for (Rating aux_rat : ratings) {
            if (aux_rat.getUsername().equals(rating.getUsername())) {
                aux_rat.setGrade(rating.getGrade());
                aux_rat.changeComments(rating.getComments());
                average_rating = calculateAverageGrade();
                NotificationHub.getInstance().notifyObserversAboutRating(this, rating);
                return;
            }
        }
        ratings.add(rating);
        average_rating = calculateAverageGrade();
        NotificationHub.getInstance().notifyObserversAboutRating(this, rating);
        ExperienceCalculator exp_calc = IMDB.getInstance().getExperience_calculator();
        exp_calc.setState(IMDB.getInstance().getLoggedUser(), exp_calc.added_first_review);
        exp_calc.updateUserExperience();
    }
    public void removeRating(String username) {
        for (Rating aux_rat : ratings) {
            if (aux_rat.getUsername().equals(username)) {
                ratings.remove(aux_rat);
                break;
            }
        }
        average_rating = calculateAverageGrade();
    }
    public abstract String toString();
    private Double calculateAverageGrade() {
        double avg_rating = 0.0;
        if (ratings.isEmpty()) {
            return avg_rating;
        }
        for (Rating aux_rat : ratings) {
            avg_rating += aux_rat.getGrade();
        }
        avg_rating /= ratings.size();
        return avg_rating;
    }
    public String getImagePath() {
        return image_path;
    }
    public void setImagePath(String image_path) {
        this.image_path = image_path;
    }
    public static Production getProductionByTitle(List<Production> productions, String title) {
        if (productions != null) {
            for (Production production : productions) {
                if (production.getTitle().equals(title)) {
                    return production;
                }
            }
        }
        return null;
    }
    public abstract void displayInfo();
    @Override
    public String getStringForSorting() {
        return title;
    }
    @Override
    public int compareTo(ComparableItem item) {
        return title.compareTo(item.getStringForSorting());
    }
    public void updateFieldsFrom(Production production) {
        if (production.title != null) {
            this.title = production.title;
        }
        this.directors = production.directors;
        this.actors = production.actors;
        this.genres = production.genres;
        if (production.ratings != null) {
            this.ratings = production.ratings;
        }
        this.subject_description = production.subject_description;
        this.average_rating = production.average_rating;
        if (production.production_contributor != null) {
            this.production_contributor = production.production_contributor;
        }
    }
}