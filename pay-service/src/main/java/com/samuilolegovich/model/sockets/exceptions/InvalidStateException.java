package com.samuilolegovich.model.sockets.exceptions;

import org.springframework.stereotype.Component;

@Component
public class InvalidStateException extends Exception {
    public InvalidStateException() {
    }
}
