package gr.myprojects.schedulr.core.exceptions;

public class AppObjectUnavailableException extends AppGenericException{
    private static final String DEFAULT_CODE = "Unavailable";

    public AppObjectUnavailableException(String entity, String message) {
        super(entity + DEFAULT_CODE, message);
    }
}
