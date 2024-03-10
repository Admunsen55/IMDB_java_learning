package experiencepack;

import enumpack.AccountType;
import observerpack.Request;
import usefulpack.ComparableItem;
import userpack.User;

public class ExperienceCalculator {
    private ExperienceStrategy strategy;
    private User<ComparableItem> user;
    public AddedFirstReview added_first_review;
    public AddedNewProductionActor added_new_production_actor;
    public RequestSolved request_solved;
    public ExperienceCalculator() {
        added_first_review = new AddedFirstReview();
        added_new_production_actor = new AddedNewProductionActor();
        request_solved = new RequestSolved();
    }
    public void setUser(User<ComparableItem> user) {
        this.user = user;
    }
    public void setStrategy(ExperienceStrategy strategy) {
        this.strategy = strategy;
    }
    public void setState(User<ComparableItem> user, ExperienceStrategy strategy) {
        this.user = user;
        this.strategy = strategy;
    }
    public void updateUserExperience() {
        if (user.getAccountType() == AccountType.ADMIN) {
            return;
        }
        if (strategy == null) {
            throw new IllegalStateException("Strategy not set!");
        }
        user.updateExperience(strategy.calculateExperience());
    }
}
