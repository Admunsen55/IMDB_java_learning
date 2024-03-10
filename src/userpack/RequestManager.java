package userpack;

import observerpack.Request;
import java.util.List;

public interface RequestManager {
    void createRequest(Request request);
    void removeRequest(Request request);
    //public List<Request> getMyIssuedRequests();
}
