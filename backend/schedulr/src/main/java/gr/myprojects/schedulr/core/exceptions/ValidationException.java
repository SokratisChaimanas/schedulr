package gr.myprojects.schedulr.core.exceptions;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class ValidationException extends Exception {
    private static final String DEFAULT_MESSAGE = "Validation failed for ";
    private BindingResult bindingResult;


    public ValidationException(String entity, BindingResult bindingResult) {
        super(DEFAULT_MESSAGE + entity);
        this.bindingResult = bindingResult;
    }
}
