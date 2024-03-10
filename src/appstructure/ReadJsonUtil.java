package appstructure;
import enumpack.AccountType;
import enumpack.Gender;
import enumpack.Genre;
import enumpack.RequestTypes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.SortedSet;
import observerpack.Request;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.List;
import productionpack.*;
import usefulpack.*;
import java.time.LocalDateTime;
import userpack.*;

public class ReadJsonUtil {
    private ReadJsonUtil() {
    }
    private static String getJsonTextFromFile (String filename) {
        StringBuilder json_string_builder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
            String aux_line;
            while ((aux_line = bufferedReader.readLine()) != null) {
                json_string_builder.append(aux_line);
                json_string_builder.append("\n");
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.err.println("Error reading the file: " + filename);
            return null;
        }
        json_string_builder.deleteCharAt(json_string_builder.length() - 1);
        return json_string_builder.toString();
    }
    private static JSONArray getJsonArrayFromFile (String filename) {
        String json_text = getJsonTextFromFile(filename);
        JSONArray json_array;
        if (json_text == null) {
            return null;
        }
        JSONParser json_parser = new JSONParser();
        try {
            json_array = (JSONArray) json_parser.parse(json_text);
        } catch (org.json.simple.parser.ParseException e) {
            System.err.println("Error parsing the file: " + filename);
            return null;
        }
        return json_array;
    }
    public static List<Actor> getActorsFromJsonFile (String filename) {
        List<Actor> actors_list = new ArrayList<>();
        try {
            JSONArray actors_array = getJsonArrayFromFile(filename);
            if (actors_array == null) {
                return null;
            }
            for (Object actor : actors_array) {
                JSONObject actor_obj = (JSONObject) actor;
                String actor_name = (String) actor_obj.get("name");
                JSONArray filmography_array = (JSONArray) actor_obj.get("performances");
                List<Pair<String, String>> aux_filmography = new ArrayList<>();
                for (Object performance : filmography_array) {
                    JSONObject performance_obj = (JSONObject) performance;
                    String performance_title = (String) performance_obj.get("title");
                    String performance_type = (String) performance_obj.get("type");
                    aux_filmography.add(new Pair<>(performance_type, performance_title));
                }
                String actor_biography = (String) actor_obj.get("biography");
                actors_list.add(new Actor(actor_name, aux_filmography, actor_biography));
            }
        } catch (Exception e) {
            System.err.println("Error somwhere when reading from file: " + filename);
            return null;
        }
        return actors_list;
    }

    public static List<Production> getProductionsFromJsonFile (String filename, List<Actor> actors_list) {
        List<Production> productions_list = new ArrayList<>();
        List<Actor> not_added_actors = new ArrayList<>();
        if (actors_list == null) {
            System.out.println("Read the actors first");
            return null;
        }
        try {
            JSONArray productions_array = getJsonArrayFromFile(filename);
            if (productions_array == null) {
                return null;
            }
            for (Object production : productions_array) {
                JSONObject production_obj = (JSONObject) production;
                String production_title = (String) production_obj.get("title");
                String production_type = (String) production_obj.get("type");
                JSONArray production_directors = (JSONArray) production_obj.get("directors");
                List<String> aux_directors = new ArrayList<>();
                for (Object director : production_directors) {
                    aux_directors.add((String) director);
                }
                JSONArray production_actors = (JSONArray) production_obj.get("actors");
                List<String> aux_actors = new ArrayList<>();
                for (Object actor : production_actors) {
                    if (Actor.getActorByName(actors_list, (String) actor) == null) {
                        Actor not_added_actor = Actor.getActorByName(not_added_actors, (String) actor);
                        if (not_added_actor == null) {
                            not_added_actor = new Actor((String) actor, null, null);
                            not_added_actors.add(not_added_actor);
                        }
                        not_added_actor.addFilmography(new Pair<>(production_type, production_title));
                    }
                    aux_actors.add((String) actor);
                }
                JSONArray production_genres = (JSONArray) production_obj.get("genres");
                List<Genre> aux_genres = new ArrayList<>();
                for (Object genre : production_genres) {
                    String genre_string = (String) genre;
                    aux_genres.add(Genre.valueOf(genre_string.toUpperCase()));
                }
                JSONArray production_ratings = (JSONArray) production_obj.get("ratings");
                List<Rating> aux_ratings = new ArrayList<>();
                for (Object rating : production_ratings) {
                    JSONObject rating_obj = (JSONObject) rating;
                    String rating_username = (String) rating_obj.get("username");
                    long rating_grade = (long) rating_obj.get("rating");
                    String rating_comment = (String) rating_obj.get("comment");
                    aux_ratings.add(new Rating(rating_username, (int)rating_grade, rating_comment));
                }
                String subject_description = (String) production_obj.get("plot");
                double avg_production_rating = (double) production_obj.get("averageRating");
                if (production_type.compareToIgnoreCase("Movie") == 0) {
                    String movie_duration = (String) production_obj.get("duration");
                    Object year_of_release_obj = production_obj.get("releaseYear");
                    int year_of_release;
                    if (year_of_release_obj instanceof Long) {
                        year_of_release = ((Long) year_of_release_obj).intValue();
                    } else {
                        year_of_release = 0;
                    }
                    productions_list.add(new Movie(production_title, aux_directors, aux_actors, aux_genres, aux_ratings,
                            subject_description, avg_production_rating, movie_duration, year_of_release));
                } else if (production_type.compareToIgnoreCase("Series") == 0) {
                    long year_of_release = (long) production_obj.get("releaseYear");
                    long number_of_seasons = (long) production_obj.get("numSeasons");
                    Series aux_series = new Series(production_title, aux_directors, aux_actors, aux_genres, aux_ratings,
                            subject_description, avg_production_rating, (int)year_of_release, (int)number_of_seasons);
                    JSONObject series_seasons = (JSONObject) production_obj.get("seasons");
                    for (Object season_key : series_seasons.keySet()) {
                        String season_name = (String) season_key;
                        JSONArray season_episodes = (JSONArray) series_seasons.get(season_name);
                        List<Episode> aux_episodes = new ArrayList<>();
                        for (Object episode : season_episodes) {
                            JSONObject episode_obj = (JSONObject) episode;
                            String episode_name = (String) episode_obj.get("episodeName");
                            String episode_duration = (String) episode_obj.get("duration");
                            aux_episodes.add(new Episode(episode_name, episode_duration));
                        }
                        aux_series.addEpisodesForSeason(season_name, aux_episodes);
                    }
                    productions_list.add(aux_series);
                } else {
                    throw new IllegalArgumentException("Invalid production type");
                }
            }
            //add the not added actors to the actors list
            actors_list.addAll(not_added_actors);
            Admin<ComparableItem> common_admin = IMDB.getInstance().getCommonAdmin();
            for (Actor actor : not_added_actors) {
                common_admin.addContribution(actor);
                actor.setContributor(common_admin);
            }
        } catch (Exception e) {
            System.err.println("Error somwhere when reading from file: " + filename);
            return null;
        }
        return productions_list;
    }

    public static void getRequestsFromJsonFile (String filename, List<User<ComparableItem>> users) {
        try {
            JSONArray requests_array = getJsonArrayFromFile(filename);
            if (requests_array == null) {
                return;
            }
            for (Object request : requests_array) {
                String request_subject;
                JSONObject request_obj = (JSONObject) request;
                RequestTypes request_type = RequestTypes.valueOf((String) request_obj.get("type"));
                String creation_date = (String) request_obj.get("createdDate");
                LocalDateTime timestamp = UtilFunction.getDateTimeFromString(creation_date);
                String requester_username = (String) request_obj.get("username");
                request_subject = (String) request_obj.get("actorName");
                if (request_subject == null) {
                    request_subject = (String) request_obj.get("movieTitle");
                }
                String request_target_string = (String) request_obj.get("to");
                String description = (String) request_obj.get("description");
                User<ComparableItem> request_target = User.getUserByUsername(request_target_string, users);
                User<ComparableItem> requester = User.getUserByUsername(requester_username, users);
                if (request_target == null || requester == null) {
                    throw new IllegalArgumentException("Invalid request");
                }
                if (request_target.getAccountType().equals(AccountType.REGULAR)) {
                    throw new IllegalArgumentException("Invalid request");
                }
                Request aux_request = new Request(request_type, (Staff<ComparableItem>)request_target,
                                                  request_subject, requester, description, timestamp);
                ((RequestManager)requester).createRequest(aux_request);
            }
        } catch (Exception e) {
            System.err.println("Error somwhere when reading from file: " + filename);
        }
    }

    public static List<User<ComparableItem>> getUsersFromJsonFile (String filename, List<Actor> actors_list,
                                                                   List<Production> productions_list) {
        List<User<ComparableItem>> users_list = new ArrayList<>();
        UserFactory user_factory = new UserFactory();
        try {
            JSONArray users_array = getJsonArrayFromFile(filename);
            if (users_array == null) {
                return null;
            }
            for (Object user : users_array) {
                JSONObject user_obj = (JSONObject) user;
                String user_username = (String) user_obj.get("username");
                String experience_string = (String) user_obj.get("experience");
                if (experience_string == null) {
                    experience_string = "-1";
                }
                String type_string = (String) user_obj.get("userType");
                AccountType account_type = AccountType.valueOf(type_string.toUpperCase());
                JSONObject user_info_obj = (JSONObject) user_obj.get("information");
                JSONObject credentials_obj = (JSONObject) user_info_obj.get("credentials");
                String credentials_email = (String) credentials_obj.get("email");
                String credentials_password = (String) credentials_obj.get("password");
                Credentials aux_credentials = new Credentials(credentials_email, credentials_password);
                long info_age = (long) user_info_obj.get("age");
                String gender_string = (String) user_info_obj.get("gender");
                String birth_date_string = (String) user_info_obj.get("birthDate");
                User.Information aux_info = new User.InformationBuilder()
                        .credentials(aux_credentials)
                        .name((String) user_info_obj.get("name"))
                        .country((String) user_info_obj.get("country"))
                        .age((int)info_age)
                        .gender(Gender.valueOf(gender_string.toUpperCase()))
                        .birthDate(UtilFunction.getDateFromString(birth_date_string))
                        .build();
                SortedSet<ComparableItem> aux_favorites = new TreeSet<>();
                JSONArray fav_prod_array = (JSONArray) user_obj.get("favoriteProductions");
                if (fav_prod_array != null) {
                    for (Object fav_prod : fav_prod_array) {
                        String fav_prod_title = (String) fav_prod;
                        Production aux_production = Production.getProductionByTitle(productions_list, fav_prod_title);
                        if (aux_production != null) {
                            aux_favorites.add(aux_production);
                        }
                    }
                }
                JSONArray fav_actors_array = (JSONArray) user_obj.get("favoriteActors");
                if (fav_actors_array != null) {
                    for (Object fav_actor : fav_actors_array) {
                        String fav_actor_name = (String) fav_actor;
                        Actor aux_actor = Actor.getActorByName(actors_list, fav_actor_name);
                        if (aux_actor != null) {
                            aux_favorites.add(aux_actor);
                        }
                    }
                }
                //NU TI TREBUIE ASTA
                List<String> aux_notifications = new LinkedList<>();
                JSONArray notifications_array = (JSONArray) user_obj.get("notifications");
                if (notifications_array != null) {
                    for (Object notification : notifications_array) {
                        aux_notifications.add((String) notification);
                    }
                }
                SortedSet<ComparableItem> aux_contributions = new TreeSet<>();
                if (account_type != AccountType.REGULAR) {
                    JSONArray actors_contributions_array = (JSONArray) user_obj.get("actorsContribution");
                    if (actors_contributions_array != null) {
                        for (Object actor_contribution : actors_contributions_array) {
                            String actor_contribution_name = (String) actor_contribution;
                            Actor aux_actor = Actor.getActorByName(actors_list, actor_contribution_name);
                            if (aux_actor != null) {
                                aux_contributions.add(aux_actor);
                            }
                        }
                    }
                    JSONArray prod_contributions_array = (JSONArray) user_obj.get("productionsContribution");
                    if (prod_contributions_array != null) {
                        for (Object prod_contribution : prod_contributions_array) {
                            String prod_contribution_title = (String) prod_contribution;
                            Production aux_production = Production.getProductionByTitle(productions_list, prod_contribution_title);
                            if (aux_production != null) {
                                aux_contributions.add(aux_production);
                            }
                        }
                    }
                }
                User<ComparableItem> aux_user = user_factory.addUser(aux_info, account_type, user_username,
                        Integer.parseInt(experience_string), aux_notifications, aux_favorites, aux_contributions);
                users_list.add(aux_user);
            }
        } catch (Exception e) {
            System.err.println("Error somwhere when reading from file: " + filename);
            return null;
        }
        return users_list;
    }
}
