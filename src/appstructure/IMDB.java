package appstructure;
import experiencepack.ExperienceCalculator;
import guipack.GUI;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;
import enumpack.AccountType;
import observerpack.NotificationHub;
import observerpack.Request;
import productionpack.Actor;
import productionpack.Production;
import usefulpack.UtilFunction;
import userpack.Admin;
import usefulpack.ComparableItem;
import userpack.Staff;
import userpack.User;

public class IMDB {
    private static IMDB instance = null;
    private List<Actor> actors;
    private List<Production> productions;
    private List<Request> requests;
    private List<User<ComparableItem>> users;
    private User<ComparableItem> common_admin = null;
    private User<ComparableItem> logged_user = null;
    private final ExperienceCalculator experience_calculator;

    private void initCommonAdmin() {
        common_admin = new Admin<>();
        common_admin.setUsername("ADMIN");
        common_admin.setAccountType(AccountType.ADMIN);
    }
    private IMDB() {
        experience_calculator = new ExperienceCalculator();
        initCommonAdmin();
    }
    public static IMDB getInstance() {
        if (instance == null) {
            instance = new IMDB();
        }
        return instance;
    }

    public Admin<ComparableItem> getCommonAdmin() {
        return (Admin<ComparableItem>) common_admin;
    }
    public ExperienceCalculator getExperience_calculator() {
        return experience_calculator;
    }
    public List<Actor> getActors() {
        return actors;
    }
    public List<Request> getRequests() {
        return requests;
    }
    public List<Production> getProductions() {
        return productions;
    }
    public List<User<ComparableItem>> getUsers() {
        return users;
    }
    public void addRequest(Request request) {
        if (requests == null) {
            requests = new LinkedList<>();
        }
        requests.add(request);
    }
    public void removeRequest(Request request) {
        requests.remove(request);
    }
    public User<ComparableItem> getLoggedUser() {
        return logged_user;
    }
    public void setLoggedUser(User<ComparableItem> logged_user) {
        this.logged_user = logged_user;
    }
    public void prepareData() {
        actors = ReadJsonUtil.getActorsFromJsonFile("JSON/actors.json");
        productions = ReadJsonUtil.getProductionsFromJsonFile("JSON/production.json", actors);
        users = ReadJsonUtil.getUsersFromJsonFile("JSON/accounts.json", actors, productions);
        ReadJsonUtil.getRequestsFromJsonFile("JSON/requests.json", users);
        for (User<ComparableItem> aux_user : users) {
            if (!aux_user.getAccountType().equals(AccountType.REGULAR)) {
                Staff<ComparableItem> aux_staff = (Staff<ComparableItem>) aux_user;
                aux_staff.markContributions();
            }
        }
        NotificationHub.getInstance();
        for (User<ComparableItem> aux_user : users) {
            NotificationHub.getInstance().registerObserver(aux_user);
        }
        uploadProductionsImages();
        uploadActorsImages();
    }
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Choose app mode: Type 1 for terminal mode or 2 for GUI mode");
            int mode = scanner.nextInt();
            scanner.nextLine();
            if (mode == 1) {
                TerminalUserInterface UI = new TerminalUserInterface();
                UI.runInterface();
            } else if (mode == 2) {
                GUI gui = new GUI();
                gui.runInterface();
            } else {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            System.err.println("Invalid mode");
        }
    }

    public void uploadProductionsImages() {
        List<String> productions_with_images = UtilFunction.readLinesFromFile("images/movies_with_images");
        try {
            for (Production production : productions) {
                if (productions_with_images.contains(production.getTitle())) {
                    production.setImagePath("images/" + production.getTitle() + ".png");
                }
            }
        } catch (Exception e) {
            System.err.println("Error uploading movies images: " + e.getMessage());
        }
    }

    public void uploadActorsImages() {
        List<String> actors_with_images = UtilFunction.readLinesFromFile("images/actors_with_images");
        try {
            for (Actor actor : actors) {
                if (actors_with_images.contains(actor.getName())) {
                    actor.setImagePath("images/" + actor.getName() + ".png");
                }
            }
        } catch (Exception e) {
            System.err.println("Error uploading actors images: " + e.getMessage());
        }
    }
}