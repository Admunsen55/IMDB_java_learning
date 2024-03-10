package userpack;
import appstructure.IMDB;
import enumpack.AccountType;
import java.util.List;
import java.util.SortedSet;
import observerpack.Request;
import usefulpack.ComparableItem;

public class Contributor<T extends ComparableItem> extends Staff<T> implements RequestManager {
    /*
    public Contributor(Information info) {
        super(info);
    }
    */
    public Contributor(Information info, AccountType account_type, String username, int experience,
                       List<String> notifications, SortedSet<T> favorites) {
        super(info, account_type, username, experience, notifications, favorites);
    }
    public Contributor(Information info, AccountType account_type, String username, int experience,
                       List<String> notifications, SortedSet<T> favorites, SortedSet<T> contributions) {
        super(info, account_type, username, experience, notifications, favorites, contributions);
    }
    @Override
    public void createRequest(Request request) {
        if (request.getRequestTarget().getUsername().equals(getUsername())) {
            return;
        }
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
    public String requestsToString() {
        StringBuilder aux_string = new StringBuilder();
        if (getPersonalRequests() == null) {
            return "No requests";
        }
        for (Request request : getPersonalRequests()) {
            aux_string.append(request.toString() + "\n\n");
        }
        return aux_string.toString();
    }
    @Override
    public String contributionsToString() {
        StringBuilder aux_string = new StringBuilder();
        if (getContributions() == null) {
            return "No contributions";
        }
        for (T contribution : getContributions()) {
            aux_string.append(contribution.getStringForSorting() + "\n");
        }
        return aux_string.toString();
    }
    @Override
    public void updateAboutRequest(Request request, Boolean accept) {
        if (accept != null) {
            if (request.getRequester().getUsername().equals(getUsername())) {
                addNotification("Your request was solved by: " + request.getRequestTarget().getUsername()
                        + " regarding: " + request.getSubject());
            }
        }
        super.updateAboutRequest(request, accept);
    }
}
