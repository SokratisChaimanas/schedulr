package gr.myprojects.schedulr.core.exceptions;

public class AppObjectNotFoundException extends AppGenericException {
    private static final String DEFAULT_CODE = "NotFound";

    public AppObjectNotFoundException(String entity, String message) {
        super(entity + DEFAULT_CODE, message);
    }
}
