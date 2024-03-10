package userpack;
import appstructure.IMDB;
import enumpack.AccountType;
import enumpack.Gender;
import java.time.LocalDate;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeSet;
import observerpack.Observer;
import observerpack.Request;
import productionpack.Actor;
import productionpack.Production;
import usefulpack.ComparableItem;

public abstract class User<T extends ComparableItem> implements Observer {
    private Information info;
    private AccountType account_type;
    private String username;
    private int experience;
    private List<String> notifications;
    private SortedSet<T> favorites;
    private String imagePath = "images/default_user_image.png";
    public User(){}
    public User(Information info, AccountType account_type, String username, int experience,
                List<String> notifications, SortedSet<T> favorites) {
        this.info = info;
        this.account_type = account_type;
        this.username = username;
        this.experience = experience;
        this.notifications = notifications;
        this.favorites = favorites;
    }
    public void setAccountType(AccountType account_type) {
        this.account_type = account_type;
    }
    public Information getInfo() {
        return info;
    }
    public String getImagePath() {
        return imagePath;
    }
    public void setInfo(Information info) {
        this.info = info;
    }
    public AccountType getAccountType() {
        return account_type;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public int getExperience() {
        return experience;
    }
    public void updateExperience(int experience) {
        this.experience += experience;
    }
    public SortedSet<T> getFavorites() {
        return favorites;
    }
    public void addNotification (String notification) {
        if (notifications == null) {
            notifications = new LinkedList<>();
        }
        notifications.add(notification);
    }
    public void removeNotification(String notification) {
        notifications.remove(notification);
    }
    public void removeAllNotifications() {
        notifications.clear();
    }
    public List<String> getNotifications() {
        return notifications;
    }
    public void addFavoriteByName(String favorite_name) {
        IMDB imdb = IMDB.getInstance();
        if (favorites == null) {
            favorites = new TreeSet<>();
        }
        for (T favorite : favorites) {
            if (favorite.getStringForSorting().equals(favorite_name)) {
                throw new IllegalArgumentException("Already in favorites");
            }
        }
        Production fav_prod = Production.getProductionByTitle(imdb.getProductions(), favorite_name);
        Actor fav_actor = Actor.getActorByName(imdb.getActors(), favorite_name);
        if (fav_prod != null) {
            favorites.add((T) fav_prod);
        } else if (fav_actor != null) {
            favorites.add((T) fav_actor);
        } else {
            throw new IllegalArgumentException("Production or actor not found");
        }
    }
    public void removeFavoriteByName(String favorite_name) {
        for (T favorite : favorites) {
            if (favorite.getStringForSorting().equals(favorite_name)) {
                favorites.remove(favorite);
                return;
            }
        }
        throw new NoSuchElementException("Not in favorites");
    }
    public List<Request> getIssuedRequests() {
        List<Request> issued_requests = new LinkedList<>();
        if (account_type == AccountType.ADMIN) {
            return issued_requests;
        } else {
            for (Request request : Request.RequestHolder.getRequests()) {
                if (request.getRequester().getUsername().equals(username)) {
                    issued_requests.add(request);
                }
            }
            for (Request request : IMDB.getInstance().getRequests()) {
                if (request.getRequester().getUsername().equals(username)) {
                    issued_requests.add(request);
                }
            }
        }
        return issued_requests;
    }
    public static class Information {
        private Credentials credentials;
        private String name;
        private String country;
        private int age;
        private Gender gender;
        private LocalDate birth_date;
        private Information(InformationBuilder information_builder) {
            this.credentials = information_builder.credentials;
            this.name = information_builder.name;
            this.country = information_builder.country;
            this.age = information_builder.age;
            this.gender = information_builder.gender;
            this.birth_date = information_builder.birth_date;
        }
        public Credentials getCredentials() {
            return credentials;
        }
        public void setCredentials(Credentials credentials) {
            this.credentials = credentials;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getCountry() {
            return country;
        }
        public void setCountry(String country) {
            this.country = country;
        }
        public int getAge() {
            return age;
        }
        public void setAge(int age) {
            this.age = age;
        }
        public Gender getGender() {
            return gender;
        }
        public void setGender(Gender gender) {
            this.gender = gender;
        }
        public LocalDate getBirthDate() {
            return birth_date;
        }
        public void setBirthDate(LocalDate birth_date) {
            this.birth_date = birth_date;
        }
        public String toString () {
            return "email: " + credentials.getEmail() + "\n" +
                    "name: " + name + "\n" +
                    "country: " + country + "\n" +
                    "age: " + age + "\n" +
                    "gender: " + gender + "\n" +
                    "birth date: " + birth_date;
        }
    }
    public static class InformationBuilder {
        private Credentials credentials;
        private String name;
        private String country;
        private int age;
        private Gender gender;
        private LocalDate birth_date;
        public InformationBuilder credentials(Credentials credentials) {
            this.credentials = credentials;
            return this;
        }
        public InformationBuilder name(String name) {
            this.name = name;
            return this;
        }
        public InformationBuilder country(String country) {
            this.country = country;
            return this;
        }
        public InformationBuilder age(int age) {
            this.age = age;
            return this;
        }
        public InformationBuilder gender(Gender gender) {
            this.gender = gender;
            return this;
        }
        public InformationBuilder birthDate(LocalDate birth_date) {
            this.birth_date = birth_date;
            return this;
        }
        public Information build() {
            return new Information(this);
        }
    }
    public static <T extends ComparableItem> User<T> getUserByCredentials(String email, String password, List<User<T>> users) {
        for (User<T> user : users) {
            if (user.info.credentials.getEmail().equals(email) && user.info.credentials.checkPassword(password)) {
                return user;
            }
        }
        return null;
    }
    public static <T extends ComparableItem> User<T> getUserByUsername(String username, List<User<T>> users) {
        if (username.equals("ADMIN")) {
            return (User<T>) IMDB.getInstance().getCommonAdmin();
        }
        for (User<T> user : users) {
            if (user.username.equals(username)) {
                return user;
            }
        }
        return null;
    }
    public String toString() {
        StringBuilder aux_string = new StringBuilder();
        aux_string.append("Username: ").append(username).append("\n");
        aux_string.append(info.toString()).append("\n");
        if (experience != -1) {
            aux_string.append("Experience: ").append(experience).append("\n");
        } else {
            aux_string.append("Experience: INFINITY\n");
        }
        aux_string.append("Account Type: ").append(account_type).append("\n");
        aux_string.append("Favourites:\n");
        for (T favorite : favorites) {
            aux_string.append(favorite.getStringForSorting()).append("\n");
        }
        aux_string.append("Notifications:\n");
        for (String notification : notifications) {
            aux_string.append(notification).append("\n");
        }
        return aux_string.toString();
    }
    public abstract void displayInfo();
    public void updateFieldsFrom(User<T> user) {
        if (user.info != null) {
            if (user.info.credentials != null) {
                this.info.credentials = user.info.credentials;
            }
            if (user.info.name != null) {
                this.info.name = user.info.name;
            }
            if (user.info.country != null) {
                this.info.country = user.info.country;
            }
            if (user.info.age != 0) {
                this.info.age = user.info.age;
            }
            if (user.info.gender != null) {
                this.info.gender = user.info.gender;
            }
            if (user.info.birth_date != null) {
                this.info.birth_date = user.info.birth_date;
            }
        }
        if (user.account_type != null) {
            this.account_type = user.account_type;
        }
        if (user.username != null) {
            this.username = user.username;
        }
        /*
        if (user.experience != -1) {
            this.experience = user.experience;
        }
        if (user.notifications != null) {
            this.notifications = user.notifications;
        }
        if (user.favorites != null) {
            this.favorites = user.favorites;
        }
        */
    }

}
