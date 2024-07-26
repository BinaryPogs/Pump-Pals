package com.pumppals.dao.exceptions;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID id) {
        super("User with ID '" + id + "' not found");
    }

    public UserNotFoundException(String username) {
        super("User with username '" + username + "' not found");
    }
}
