package usefulpack;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import enumpack.Genre;

public class UtilFunction {
    public static LocalDateTime getDateTimeFromString(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return LocalDateTime.parse(date, formatter);
    }
    public static LocalDate getDateFromString(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }
    public static String getOnlyTwoDecimals(double number) {
        return String.format("%.2f", number);
    }
    public static <T extends ComparableItem> List<String> getBestMatches(String name, List<T> entities) {
        List<String> strings_for_sorting = new LinkedList<>();
        List<String> best_matches = new LinkedList<>();
        for (ComparableItem entity : entities) {
            strings_for_sorting.add(entity.getStringForSorting());
        }
        for (String str : strings_for_sorting) {
            if (str.toLowerCase().contains(name.toLowerCase())) {
                best_matches.add(str);
            }
        }
        strings_for_sorting.clear();
        return best_matches;
    }
    public static <T extends ComparableItem> String getBestMatchesString(String name, List<T> entities) {
        List<String> best_matches = getBestMatches(name, entities);
        StringBuilder sb = new StringBuilder();

        for (String str : best_matches) {
            sb.append(str);
            sb.append("\n");
        }
        return sb.toString();
    }
    public static String turnGenreListToSingleString(List<Genre> genres) {
        StringBuilder sb = new StringBuilder();
        if (genres == null) {
            return "";
        }
        for (Genre genre : genres) {
            sb.append(genre.toString());
            sb.append(" ");
        }
        return sb.toString();
    }
    public static String turnListToSingleString (List<String> list) {
        StringBuilder sb = new StringBuilder();
        if (list == null) {
            return "";
        }
        for (String str : list) {
            sb.append(str);
            sb.append(" ");
        }
        return sb.toString();
    }
    public static List<String> turnSingleStringToList(String str) {
        List<String> list = new ArrayList<>();
        String[] aux = str.split("\\s+");

        Collections.addAll(list, aux);
        return list;
    }
    public static List<Genre> turnSingleStringToGenreList(String str) {
        List<Genre> list = new ArrayList<>();
        String[] aux = str.split("\\s+");

        for (String genre : aux) {
            list.add(Genre.valueOf(genre.toUpperCase()));
        }
        return list;
    }
    public static List<String> readLinesFromFile(String filename) {
        List<String> lines = new ArrayList<>();
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading from file: " + filename);
            e.printStackTrace();
        }
        return lines;
    }
}
