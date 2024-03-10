package observerpack;
import appstructure.IMDB;
import enumpack.RequestTypes;
import userpack.User;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import usefulpack.ComparableItem;
import userpack.RequestManager;
import userpack.Staff;
import userpack.StaffInterface;

public class Request {
    private RequestTypes type;
    private LocalDateTime timestamp;
    private String request_subject;
    private Staff<ComparableItem> request_target;
    private User<ComparableItem> requester;
    private String description;
    private int id;
    private static int id_counter = 0;
    public Request(RequestTypes type, Staff<ComparableItem> request_target, String request_subject,
                   User<ComparableItem> requester, String description, LocalDateTime timestamp) {
        this.type = type;
        this.request_target = request_target;
        this.request_subject = request_subject;
        this.requester = requester;
        this.description = description;
        this.timestamp = timestamp;
        this.id = id_counter++;
    }
    public Request(RequestTypes type, Staff<ComparableItem> request_target, String request_subject,
                   User<ComparableItem> requester, String description) {
        this.type = type;
        this.request_target = request_target;
        this.request_subject = request_subject;
        this.requester = requester;
        this.description = description;
        this.id = id_counter++;
    }
    public RequestTypes getType() {
        return type;
    }
    public void setSubject(String request_subject) {
        this.request_subject = request_subject;
    }
    public String getSubject() {
        return request_subject;
    }
    public Staff<ComparableItem> getRequestTarget() {
        return request_target;
    }
    public Staff<ComparableItem> setRequestTarget(Staff<ComparableItem> request_target) {
        return this.request_target = request_target;
    }
    public User<ComparableItem> getRequester() {
        return requester;
    }
    public int getId() {
        return id;
    }
    public void setRequestTimeNow() {
        this.timestamp = LocalDateTime.now();
    }
    public static Request getRequestById(int id) {
        IMDB imdb = IMDB.getInstance();
        if (RequestHolder.getRequests() != null) {
            for (Request request : RequestHolder.getRequests()) {
                if (request.getId() == id) {
                    return request;
                }
            }
        }
        if (imdb.getRequests() != null) {
            for (Request request : imdb.getRequests()) {
                if (request.getId() == id) {
                    return request;
                }
            }
        }
        return null;
    }
    // Static nested class RequestHolder inside Request
    public static class RequestHolder {
        private static List<Request> requests = new LinkedList<>();
        private RequestHolder() {
            // Private constructor
        }
        public static void addRequest(Request request) {
            requests.add(request);
        }
        public static List<Request> getRequests() {
            return requests;
        }
        public static void removeRequest(Request request) {
            requests.remove(request);
        }
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(  "Request of type: " + type +
                    "\nRequest ID: " + id +
                    "\nCreation date: " + timestamp);
        if (request_subject != null) {
            sb.append("\nRequest subject: " + request_subject);
        }
        sb.append(  "\nRequester username: " + requester.getUsername() +
                    "\nRequest target: " + request_target.getUsername() +
                    "\nDescription: " + description);
        return sb.toString();
    }
}

