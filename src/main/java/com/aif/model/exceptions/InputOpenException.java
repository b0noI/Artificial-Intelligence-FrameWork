package com.aif.model.exceptions;

public class InputOpenException extends Exception{

    public InputOpenException(String msg){
        super(msg);
    }

    public InputOpenException(String msg, Exception e){
        super(msg, e);
    }

}
