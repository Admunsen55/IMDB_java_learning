package appstructure;
import enumpack.AccountType;
import enumpack.Gender;
import enumpack.Genre;
import enumpack.RequestTypes;
import java.util.*;
import observerpack.Request;
import productionpack.*;
import usefulpack.ComparableItem;
import usefulpack.UtilFunction;
import userpack.*;
import usefulpack.Pair;
import userpack.Credentials;
import java.time.LocalDate;

public class TerminalUserInterface {
    private final IMDB imdb;
    public TerminalUserInterface() {
        imdb = IMDB.getInstance();
    }

    public void runInterface() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            while (imdb.getLoggedUser() == null) {
                System.out.println("Type EXIT if you want to exit the app or anything else to continue");
                if (scanner.nextLine().equals("EXIT")) {
                    scanner.close();
                    return;
                }
                System.out.println("Introduce email: ");
                String email = scanner.nextLine();
                System.out.println("Introduce password: ");
                String password = scanner.nextLine();
                imdb.setLoggedUser(User.getUserByCredentials(email, password, imdb.getUsers()));
                if (imdb.getLoggedUser() != null) {
                    System.out.println("Welcome back " + imdb.getLoggedUser().getUsername() + "!"
                                        + "\nYou have " + imdb.getLoggedUser().getNotifications().size() + " notifications");
                    if (imdb.getLoggedUser().getExperience() != -1) {
                        System.out.println("Experience: " + imdb.getLoggedUser().getExperience());
                    } else {
                        System.out.println("Experience: INFINITY");
                    }
                }
            }
            while (imdb.getLoggedUser() != null) {
                switch (imdb.getLoggedUser().getAccountType()) {
                    case REGULAR:
                        showRegularOptions(scanner);
                        break;
                    case CONTRIBUTOR:
                        showContributorOptions(scanner);
                        break;
                    case ADMIN:
                        showAdminOptions(scanner);
                        break;
                    default:
                        imdb.setLoggedUser(null);
                        break;
                }
            }
        }
    }
    private void showRegularOptions(Scanner scanner) {
        int option;
        System.out.println("\nChoose action:");
        System.out.println("    1. View productions details");
        System.out.println("    2. View actors details");
        System.out.println("    3. Search for actor");
        System.out.println("    4. Search for production");
        System.out.println("    5. Add favorite");
        System.out.println("    6. Remove favorite");
        System.out.println("    7. Add review");
        System.out.println("    8. Remove review");
        System.out.println("    9. Issue a request");
        System.out.println("    10. Retract a request");
        System.out.println("    11. View my personal details");
        System.out.println("    12. View notifications");
        System.out.println("    13. Delete notification");
        System.out.println("    14. Logout");
        try {
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    viewProductionsDetails(scanner);
                    break;
                case 2:
                    viewActorsDetails(scanner);
                    break;
                case 3:
                    searchActor(scanner);
                    break;
                case 4:
                    searchProduction(scanner);
                    break;
                case 5:
                    addFavorite(scanner);
                    break;
                case 6:
                    removeFavorite(scanner);
                    break;
                case 7:
                    addReview(scanner);
                    break;
                case 8:
                    removeReview(scanner);
                    break;
                case 9:
                    issueRequest(scanner);
                    break;
                case 10:
                    retractRequest(scanner);
                    break;
                case 11:
                    viewPersonalDetails();
                    break;
                case 12:
                    viewNotifications();
                    break;
                case 13:
                    deleteNotification();
                    break;
                case 14:
                    loggoff();
                    break;
                default:
                    System.err.println("Invalid option");
                    break;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
        }
    }
    private void showContributorOptions(Scanner scanner) {
        int option;
        System.out.println("\nChoose action:");
        System.out.println("    1. View productions details");
        System.out.println("    2. View actors details");
        System.out.println("    3. Search for actor");
        System.out.println("    4. Search for production");
        System.out.println("    5. Add favorite");
        System.out.println("    6. Remove favorite");
        System.out.println("    7. Add production or actor");
        System.out.println("    8. Remove production or actor");
        System.out.println("    9. Issue a request");
        System.out.println("    10. Retract a request");
        System.out.println("    11. Solve my requests");
        System.out.println("    12. Update production or actor");
        System.out.println("    13. View my personal details");
        System.out.println("    14. View notifications");
        System.out.println("    15. Delete notification");
        System.out.println("    16. Logout");
        try {
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    viewProductionsDetails(scanner);
                    break;
                case 2:
                    viewActorsDetails(scanner);
                    break;
                case 3:
                    searchActor(scanner);
                    break;
                case 4:
                    searchProduction(scanner);
                    break;
                case 5:
                    addFavorite(scanner);
                    break;
                case 6:
                    removeFavorite(scanner);
                    break;
                case 7:
                    addProductionOrActor(scanner);
                    break;
                case 8:
                    removeProductionOrActor(scanner);
                    break;
                case 9:
                    issueRequest(scanner);
                    break;
                case 10:
                    retractRequest(scanner);
                    break;
                case 11:
                    solveMyRequests(scanner);
                    break;
                case 12:
                    updateProductionOrActors(scanner);
                    break;
                case 13:
                    viewPersonalDetails();
                    break;
                case 14:
                    viewNotifications();
                    break;
                case 15:
                    deleteNotification();
                    break;
                case 16:
                    loggoff();
                    break;
                default:
                    System.err.println("Invalid option");
                    break;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
        }
    }
    private void showAdminOptions(Scanner scanner) {
        int option;
        System.out.println("\nChoose action:");
        System.out.println("    1. View productions details");
        System.out.println("    2. View actors details");
        System.out.println("    3. Search for actor");
        System.out.println("    4. Search for production");
        System.out.println("    5. Add favorite");
        System.out.println("    6. Remove favorite");
        System.out.println("    7. Add production or actor");
        System.out.println("    8. Remove production or actor");
        System.out.println("    9. Solve my requests");
        System.out.println("    10. Update production or actor");
        System.out.println("    11. Add or update user");
        System.out.println("    12. Remove user");
        System.out.println("    13. View my personal details");
        System.out.println("    14. View notifications");
        System.out.println("    15. Delete notification");
        System.out.println("    16. Logout");
        try {
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    viewProductionsDetails(scanner);
                    break;
                case 2:
                    viewActorsDetails(scanner);
                    break;
                case 3:
                    searchActor(scanner);
                    break;
                case 4:
                    searchProduction(scanner);
                    break;
                case 5:
                    addFavorite(scanner);
                    break;
                case 6:
                    removeFavorite(scanner);
                    break;
                case 7:
                    addProductionOrActor(scanner);
                    break;
                case 8:
                    removeProductionOrActor(scanner);
                    break;
                case 9:
                    solveMyRequests(scanner);
                    break;
                case 10:
                    updateProductionOrActors(scanner);
                    break;
                case 11:
                    addOrUpdateUser(scanner);
                    break;
                case 12:
                    removeUser(scanner);
                    break;
                case 13:
                    viewPersonalDetails();
                    break;
                case 14:
                    viewNotifications();
                    break;
                case 15:
                    deleteNotification();
                    break;
                case 16:
                    loggoff();
                    break;
                default:
                    System.err.println("Invalid option");
                    break;
            }
        } catch (Exception e) {
            System.err.println("Invalid input:" + e.getMessage());
        }
    }
    private void viewProductionsDetails(Scanner scanner) {
        System.out.println("Chose how you want to view productions by typing the number option:");
        System.out.println("1.All productions");
        System.out.println("2.Filter by genre");
        System.out.println("3.Filter by number of ratings");
        int option = scanner.nextInt();
        scanner.nextLine();
        if (option == 1) {
            for (Production production : imdb.getProductions()) {
                production.displayInfo();
            }
        } else if (option == 2) {
            System.out.println("Choose a genre:");
            for (Genre genre : Genre.values()) {
                System.out.println("*) " + genre.toString());
            }
            String genre_string = scanner.nextLine().toUpperCase();
            Genre picked_genre;
            try {
                picked_genre = Genre.valueOf(genre_string);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid genre");
                return;
            }
            for (Production production : imdb.getProductions()) {
                if (production.getGenres().contains(picked_genre)) {
                    production.displayInfo();
                }
            }
        } else if (option == 3) {
            System.out.println("Introduce minimum number of ratings:");
            int min_ratings = scanner.nextInt();
            scanner.nextLine();
            for (Production production : imdb.getProductions()) {
                if (production.getRatings().size() >= min_ratings) {
                    production.displayInfo();
                }
            }
        } else {
            System.err.println("Invalid option");
        }
    }
    private void viewActorsDetails(Scanner scanner) {
        System.out.println("Type SORT to sort actors by name or anything else to let them as they are:");
        String option = scanner.nextLine();
        if (option.equalsIgnoreCase("SORT")) {
            Collections.sort(imdb.getActors());
        }
        for (Actor actor : imdb.getActors()) {
            actor.displayInfo();
        }
    }
    private void searchActor(Scanner scanner) {
        System.out.println("Introduce actor name: ");
        String actor_name = scanner.nextLine();
        Actor actor = Actor.getActorByName(imdb.getActors(), actor_name);
        if (actor == null) {
            System.err.println("Actor not found");
            //Show closest matches
            List<ComparableItem> comparableActors = new ArrayList<>();
            comparableActors.addAll(imdb.getActors());
            System.out.println("Closest matches:");
            System.out.print(UtilFunction.getBestMatchesString(actor_name, comparableActors));
        } else {
            actor.displayInfo();
        }
    }
    private void searchProduction(Scanner scanner) {
        System.out.println("Introduce production name: ");
        String production_name = scanner.nextLine();
        Production production = Production.getProductionByTitle(imdb.getProductions(), production_name);
        if (production == null) {
            System.err.println("Production not found");
            //Show closest matches
            List<ComparableItem> comparableProductions = new ArrayList<>();
            comparableProductions.addAll(imdb.getProductions());
            System.out.println("Closest matches:");
            System.out.print(UtilFunction.getBestMatchesString(production_name, comparableProductions));
        } else {
            production.displayInfo();
        }
    }
    private void addFavorite(Scanner scanner) {
        System.out.println("Introduce  favourite: ");
        String favourite_name = scanner.nextLine();
        imdb.getLoggedUser().addFavoriteByName(favourite_name);
    }
    private void removeFavorite(Scanner scanner) {
        System.out.println("Introduce  favourite: ");
        String favourite_name = scanner.nextLine();
        imdb.getLoggedUser().removeFavoriteByName(favourite_name);
    }
    public void addReview(Scanner scanner) {
        System.out.println("Introduce production name: ");
        String production_name = scanner.nextLine();
        Production production = Production.getProductionByTitle(imdb.getProductions(), production_name);
        if (production == null) {
            System.err.println("Production not found");
            return;
        }
        System.out.println("Introduce review grade: ");
        int grade = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Introduce review description: ");
        String description = scanner.nextLine();
        production.addOrUpdateRating(new Rating(imdb.getLoggedUser().getUsername(), grade, description));
    }
    public void removeReview(Scanner scanner) {
        System.out.println("Introduce production name: ");
        String production_name = scanner.nextLine();
        Production production = Production.getProductionByTitle(imdb.getProductions(), production_name);
        if (production == null) {
            System.err.println("Production not found");
            return;
        }
        production.removeRating(imdb.getLoggedUser().getUsername());
    }

    private void viewNotifications() {
        for (String notification : imdb.getLoggedUser().getNotifications()) {
            System.out.println(notification);
        }
    }
    private void issueRequest(Scanner scanner) {
        Request aux_request;
        System.out.println("Choose request type by typing its name:");
        System.out.println("*) DELETE_ACCCONT");
        System.out.println("*) ACTOR_ISSUE");
        System.out.println("*) MOVIE_ISSUE");
        System.out.println("*) OTHERS");
        String req_type_string = scanner.nextLine().toUpperCase();
        RequestTypes req_type;
        try {
            req_type = RequestTypes.valueOf(req_type_string);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid request type");
            return;
        }
        System.out.println("Introduce description for request:");
        String description = scanner.nextLine();
        Staff<ComparableItem> req_target;
        String subject = null;
        if (req_type == RequestTypes.ACTOR_ISSUE) {
            System.out.println("Introduce actor for request:");
            subject = scanner.nextLine();
            Actor actor = Actor.getActorByName(imdb.getActors(), subject);
            if (actor == null) {
                throw new IllegalArgumentException("Actor not found");
            }
            req_target = actor.getContributor();
        } else if (req_type == RequestTypes.MOVIE_ISSUE) {
            System.out.println("Introduce production for request:");
            subject = scanner.nextLine();
            Production production = Production.getProductionByTitle(imdb.getProductions(), subject);
            if (production == null) {
                throw new IllegalArgumentException("Production not found");
            }
            req_target = production.getContributor();
        } else {
            req_target = imdb.getCommonAdmin();
        }
        if (req_target == null) {
            throw new IllegalArgumentException("Contributor not found");
        }
        aux_request = new Request(req_type, req_target, subject, imdb.getLoggedUser(), description);
        aux_request.setRequestTimeNow();
        System.out.println("Request issued: " + aux_request);
        ((RequestManager)imdb.getLoggedUser()).createRequest(aux_request);
        //System.out.println("Request issued: " + aux_request.toString());
    }
    private void retractRequest(Scanner scanner) {
        System.out.println("Here are your requests:");
        List<Request> requests = imdb.getLoggedUser().getIssuedRequests();
        if (requests.size() == 0) {
            System.out.println("You have no requests");
            return;
        } else {
            for (Request request : requests) {
                System.out.println(request);
            }
        }
        System.out.println("\nIntroduce request id to retract:");
        int request_id = scanner.nextInt();
        scanner.nextLine();
        Request request = Request.getRequestById(request_id);
        if (request == null) {
            System.err.println("Request not found");
            return;
        }
        ((RequestManager)imdb.getLoggedUser()).removeRequest(request);
    }
    private void viewPersonalDetails() {
        imdb.getLoggedUser().displayInfo();
    }
    private void loggoff() {
        imdb.setLoggedUser(null);
    }
    private void addProductionOrActor(Scanner scanner) {
        System.out.println("Introduce 1 for production or 2 for actor: ");
        int option = scanner.nextInt();
        scanner.nextLine();
        if (option == 1) {
            System.out.println("Introduce production name: ");
            String title = scanner.nextLine();
            System.out.println("Introduce production type: (MOVIE/SERIES)");
            String production_type = scanner.nextLine();
            Production new_production = productionVerbose(scanner, title, production_type);
            ((Staff<ComparableItem>)imdb.getLoggedUser()).addProductionSystem(new_production);
            System.out.println("Production added:");
            new_production.displayInfo();
        } else if (option == 2) {
            Actor new_actor = actorVerbose(scanner);
            ((Staff<ComparableItem>)imdb.getLoggedUser()).addActorSystem(new_actor);
            System.out.println("Actor added:");
            new_actor.displayInfo();
        } else {
            System.err.println("Invalid option");
        }
    }
    private void removeProductionOrActor(Scanner scanner) {
        System.out.println("Introduce 1 for production or 2 for actor: ");
        int option = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Introduce name: ");
        String name = scanner.nextLine();
        if (option == 1) {
            ((Staff<ComparableItem>)imdb.getLoggedUser()).removeProductionSystem(name);
        } else if (option == 2) {
            ((Staff<ComparableItem>)imdb.getLoggedUser()).removeActorSystem(name);
        } else {
            System.err.println("Invalid option");
        }
    }
    private void solveMyRequests(Scanner scanner) {
        Staff<ComparableItem> staff_member = (Staff<ComparableItem>)imdb.getLoggedUser();
        System.out.println("Here are your requests:");
        System.out.println(staff_member.requestsToString());
        System.out.println("HAVE YOU SOLVED ANY REQUESTS? (Y/N)");
        String option = scanner.nextLine();
        if (option.equalsIgnoreCase("Y")) {
            System.out.println("Introduce requests id-es you have solved or rejected (separated by space):");
            String requests_string = scanner.nextLine();
            List<String> requests_string_list = UtilFunction.turnSingleStringToList(requests_string);
            for (String request_string : requests_string_list) {
                try {
                    int solved_request_id = Integer.parseInt(request_string);
                    Request solved_request = Request.getRequestById(solved_request_id);
                    if (solved_request == null) {
                        System.err.println("Request with id: " + solved_request_id + " not found");
                    } else {
                        System.out.println("Do you accept the request with id " + solved_request_id + "? (Y/N)");
                        String accept_string = scanner.nextLine();
                        boolean accept;
                        if (accept_string.equalsIgnoreCase("Y")) {
                            accept = true;
                        } else if (accept_string.equalsIgnoreCase("N")) {
                            accept = false;
                        } else {
                            System.err.println("Invalid option");
                            continue;
                        }
                        ((StaffInterface)imdb.getLoggedUser()).solveRequest(solved_request, accept);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid request id for request " + request_string);
                }
            }
        }
    }
    private void updateProductionOrActors(Scanner scanner) {
        System.out.println("Your contributions:\n" + ((Staff<ComparableItem>)imdb.getLoggedUser()).contributionsToString());
        System.out.println("Introduce 1 for production or 2 for actor: ");
        int option = scanner.nextInt();
        scanner.nextLine();
        if (option == 1) {
            System.out.println("Introduce production to be changed name: ");
            String old_production_name = scanner.nextLine();
            Production old_production = Production.getProductionByTitle(imdb.getProductions(), old_production_name);
            if (old_production == null) {
                System.err.println("Production not found");
                return;
            }
            System.out.println("Introduce new data ...");
            Production new_production = productionVerbose(scanner, old_production_name, old_production.getClass().getSimpleName());
            ((Staff<ComparableItem>)imdb.getLoggedUser()).updateProduction(old_production, new_production);
            System.out.println("Production updated:");
            new_production.displayInfo();
        } else if (option == 2) {
            System.out.println("Introduce actor to be changed name: ");
            String old_actor_name = scanner.nextLine();
            Actor old_actor = Actor.getActorByName(imdb.getActors(), old_actor_name);
            if (old_actor == null) {
                System.err.println("Actor not found");
                return;
            }
            System.out.println("Introduce new data...");
            Actor new_actor = actorVerbose(scanner);
            ((Staff<ComparableItem>)imdb.getLoggedUser()).updateActor(old_actor, new_actor);
            System.out.println("Actor updated:");
            new_actor.displayInfo();
        } else {
            System.err.println("Invalid option");
        }
    }
    private void removeUser(Scanner scanner) {
        System.out.println("Introduce username: ");
        String username = scanner.nextLine();
        User<ComparableItem> user = User.getUserByUsername(username, imdb.getUsers());
        if (user == null) {
            System.err.println("User not found");
            return;
        }
        ((Admin<ComparableItem>)imdb.getLoggedUser()).removeUserSystem(user);
    }
    private void addOrUpdateUser(Scanner scanner) {
        System.out.println("Introduce 1 for add or 2 for update: ");
        int option = scanner.nextInt();
        scanner.nextLine();
        if (option == 1) {
            User<ComparableItem> new_user = userVerboseRegular(scanner);
            ((Admin<ComparableItem>)imdb.getLoggedUser()).addUserSystem(new_user);
            System.out.println("User added:");
            new_user.displayInfo();
        } else if (option == 2) {
            System.out.println("Introduce username: ");
            String username = scanner.nextLine();
            User<ComparableItem> old_user = User.getUserByUsername(username, imdb.getUsers());
            if (old_user == null) {
                System.err.println("User not found");
                return;
            }
            updateUserVerboseRegular(old_user, scanner);
            System.out.println("User updated:");
            old_user.displayInfo();
        } else {
            System.err.println("Invalid option");
        }
    }
    private void deleteNotification() {
        imdb.getLoggedUser().removeAllNotifications();
    }
    private void seasonVerbose(Scanner scanner, Series series) {
        System.out.println("Introduce season name: ");
        String season_name = scanner.nextLine();
        System.out.println("Introduce number of episodes: ");
        int number_of_episodes = scanner.nextInt();
        scanner.nextLine();
        List<Episode> episodes = new ArrayList<>();
        for (int i = 1; i <= number_of_episodes; i++) {
            System.out.println("Introduce episode " + i + " name: ");
            String episode_name = scanner.nextLine();
            System.out.println("Introduce episode " + i + " duration: ");
            String episode_duration = scanner.nextLine();
            episodes.add(new Episode(episode_name, episode_duration));
        }
        series.addEpisodesForSeason(season_name, episodes);
    }
    private Production productionVerbose(Scanner scanner, String title, String production_type) {
        Production new_production;
        System.out.println("Introduce production genres: (separated by spaces)");
        System.out.println("    Possible genres:");
        for (Genre genre : Genre.values()) {
            System.out.println("*) " + genre.toString());
        }
        String genres_string = scanner.nextLine();
        List<String> genres_string_list = UtilFunction.turnSingleStringToList(genres_string);
        List<Genre> genres = new ArrayList<>();
        for (String genre_string : genres_string_list) {
            try {
                genres.add(Genre.valueOf(genre_string.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid genre");
            }
        }
        System.out.println("Introduce actors: (separated by spaces)");
        String actors_string = scanner.nextLine();
        List<String> actors = UtilFunction.turnSingleStringToList(actors_string);
        System.out.println("Introduce directors: (separated by spaces)");
        String directors_string = scanner.nextLine();
        List<String> directors = UtilFunction.turnSingleStringToList(directors_string);
        System.out.println("Introduce subject description: ");
        String subject_description = scanner.nextLine();
        if (production_type.equalsIgnoreCase("MOVIE")) {
            System.out.println("Introduce duration: ");
            String duration = scanner.nextLine();
            System.out.println("Introduce year of release: ");
            int year_of_release = scanner.nextInt();
            scanner.nextLine();
            new_production = new Movie(title, directors, actors, genres, null,
                    subject_description, 0.0, duration, year_of_release);
        } else if (production_type.equalsIgnoreCase("SERIES")){
            System.out.println("Introduce year of release: ");
            int year_of_release = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Introduce number of seasons: ");
            int number_of_seasons = scanner.nextInt();
            scanner.nextLine();
            new_production = new Series(title, directors, actors, genres, null,
                    subject_description, 0.0, year_of_release, number_of_seasons);
            for (int i = 0; i < number_of_seasons; i++) {
                seasonVerbose(scanner, (Series)new_production);
            }
        } else {
            throw new IllegalArgumentException("Invalid production type");
        }
        return new_production;
    }
    private Actor actorVerbose(Scanner scanner) {
        Actor new_actor;
        System.out.println("Introduce new actor name: ");
        String name = scanner.nextLine();
        System.out.println("Introduce number of productions in filmography: ");
        int number_of_productions = scanner.nextInt();
        scanner.nextLine();
        List<Pair<String, String>> filmography = new ArrayList<>();
        for (int i = 1; i <= number_of_productions; i++) {
            System.out.println("Introduce production " + i + " name: ");
            String production_name = scanner.nextLine();
            System.out.println("Introduce production " + i + " type: (MOVIE/SERIES)");
            String production_type = scanner.nextLine();
            if (!production_type.equalsIgnoreCase("MOVIE") && !production_type.equalsIgnoreCase("SERIES")) {
                System.err.println("Invalid production type");
                throw new IllegalArgumentException("Invalid production type");
            }
            filmography.add(new Pair<>(production_type, production_name));
        }
        System.out.println("Introduce biography: ");
        String biography = scanner.nextLine();
        new_actor = new Actor(name, filmography, biography);
        return new_actor;
    }

    private User<ComparableItem> userVerboseRegular (Scanner scanner) {
        System.out.println("Introduce username: ");
        String username = scanner.nextLine();
        User.Information aux_info = informationVerbose(scanner);
        UserFactory user_factory = new UserFactory();
        return user_factory.addUser(aux_info, AccountType.REGULAR, username, 0,
                                    new LinkedList<>(), new TreeSet<>(), null);
    }
    private User.Information informationVerbose(Scanner scanner) {
        System.out.println("Introduce email: ");
        String email = scanner.nextLine();
        System.out.println("Introduce password : (Suggestion: " + Credentials.suggestStrongPassword() + ")");
        String password = scanner.nextLine();
        System.out.println("Introduce name: ");
        String name = scanner.nextLine();
        System.out.println("Introduce country: ");
        String country = scanner.nextLine();
        System.out.println("Introduce age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Introduce gender: (MALE/FEMALE/OTHER)");
        String gender_string = scanner.nextLine();
        Gender gender = Gender.valueOf(gender_string.toUpperCase());
        System.out.println("Introduce birth date: (yyyy-mm-dd)");
        String birth_date_string = scanner.nextLine();
        LocalDate birth_date = UtilFunction.getDateFromString(birth_date_string);
        return new User.InformationBuilder()
                .credentials(new Credentials(email, password))
                .name(name)
                .country(country)
                .age(age)
                .gender(gender)
                .birthDate(birth_date)
                .build();
    }

    private void updateUserVerboseRegular (User<ComparableItem> old_user, Scanner scanner) {
        System.out.println("Pick what data you want to change: ");
        System.out.println("    1. Email");
        System.out.println("    2. Password");
        System.out.println("    3. Name");
        System.out.println("    4. Country");
        System.out.println("    5. Age");
        System.out.println("    6. Birth date");
        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option) {
            case 1:
                System.out.println("Introduce new email: ");
                String email = scanner.nextLine();
                old_user.getInfo().getCredentials().setEmail(email);
                break;
            case 2:
                System.out.println("Introduce new password: (Suggestion: " + Credentials.suggestStrongPassword() + ")");
                String password = scanner.nextLine();
                old_user.getInfo().getCredentials().setPassword(password);
                break;
            case 3:
                System.out.println("Introduce new name: ");
                String name = scanner.nextLine();
                old_user.getInfo().setName(name);
                break;
            case 4:
                System.out.println("Introduce new country: ");
                String country = scanner.nextLine();
                old_user.getInfo().setCountry(country);
                break;
            case 5:
                System.out.println("Introduce new age: ");
                int age = scanner.nextInt();
                scanner.nextLine();
                old_user.getInfo().setAge(age);
                break;
            case 6:
                System.out.println("Introduce new birth date: (yyyy-mm-dd)");
                String birth_date_string = scanner.nextLine();
                LocalDate birth_date = UtilFunction.getDateFromString(birth_date_string);
                old_user.getInfo().setBirthDate(birth_date);
                break;
            default:
                System.err.println("Invalid option");
                break;
        }
    }
}
