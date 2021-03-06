package com.likhina.exception;

public class InvalidArgumentException extends Exception {
    public InvalidArgumentException() {
        super("App didn't get valid input parameters. It should be at least two (path to the files) parameters.");
    }
}
