package userpack;
import appstructure.IMDB;
import enumpack.AccountType;
import experiencepack.ExperienceCalculator;
import experiencepack.ExperienceStrategy;
import java.util.*;
import observerpack.NotificationHub;
import productionpack.Actor;
import observerpack.Request;
import productionpack.Production;
import productionpack.Rating;
import usefulpack.ComparableItem;

public abstract class Staff<T extends ComparableItem> extends User<T> implements StaffInterface {
    private List<Request> requests;
    private SortedSet<T> contributions;
    public Staff(){super();}
    public Staff(Information info, AccountType account_type, String username, int experience,
                 List<String> notifications, SortedSet<T> favorites) {
        super(info, account_type, username, experience, notifications, favorites);
    }
    public Staff(Information info, AccountType account_type, String username, int experience,
                 List<String> notifications, SortedSet<T> favorites, SortedSet<T> contributions) {
        super(info, account_type, username, experience, notifications, favorites);
        this.contributions = contributions;
    }
    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
    public List<Request> getPersonalRequests() {
        return requests;
    }
    public void addPersonalRequest(Request request) {
        if (requests == null) {
            requests = new LinkedList<>();
        }
        requests.add(request);
    }

    public void removePersonalRequest(Request request) {
        if (requests == null) {
            return;
        }
        requests.remove(request);
    }
    public void setContributions(SortedSet<T> contributions) {
        this.contributions = contributions;
    }
    public void addContribution(T contribution) {
        if (contributions == null) {
            contributions = new TreeSet<>();
        }
        contributions.add(contribution);
    }
    public void removeContribution(T contribution) {
        if (contributions == null) {
            return;
        }
        contributions.remove(contribution);
    }
    public SortedSet<T> getContributions() {
        return contributions;
    }
    public void markContributions() {
        if (contributions == null) {
            return;
        }
        for (T contribution : contributions) {
            if (contribution instanceof Production) {
                ((Production) contribution).setContributor((Staff<ComparableItem>) this);
            } else if (contribution instanceof Actor) {
                ((Actor) contribution).setContributor((Staff<ComparableItem>) this);
            }
        }
    }
    @Override
    public void addProductionSystem(Production p) {
        IMDB imdb = IMDB.getInstance();
        if (Production.getProductionByTitle(imdb.getProductions(), p.getTitle()) != null) {
            throw new IllegalArgumentException("Production already exists");
        }
        p.setContributor((Staff<ComparableItem>)this);
        this.addContribution((T)p);
        imdb.getProductions().add(p);
        ExperienceCalculator exp_calc = imdb.getExperience_calculator();
        exp_calc.added_new_production_actor.setItem(p);
        exp_calc.setState((User<ComparableItem>)this, exp_calc.added_new_production_actor);
        exp_calc.updateUserExperience();
    }
    @Override
    public void addActorSystem(Actor a) {
        IMDB imdb = IMDB.getInstance();
        if (Actor.getActorByName(imdb.getActors(), a.getName()) != null) {
            throw new IllegalArgumentException("Actor already exists");
        }
        a.setContributor((Staff<ComparableItem>)this);
        this.addContribution((T)a);
        imdb.getActors().add(a);
        ExperienceCalculator exp_calc = imdb.getExperience_calculator();
        exp_calc.added_new_production_actor.setItem(a);
        exp_calc.setState((User<ComparableItem>)this, exp_calc.added_new_production_actor);
        exp_calc.updateUserExperience();
    }
    @Override
    public void removeProductionSystem(String name) {
        IMDB imdb = IMDB.getInstance();
        Production aux_prod = Production.getProductionByTitle(imdb.getProductions(), name);
        if (aux_prod == null) {
            throw new IllegalArgumentException("Production not found");
        }
        String contributor_username = aux_prod.getContributor().getUsername();
        if (contributor_username.equals("ADMIN")) {
            if (this.getAccountType() == AccountType.ADMIN) {
                imdb.getProductions().remove(aux_prod);
                imdb.getCommonAdmin().removeContribution(aux_prod);
            } else {
                throw new IllegalArgumentException("You don't have permission to remove this production");
            }
        } else if (contributor_username.equals(this.getUsername())) {
            imdb.getProductions().remove(aux_prod);
            this.removeContribution((T) aux_prod);
        } else {
            throw new IllegalArgumentException("You don't have permission to remove this production");
        }
        for (User<ComparableItem> user : imdb.getUsers()) {
            if (user.getFavorites() != null) {
                try {
                    user.removeFavoriteByName(name);
                } catch (NoSuchElementException e) {
                    //do nothing
                }
                //some signaling mechanism for notifications later
            }
        }
    }
    @Override
    public void removeActorSystem(String name) {
        IMDB imdb = IMDB.getInstance();
        Actor aux_actor = Actor.getActorByName(imdb.getActors(), name);
        if (aux_actor == null) {
            throw new IllegalArgumentException("Actor not found");
        }
        String contributor_username = aux_actor.getContributor().getUsername();
        if (contributor_username.equals("ADMIN")) {
            if (this.getAccountType() == AccountType.ADMIN) {
                imdb.getActors().remove(aux_actor);
                imdb.getCommonAdmin().removeContribution(aux_actor);
            } else {
                throw new IllegalArgumentException("You don't have permission to remove this actor");
            }
        } else if (contributor_username.equals(this.getUsername())) {
            imdb.getActors().remove(aux_actor);
            this.removeContribution((T) aux_actor);
        } else {
            throw new IllegalArgumentException("You don't have permission to remove this actor");
        }
        for (User<ComparableItem> user : imdb.getUsers()) {
            if (user.getFavorites() != null) {
                try {
                    user.removeFavoriteByName(name);
                } catch (NoSuchElementException e) {
                    //do nothing
                }
                //some signaling mechanism for notifications later
            }
        }
    }
    @Override
    public void updateProduction(Production old_p, Production new_p) {
//        Production old_prod = Production.getProductionByTitle(imdb.getProductions(), p.getTitle());
//        if (old_prod == null) {
//            throw new IllegalArgumentException("Production not found");
//        }
        String contributor_username = old_p.getContributor().getUsername();
        if (contributor_username.equals("ADMIN")) {
            if (this.getAccountType() != AccountType.ADMIN) {
                throw new IllegalArgumentException("You don't have permission to update this production");
            }
        }
        if (!contributor_username.equals(this.getUsername())) {
            throw new IllegalArgumentException("You don't have permission to update this production");
        }
        old_p.updateFieldsFrom(new_p);
    }
    @Override
    public void updateActor(Actor old_a, Actor new_a) {
//        Actor old_actor = Actor.getActorByName(imdb.getActors(), a.getName());
//        if (old_actor == null) {
//            throw new IllegalArgumentException("Actor not found");
//        }
        String contributor_username = old_a.getContributor().getUsername();
        if (contributor_username.equals("ADMIN")) {
            if (this.getAccountType() != AccountType.ADMIN) {
                throw new IllegalArgumentException("You don't have permission to update this actor");
            }
        }
        if (!contributor_username.equals(this.getUsername())) {
            throw new IllegalArgumentException("You don't have permission to update this actor");
        }
        old_a.updateFieldsFrom(new_a);
    }
    @Override
    public void solveRequest(Request request, Boolean accept) {
        String request_target_username = request.getRequestTarget().getUsername();
        if (request_target_username.equals("ADMIN")) {
            if (this.getAccountType() != AccountType.ADMIN) {
                throw new IllegalArgumentException("You don't have permission to solve this request");
            } else {
                Request.RequestHolder.removeRequest(request);
            }
        } else {
            if (!request_target_username.equals(this.getUsername())) {
                throw new IllegalArgumentException("You don't have permission to solve this request");
            } else {
                request.getRequestTarget().removePersonalRequest(request);
                IMDB.getInstance().removeRequest(request);
            }
        }
        //NotificationHub.getInstance().notifyObserversAboutRequest(request, accept);
        if (accept == false) {
            request.getRequester().addNotification("Your request was rejected by: " + getUsername()
                    + " regarding: " + request.getSubject());
            return;
        }
        if (accept == true) {
            request.getRequester().addNotification("Your request was solved by: " + getUsername()
                    + " regarding: " + request.getSubject());
        }
        ExperienceCalculator exp_calc = IMDB.getInstance().getExperience_calculator();
        exp_calc.request_solved.setRequest(request);
        exp_calc.setState(request.getRequester(), exp_calc.request_solved);
        exp_calc.updateUserExperience();
    }
    @Override
    public void updateAboutRating(Production production, Rating rating) {
        if (production.getContributor().getUsername().equals(getUsername())) {
            addNotification("Your added production: " + production.getTitle()
                    + " got a rating of " + rating.getGrade() + " from: " + rating.getUsername()
                    + " regarding: " + rating.getComments());
        }
    }
    @Override
    public void updateAboutRequest(Request request, Boolean accept) {
        if (accept == null) {
            if (request.getRequestTarget().getUsername().equals(getUsername())) {
                addNotification("A new request has been issued by: " + request.getRequester().getUsername()
                        + " to you regarding: " + request.getSubject());
            }
        }
    }
    public String toString() {
        StringBuilder aux_string = new StringBuilder();
        aux_string.append(super.toString());
        aux_string.append("Contributions:\n");
        for (T contribution : contributions) {
            aux_string.append(contribution.getStringForSorting()).append("\n");
        }
        return aux_string.toString();
    }
    public abstract String requestsToString();
    public abstract String contributionsToString();
    @Override
    public void displayInfo() {
        System.out.print(this);
    }
    @Override
    public void updateFieldsFrom(User<T> user) {
        if (!(user instanceof Staff)) {
            throw new IllegalArgumentException("User to update from is not a staff member");
        }
        super.updateFieldsFrom(user);
        Staff<T> staff = (Staff<T>) user;
        if (staff.contributions != null) {
            this.contributions = staff.contributions;
        }
        if (staff.requests != null) {
            this.requests = staff.requests;
        }
    }


}
