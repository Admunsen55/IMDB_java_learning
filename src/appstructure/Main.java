package appstructure;

public class Main {
    public static void main(String[] args) {
        IMDB imdb = IMDB.getInstance();
        imdb.prepareData();
        imdb.run();
    }
}
