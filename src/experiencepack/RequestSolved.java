package experiencepack;

import enumpack.RequestTypes;
import observerpack.Request;

public class RequestSolved implements ExperienceStrategy{
    private Request request;
    public RequestSolved() {
    }
    public void setRequest(Request request) {
        this.request = request;
    }
    @Override
    public int calculateExperience() {
        if (request.getType() == RequestTypes.ACTOR_ISSUE) {
            return 1;
        }
        if (request.getType() == RequestTypes.MOVIE_ISSUE) {
            return 2;
        }
        return 0;
    }
}
