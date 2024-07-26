package com.pumppals.dao.exceptions;

import java.util.UUID;

public class ChallengeNotFoundException extends RuntimeException {
    public ChallengeNotFoundException(UUID id) {
        super("Challenge with ID '" + id + "' not found");
    }
}
