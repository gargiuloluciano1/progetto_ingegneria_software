package view;
import javafx.event.*;

public class UserEvent extends Event {

    public static final EventType<UserEvent> ANY = new EventType<>(Event.ANY, "ANY");

    public static final EventType<UserEvent> LOGIN_SUCCEDED = new EventType<>(ANY, "LOGIN_SUCCEDED");

    public static final EventType<UserEvent> LOGIN_FAILED = new EventType<>(ANY, "LOGIN_FAILED");

    public static final EventType<UserEvent> REGISTRATION = new EventType<>(ANY, "NEW_REGISTRATION_REQUEST");
    
    public static final EventType<UserEvent> REGISTRATION_SUCCEDED = new EventType<>(ANY, "REGISTRATION_SUCCEDED");

    public UserEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    // any other fields of importance, e.g. data, timestamp
}
