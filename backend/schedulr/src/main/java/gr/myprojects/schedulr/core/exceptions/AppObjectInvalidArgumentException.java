package gr.myprojects.schedulr.core.exceptions;

public class AppObjectInvalidArgumentException extends AppGenericException {
    private static final String DEFAULT_CODE = "InvalidArgument";

    public AppObjectInvalidArgumentException(String entity, String message) {
        super(entity + DEFAULT_CODE, message);
    }
}
