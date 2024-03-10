package userpack;
import enumpack.AccountType;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import observerpack.NotificationHub;
import observerpack.Request;
import usefulpack.ComparableItem;
import appstructure.IMDB;
import productionpack.Production;
import productionpack.Rating;
import productionpack.Actor;

public class Admin <T extends ComparableItem> extends Staff<T> implements AdminInterface{
    public Admin() {
        super();
    }
    public Admin(Information info, AccountType account_type, String username, int experience,
                 List<String> notifications, SortedSet<T> favorites, SortedSet<T> contributions) {
        super(info, account_type, username, experience, notifications, favorites, contributions);
    }
    @Override
    public String requestsToString() {
        StringBuilder aux_string = new StringBuilder();
        List<Request> common_requests = Request.RequestHolder.getRequests();
        if (common_requests != null) {
            for (Request request : common_requests) {
                aux_string.append(request.toString()).append("\n\n");
            }
        }
        if (getPersonalRequests() != null) {
            for (Request request : getPersonalRequests()) {
                aux_string.append(request.toString()).append("\n\n");
            }
        }
        if (aux_string.isEmpty()) {
            return "No requests";
        }
        return aux_string.toString();
    }
    @Override
    public String contributionsToString() {
        StringBuilder aux_string = new StringBuilder();
        int has_contributions = 0;
        for (ComparableItem contribution : IMDB.getInstance().getCommonAdmin().getContributions()) {
            aux_string.append(contribution.getStringForSorting()).append("\n");
            has_contributions = 1;
        }
        for (T contribution : getContributions()) {
            aux_string.append(contribution.getStringForSorting()).append("\n");
            has_contributions = 1;
        }
        if (has_contributions == 0) {
            return "No contributions";
        }
        return aux_string.toString();
    }
    @Override
    public void addUserSystem(User<ComparableItem> user) {
        if (User.getUserByUsername(user.getUsername(), IMDB.getInstance().getUsers()) != null) {
            throw new IllegalArgumentException("User already exists");
        }
        IMDB.getInstance().getUsers().add(user);
        NotificationHub.getInstance().registerObserver(user);
    }
    @Override
    public void removeUserSystem(User<ComparableItem> user) {
        IMDB imdb = IMDB.getInstance();
        if (imdb.getLoggedUser() == user) {
            throw new IllegalArgumentException("Cannot remove yourself");
        }
        if (user.getAccountType() == AccountType.REGULAR) {
            // Remove user from ratings
            if (imdb.getProductions() != null) {
                for (Production production : imdb.getProductions()) {
                    if (production.getRatings() == null) {
                        continue;
                    }
                    Iterator<Rating> iterator = production.getRatings().iterator();
                    while (iterator.hasNext()) {
                        Rating rating = iterator.next();
                        if (rating.getUsername().equals(user.getUsername())) {
                            iterator.remove();
                        }
                    }
                }
            }
            // Remove requests issued by user
            if (imdb.getRequests() != null) {
                Iterator<Request> iterator = imdb.getRequests().iterator();
                while (iterator.hasNext()) {
                    Request request = iterator.next();
                    if (request.getRequester().getUsername().equals(user.getUsername())) {
                        request.getRequestTarget().removePersonalRequest(request);
                        iterator.remove();
                    }
                }
            }
            if (Request.RequestHolder.getRequests() != null) {
                Iterator<Request> iterator = Request.RequestHolder.getRequests().iterator();
                while (iterator.hasNext()) {
                    Request request = iterator.next();
                    if (request.getRequester().getUsername().equals(user.getUsername())) {
                        iterator.remove();
                    }
                }
            }
        } else if (user.getAccountType() == AccountType.CONTRIBUTOR) {
            if (imdb.getProductions() != null) {
                for (Production production : imdb.getProductions()) {
                    if (production.getContributor().getUsername().equals(user.getUsername())) {
                        production.setContributor(imdb.getCommonAdmin());
                        imdb.getCommonAdmin().addContribution(production);
                    }
                }
            }
            if (imdb.getActors() != null) {
                for (Actor actor : imdb.getActors()) {
                    if (actor.getContributor().getUsername().equals(user.getUsername())) {
                        actor.setContributor(imdb.getCommonAdmin());
                        imdb.getCommonAdmin().addContribution(actor);
                    }
                }
            }
            List<Request> personal_requests = ((Staff<T>) user).getPersonalRequests();
            if (personal_requests != null) {
                for (Request request : personal_requests) {
                    request.setRequestTarget(imdb.getCommonAdmin());
                    Request.RequestHolder.addRequest(request);
                }
            }
        } else {
            throw new IllegalArgumentException("Cannot remove admin");
        }
        imdb.getUsers().remove(user);
        NotificationHub.getInstance().removeObserver(user);
    }
    @Override
    public void updateUser(User<ComparableItem> old_user, User<ComparableItem> new_user) {

    }
    @Override
    public void updateAboutRequest(Request request, Boolean accept) {
        if (accept == null) {
            if (request.getRequestTarget().getUsername().equals("ADMIN")) {
                addNotification("A new request has been issued by " + request.getRequester().getUsername()
                        + " to ADMINS regarding: " + request.getSubject());
            }
        }
        super.updateAboutRequest(request, accept);
    }
}
