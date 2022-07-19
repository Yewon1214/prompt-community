package kr.co.community.handler;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.FileNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected String handlerFileNotFound(FileNotFoundException e, Model model){
        model.addAttribute("errorStatus", HttpStatus.NOT_FOUND.value());
        model.addAttribute("errorMessage", e.getMessage());
        return "app/errors/error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected String handlerException(Exception e, Model model){
        model.addAttribute("errorStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("errorMessage", e.getMessage());
        return "app/errors/error";
    }

}
