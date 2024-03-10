package userpack;

import enumpack.AccountType;
import usefulpack.ComparableItem;

public interface AdminInterface {
    void addUserSystem(User<ComparableItem> user);
    void removeUserSystem(User<ComparableItem> user);
    void updateUser(User<ComparableItem> old_user, User<ComparableItem> new_user);
    //void promoteUser(User user, AccountType new_account_type);
}
