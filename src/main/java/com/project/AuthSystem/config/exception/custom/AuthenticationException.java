package com.project.AuthSystem.config.exception.custom;

public class AuthenticationException  extends RuntimeException{

    public AuthenticationException(String msg){
        super(msg);
    }
}
