package xyz.brenden.firebase101;

public class FriendlyMessage {

    // Properties
    private String uid;
    private String message;
    private String name;
    private long time;

    // Constructors
    public FriendlyMessage() {
    }

    public FriendlyMessage(String uid, String message, String name, long time) {
        super();
        this.uid = uid;
        this.message = message;
        this.name = name;
        this.time = time;
    }

    // Getters & Setters
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
