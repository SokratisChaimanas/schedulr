package gr.myprojects.schedulr.core.exceptions;

public class UserNotAuthorizedException extends AppGenericException {
    public static final String DEFAULT_CODE = "UserNotAuthorized";

    public UserNotAuthorizedException(String message) {
        super(DEFAULT_CODE, message);
    }
}
