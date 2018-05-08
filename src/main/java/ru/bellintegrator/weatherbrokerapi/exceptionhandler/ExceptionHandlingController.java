package ru.bellintegrator.weatherbrokerapi.exceptionhandler;

import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class ExceptionHandlingController {
    private Logger log = LoggerFactory.getLogger(ExceptionHandlingController.class);

    @ResponseBody
    @ExceptionHandler(value = NotFoundException.class)
    public void handleNotFoundException(NotFoundException ex) {
        log.error(ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = Throwable.class)
    public void handleThrowable(Throwable ex) {
        log.error(ex.getMessage());
    }
}
