package com.pumppals.validation;

import com.pumppals.models.User;
import com.pumppals.dao.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserValidator {

    public static void validateNewUser(User user) throws ValidationException {
        List<String> errors = new ArrayList<>();

        if (user.getId() != null) {
            errors.add("User ID should not be set for new user creation");
        }
        validateCommonFields(user, errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public static void validateExistingUser(User user) throws ValidationException {
        List<String> errors = new ArrayList<>();

        if (user.getId() == null) {
            errors.add("User ID must be set for existing user");
        }
        validateCommonFields(user, errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private static void validateCommonFields(User user, List<String> errors) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            errors.add("Username cannot be empty");
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            errors.add("Name cannot be empty");
        }
        // Add more validation rules as needed
    }
}