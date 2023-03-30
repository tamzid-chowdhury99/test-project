package com.aetna.demo.exception;

import com.cvs.digital.hc.common.error.model.Error;

import java.util.List;

public class UserCollectionException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public UserCollectionException(String message) {
        super(message);
    }

    public static String NotFoundException(String id){
        return "User with id "+id+" does not exist";
    }

    public static String UserAlreadyExists(String email){
        return "User with email " + email + " already exists";
    }

}
