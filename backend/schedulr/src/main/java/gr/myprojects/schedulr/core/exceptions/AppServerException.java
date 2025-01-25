package gr.myprojects.schedulr.core.exceptions;

public class AppServerException extends AppGenericException {
    private static final String DEFAULT_CODE = "AppServerError";

    public AppServerException(String message) {
        super(DEFAULT_CODE, message);
    }
}
