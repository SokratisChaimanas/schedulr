package gr.myprojects.schedulr.core.exceptions;

import lombok.Getter;

@Getter
public class AppGenericException extends Exception {
    private final String CODE;

    public AppGenericException(String code, String message) {
        super(message);
        this.CODE = code;
    }
}
