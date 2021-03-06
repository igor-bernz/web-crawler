package com.likhina.exception;

public class OriginElementNotFound extends Exception {
    public OriginElementNotFound(String id) {
        super(String.format("Origin element wasn't found by id %s", id));
    }
}
