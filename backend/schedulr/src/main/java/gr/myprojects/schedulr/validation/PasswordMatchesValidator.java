package gr.myprojects.schedulr.validation;

import gr.myprojects.schedulr.dto.user.UserRegisterDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserRegisterDTO> {

    @Override
    public boolean isValid(UserRegisterDTO dto, ConstraintValidatorContext constraintValidatorContext) {
        return Objects.equals(dto.getPassword(), dto.getConfirmPassword());
    }
}
