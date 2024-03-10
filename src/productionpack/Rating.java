package productionpack;
import appstructure.IMDB;
import usefulpack.ComparableItem;
import userpack.User;
import java.util.List;

public class Rating implements Comparable<Rating> {
    private String username;
    private int grade;
    private String comments;
    //vezi cu asta
    public Rating(String username, int grade, String comments) {
        if (grade < 1 || grade > 10) {
            throw new IllegalArgumentException("Movie evaluation should have a grade between 1 and 10");
        }
        this.username = username;
        this.grade = grade;
        this.comments = comments;
    }
    public String getUsername() {
        return username;
    }
    public int getGrade() {
        return grade;
    }
    public void setGrade(int new_grade) {
        if (new_grade < 1 || new_grade > 10) {
            throw new IllegalArgumentException("Movie evaluation should have a grade between 1 and 10");
        }
        grade = new_grade;
    }
    public String getComments() {
        return comments;
    }
    public void changeComments(String new_comments) {
        comments = new_comments;
    }
    public String toString() {
        return  "Username: " + username +
                " | grade: " + grade +
                " | comments: " + comments + "\n";
    }
    @Override
    public int compareTo(Rating r) {
        List<User<ComparableItem>> users = IMDB.getInstance().getUsers();
        User<ComparableItem> u1 = User.getUserByUsername(username, users);
        User<ComparableItem> u2 = User.getUserByUsername(r.getUsername(), users);
        return u1.getExperience() - u2.getExperience();
    }
}
