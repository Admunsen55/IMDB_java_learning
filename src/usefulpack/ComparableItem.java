package usefulpack;
import userpack.User;
import java.util.List;

public interface ComparableItem extends Comparable<ComparableItem> {
    String getStringForSorting();
}
