package observerpack;

import productionpack.Production;
import productionpack.Rating;

public interface Observer {
    void updateAboutRating(Production production, Rating rating);
    void updateAboutRequest(Request request, Boolean accept);
}