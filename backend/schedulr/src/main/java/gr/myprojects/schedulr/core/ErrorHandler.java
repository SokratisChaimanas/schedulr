package gr.myprojects.schedulr.core;

import gr.myprojects.schedulr.core.exceptions.*;
import gr.myprojects.schedulr.dto.response.FailureResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import org.springframework.security.access.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<FailureResponseDTO> handleValidationException(ValidationException e) {
        BindingResult bindingResult = e.getBindingResult();

        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError: bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        FailureResponseDTO failureResponseDTO = FailureResponseDTO.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code("ValidationError")
                .message(e.getMessage())
                .extraInformation(errors)
                .build();

        return new ResponseEntity<>(failureResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppObjectNotFoundException.class)
    public ResponseEntity<FailureResponseDTO> handleAppObjectNotFoundException(AppObjectNotFoundException e) {
        FailureResponseDTO failureResponseDTO = FailureResponseDTO.builder()
                .status(HttpStatus.NOT_FOUND)
                .code(e.getCODE())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(failureResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AppObjectAlreadyExistsException.class)
    public ResponseEntity<FailureResponseDTO> handleAppObjectAlreadyExistsException(AppObjectAlreadyExistsException e) {
        FailureResponseDTO failureResponseDTO = FailureResponseDTO.builder()
                .status(HttpStatus.CONFLICT)
                .code(e.getCODE())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(failureResponseDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotAuthorizedException.class)
    public ResponseEntity<FailureResponseDTO> handleUserNotAuthorizedException(UserNotAuthorizedException e) {
        FailureResponseDTO failureResponseDTO = FailureResponseDTO.builder()
                .status(HttpStatus.FORBIDDEN)
                .code(e.getCODE())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(failureResponseDTO, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EventFullException.class)
    public ResponseEntity<FailureResponseDTO> handleEventFullException(EventFullException e) {
        FailureResponseDTO failureResponseDTO = FailureResponseDTO.builder()
                .status(HttpStatus.CONFLICT)
                .code(e.getCODE())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(failureResponseDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AppObjectUnavailableException.class)
    public ResponseEntity<FailureResponseDTO> handleAppObjectUnavailableException(AppObjectUnavailableException e) {
        FailureResponseDTO failureResponseDTO = FailureResponseDTO.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(e.getCODE())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(failureResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppObjectInvalidArgumentException.class)
    public ResponseEntity<FailureResponseDTO> handleAppObjectInvalidArgumentException(AppObjectInvalidArgumentException e) {
        FailureResponseDTO failureResponseDTO = FailureResponseDTO.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(e.getCODE())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(failureResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<FailureResponseDTO> handleAccessDeniedException(AccessDeniedException e) {
        FailureResponseDTO failureResponseDTO = FailureResponseDTO.builder()
                .status(HttpStatus.FORBIDDEN)
                .code("AccessDenied")
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(failureResponseDTO, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AppServerException.class)
    public ResponseEntity<FailureResponseDTO> handleAppServerException(AppServerException e) {
        FailureResponseDTO failureResponseDTO = FailureResponseDTO.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .code(e.getCODE())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(failureResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
