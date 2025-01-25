package gr.myprojects.schedulr.core.exceptions;

public class AppObjectAlreadyExistsException extends AppGenericException {
    private static final String DEFAULT_CODE = "AlreadyExists";

    public AppObjectAlreadyExistsException(String entity, String message) {
        super(entity + DEFAULT_CODE, message);
    }
}
