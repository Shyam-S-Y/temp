package com.idcard.controller;

public interface Validator {
    boolean validate(Object data) throws ValidationException;
    String getErrorMessage();
}