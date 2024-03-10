package userpack;

import enumpack.AccountType;
import usefulpack.ComparableItem;
import java.util.List;
import java.util.SortedSet;

public class UserFactory {
    public <T extends ComparableItem> User<T> addUser(User.Information info, AccountType account_type, String username, int experience,
                                                      List<String> notifications, SortedSet<T> favorites, SortedSet<T> contributions) {
        if (account_type == null) {
            return null;
        }
        switch (account_type) {
            case REGULAR:
                return new Regular<>(info, account_type, username, experience, notifications, favorites);
            case CONTRIBUTOR:
                return new Contributor<>(info, account_type, username, experience, notifications, favorites, contributions);
            case ADMIN:
                return new Admin<>(info, account_type, username, experience, notifications, favorites, contributions);
            default:
                return null;
        }
    }


}
