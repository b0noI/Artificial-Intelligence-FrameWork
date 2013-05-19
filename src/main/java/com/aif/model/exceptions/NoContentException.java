package com.aif.model.exceptions;

/**
 * NoContentException throws when trying to create TextMF objects (exmpl: Sentence/Word/etc...) without(or with corrupted) content
 */
public class NoContentException extends Exception{

    NoContentException(String msg, Exception e){
        super(msg, e);
    }

    public NoContentException(String msg){
        super(msg);
    }

}
