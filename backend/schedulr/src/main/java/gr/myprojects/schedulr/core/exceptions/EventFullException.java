package gr.myprojects.schedulr.core.exceptions;

public class EventFullException extends AppGenericException {
    private static final String DEFAULT_CODE = "EventFull";

    public EventFullException(String message) {
        super(DEFAULT_CODE, message);
    }
}
