package com.foxminded.controllers;

import com.foxminded.services.exceptions.EntityAlreadyExistsException;
import com.foxminded.services.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
public class ErrorHandlingControllerAdvice {

    private static final String MESSAGE = "message";
    private static final String ERROR_VIEW = "error";

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ModelAndView onBindException(HttpServletRequest req, BindException e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject(MESSAGE, e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
        mav.addObject("url", req.getRequestURL());
        req.getAttributeNames();
        mav.setViewName(ERROR_VIEW);
        return mav;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ModelAndView onEntityNotFoundException(HttpServletRequest req, EntityNotFoundException e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject(MESSAGE, e.getMessage());
        mav.addObject("url", req.getRequestURL());
        req.getAttributeNames();
        mav.setViewName(ERROR_VIEW);
        return mav;
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ModelAndView onEntityAlreadyExistsException(HttpServletRequest req, EntityAlreadyExistsException e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject(MESSAGE, e.getMessage());
        mav.addObject("url", req.getRequestURL());
        req.getAttributeNames();
        mav.setViewName(ERROR_VIEW);
        return mav;
    }
}
