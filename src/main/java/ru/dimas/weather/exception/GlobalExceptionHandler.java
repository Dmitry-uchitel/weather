package ru.dimas.weather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.dimas.weather.service.withdb.SessionService;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    SessionService sessionService;

    public GlobalExceptionHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    // Обработка LocationAlreadyAddedException
    @ExceptionHandler(LocationAlreadyAddedException.class)
    public ResponseEntity<String> handleLocationAlreadyAddedException(LocationAlreadyAddedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    // Обработка SessionIsAlreadyOverException
    @ExceptionHandler(SessionIsAlreadyOverException.class)
    public String handleSessionIsAlreadyOverException(SessionIsAlreadyOverException ex, Model model) {
        sessionService.deleteEndedSessions();
        model.addAttribute("errorMesage", ex.getMessage());
        return "index";
    }

    // Обработка UserAlreadyExistsException
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    // Обработка UserWithLoginIsNotExist
    @ExceptionHandler(UserWithLoginIsNotExist.class)
    public ResponseEntity<String> handleUserWithLoginIsNotExistException(UserWithLoginIsNotExist ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PageNotFoundException.class)
    public ResponseEntity<String> handlePageNotFoundException(PageNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Обработка WrongPasswordException
    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<String> handleWrongPasswordException(WrongPasswordException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // Обработка остальных исключений (генеральных)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}