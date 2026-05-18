public class UserEvent extends Event {
    public static final EventType<UserEvent> ANY = new Event<UserEvent>(Event.ANY, "ANY"); 
    public static final EventType<UserEvent> LOGIN_SUCCEDED = new Event<UserEvent>(Event.ANY,
    public static final EventType<UserEvent> LOGIN_SUCCEDED = new Event<UserEvent>(Event.ANY,
	    "LOGIN_SUCCEDED"); 
    public static final EventType<UserEvent> LOGIN_FAILED = new Event<UserEvent>(Event.ANY,
	    "LOGIN_FAILED"); 
    public UserEvent(EventType<? extends Event> eventType) {
    }
}
