package observerpack;

import enumpack.AccountType;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.*;
import productionpack.Production;
import productionpack.Rating;
import usefulpack.ComparableItem;
import userpack.User;

public class NotificationHub implements Observable {
    private static NotificationHub instance = null;
    private Map<NotificationEvent, LinkedList<User<ComparableItem>>> observers;
    private NotificationHub() {
        observers = new HashMap<>();
        for (NotificationEvent event : NotificationEvent.values()) {
            observers.put(event, new LinkedList<>());
        }
    }
    public static NotificationHub getInstance() {
        if (instance == null) {
            instance = new NotificationHub();
        }
        return instance;
    }
    @Override
    public void registerObserver(User<ComparableItem> observer) {
        if (observer.getAccountType() == AccountType.REGULAR) {
            observers.get(NotificationEvent.RATED_GOT_RATED).add(observer);
            observers.get(NotificationEvent.REQUEST_GOT_SOLVED).add(observer);
            observers.get(NotificationEvent.REQUEST_GOT_REJECTED).add(observer);
        } else if (observer.getAccountType() == AccountType.CONTRIBUTOR) {
            observers.get(NotificationEvent.ADDED_GOT_RATED).add(observer);
            observers.get(NotificationEvent.GOT_NEW_REQUEST).add(observer);
            observers.get(NotificationEvent.REQUEST_GOT_SOLVED).add(observer);
            observers.get(NotificationEvent.REQUEST_GOT_REJECTED).add(observer);
        } else if (observer.getAccountType() == AccountType.ADMIN) {
            observers.get(NotificationEvent.ADDED_GOT_RATED).add(observer);
            observers.get(NotificationEvent.GOT_NEW_REQUEST).add(observer);
        }
    }
    @Override
    public void removeObserver(User<ComparableItem> observer) {
        if (observer.getAccountType() == AccountType.REGULAR) {
            observers.get(NotificationEvent.RATED_GOT_RATED).remove(observer);
            observers.get(NotificationEvent.REQUEST_GOT_SOLVED).remove(observer);
            observers.get(NotificationEvent.REQUEST_GOT_REJECTED).remove(observer);
        } else if (observer.getAccountType() == AccountType.CONTRIBUTOR) {
            observers.get(NotificationEvent.ADDED_GOT_RATED).remove(observer);
            observers.get(NotificationEvent.GOT_NEW_REQUEST).remove(observer);
            observers.get(NotificationEvent.REQUEST_GOT_SOLVED).remove(observer);
            observers.get(NotificationEvent.REQUEST_GOT_REJECTED).remove(observer);
        } else if (observer.getAccountType() == AccountType.ADMIN) {
            observers.get(NotificationEvent.ADDED_GOT_RATED).remove(observer);
            observers.get(NotificationEvent.GOT_NEW_REQUEST).remove(observer);
        }
    }
    public void notifyObserversAboutRating(Production production, Rating rating) {
        for (User<ComparableItem> observer : observers.get(NotificationEvent.RATED_GOT_RATED)) {
            observer.updateAboutRating(production, rating);
        }
        for (User<ComparableItem> observer : observers.get(NotificationEvent.ADDED_GOT_RATED)) {
            observer.updateAboutRating(production, rating);
        }
    }

    public void notifyObserversAboutRequest(Request request, Boolean accept) {
        for (User<ComparableItem> observer : observers.get(NotificationEvent.REQUEST_GOT_SOLVED)) {
            observer.updateAboutRequest(request, accept);
        }
        for (User<ComparableItem> observer : observers.get(NotificationEvent.REQUEST_GOT_REJECTED)) {
            observer.updateAboutRequest(request, accept);
        }
        for (User<ComparableItem> observer : observers.get(NotificationEvent.GOT_NEW_REQUEST)) {
            observer.updateAboutRequest(request, accept);
        }
    }
}
