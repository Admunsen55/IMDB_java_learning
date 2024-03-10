package userpack;
import appstructure.IMDB;
import enumpack.AccountType;
import java.util.List;
import java.util.SortedSet;
import observerpack.NotificationHub;
import observerpack.Request;
import usefulpack.ComparableItem;
import productionpack.Production;
import productionpack.Rating;

public class Regular<T extends ComparableItem> extends User<T> implements RequestManager {
    public Regular(){super();}
    public Regular(Information info, AccountType account_type, String username, int experience,
                   List<String> notifications, SortedSet<T> favorites) {
        super(info, account_type, username, experience, notifications, favorites);
    }
    @Override
    public void createRequest(Request request) {
        String request_target_username = request.getRequestTarget().getUsername();
        if (request_target_username.equals("ADMIN")) {
            Request.RequestHolder.addRequest(request);
        } else {
            request.getRequestTarget().addPersonalRequest(request);
            IMDB.getInstance().addRequest(request);
        }
        //NotificationHub.getInstance().notifyObserversAboutRequest(request, null);
        request.getRequestTarget().addNotification("You have a new request from: " + getUsername()
                                                    + " regarding: " + request.getSubject());
    }
    @Override
    public void removeRequest(Request request) {
        String request_target_username = request.getRequestTarget().getUsername();
        if (request_target_username.equals("ADMIN")) {
            Request.RequestHolder.removeRequest(request);
        } else {
            request.getRequestTarget().removePersonalRequest(request);
            IMDB.getInstance().removeRequest(request);
        }
        request.getRequestTarget().addNotification("Your request was removed by: " + getUsername());
    }
    @Override
    public void displayInfo() {
        System.out.print(super.toString());
    }
    @Override
    public void updateAboutRating(Production production, Rating rating) {
        if (production.getRatings() == null) {
            return;
        }
        //tratam cazul cand userul curent a dat reating-ul, deci n-ar trebui sa fie notificat
        if (rating.getUsername().equals(getUsername())) {
            return;
        }
        for (Rating production_rating : production.getRatings()) {
            if (production_rating.getUsername().equals(getUsername())) {
                addNotification("Your reviewed production: " + production.getTitle() + " was also rated by: "
                        + rating.getUsername() + " with: " + rating.getGrade() + " regarding: " + rating.getComments());
                return;
            }
        }
    }
    @Override
    public void updateAboutRequest(Request request, Boolean accept) {
        if (accept == null || !request.getRequester().getUsername().equals(getUsername())) {
            return;
        }
        if (accept == true) {
            addNotification("Your request was solved by: " + request.getRequestTarget().getUsername()
                    + " regarding: " + request.getSubject());
        } else {
            addNotification("Your request was rejected by: " + request.getRequestTarget().getUsername()
                    + " regarding: " + request.getSubject());
        }
    }
}
