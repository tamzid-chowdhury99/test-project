package com.aetna.demo.exception;

public class UserCollectionException extends Exception{

    private static final long serialVersionUID = 1L;

    public UserCollectionException(String message){
        super(message);
    }

    public static String NotFoundException(String id){
        return "User with id "+id+" does not exist";
    }

    public static String UserAlreadyExists(String email){
        return "User with email " + email + " already exists";
    }

    public static String EmptyUserList(){
        return "No users in the database";
    }
}
