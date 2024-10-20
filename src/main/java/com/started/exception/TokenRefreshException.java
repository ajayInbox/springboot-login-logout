package com.started.exception;

public class TokenRefreshException extends Exception{

    public TokenRefreshException(String msg){
        super(msg);
    }

    public TokenRefreshException(String token, String msg){
        super(String.format("Token: %s, Error message: %s", token, msg));
    }
}
