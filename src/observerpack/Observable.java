package observerpack;

import productionpack.Production;
import productionpack.Rating;
import usefulpack.ComparableItem;
import userpack.User;

public interface Observable {
    void registerObserver(User<ComparableItem> observer);
    void removeObserver(User<ComparableItem> observer);
    void notifyObserversAboutRating(Production production, Rating rating);
    void notifyObserversAboutRequest(Request request, Boolean accept);
}
