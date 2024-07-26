package com.pumppals.dao.exceptions;

public class ChallengeDatabaseException extends RuntimeException {
    public ChallengeDatabaseException(String message) {
        super(message);
    }

    public ChallengeDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
