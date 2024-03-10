package experiencepack;

import productionpack.Actor;
import productionpack.Production;
import usefulpack.ComparableItem;

public class AddedNewProductionActor implements ExperienceStrategy{
    private ComparableItem item;
    public AddedNewProductionActor() {
    }
    public void setItem(ComparableItem item) {
        this.item = item;
    }
    @Override
    public int calculateExperience() {
        if (item instanceof Production) {
            return 10;
        }
        if (item instanceof Actor) {
            return 5;
        }
        return 0;
    }
}
