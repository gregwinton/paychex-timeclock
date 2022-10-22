package com.gregsprogrammingworks.timeout.timecard.model;

import androidx.annotation.NonNull;

import java.util.regex.Pattern;

/**
 * Employee model class. In order to use the system, a user
 * *MUST* have an employee record
 */
public class Employee {

    /// Tag for logging and exceptions
    static private final String TAG = Employee.class.getSimpleName();

    /// Unique identifier of employee - read only
    private final String _id;

    /// Free-form text employee name - may not be unique and may be edited
    private String _name;

    /**
     * Employee constructor with unique id and name
     * @param id    Employee unique identifier
     * @param name  Employee name
     */
    public Employee(String id, String name) throws IllegalArgumentException {

        // Check employee id
        ValidIdOrThrow(id);

        // Check employee name
        ValidNameOrThrow(name);

        // If we're still here, they're valid => store them
        _id = id;
        _name = name;
    }

    /**
     * Employee id get accessor
     * @return unique identifier of employee
     */
    public String getId() {
        return _id;
    }

    /**
     * Get the employee name
     * @return employee name
     */
    public String getName() {
        return _name;
    }

    /**
     * Set the employee's name
     * @param name  new employee name. May not be null or empty
     * @throws IllegalArgumentException if Employee name is empty string
     */
    public void setName(String name) {
        ValidNameOrThrow(name);
        _name = name;
    }

    /**
     * Check an id value, and throw an error if its invalid.
     * - ids must not be null
     * - ids must not be empty
     * - ids must not contain whitespace
     * @param id    id to check
     * @throws IllegalArgumentException if id is invalid
     */
    private static void ValidIdOrThrow(String id) throws IllegalArgumentException {

        if (null == id) {
            // id is null. Throw an exception
            throw new IllegalArgumentException(TAG + ": Employee id MUST NOT be null");
        }
        else {

            // There should be no whitespace
            String deSpacedName = DeSpaceString(id);
            if (! deSpacedName.equals(id)) {    // Strings should still match

                // They don't => id has whitespace Throw an exception.
                throw new IllegalArgumentException(TAG + ": Employee id MUST NOT contain whitespace");
            }
        }
    }

    /**
     * Check an name value, and throw an error if its invalid.
     * - names must not be null
     * - names must not be empty
     * - names must not contain only whitespace
     * @param name    name to check
     * @throws IllegalArgumentException if name is invalid
     */
    private static void ValidNameOrThrow(String name) throws IllegalArgumentException {

        // Is name null?
        if (null == name) {

            // yes. name must not be null. Throw an exception
            throw new IllegalArgumentException(TAG + ": Employee name MUST NOT be null");
        }
        else {

            // Remove all whitespace from name
            String deSpacedName = DeSpaceString(name);

            // Make sure there's something left
            if (0 == deSpacedName.length())
            {
                // There's nothing left - throw an exception
                throw new IllegalArgumentException(TAG + ": Employee name MUST NOT be empty or only whitespace");
            }
        }
    }

    /**
     * Remove all spaces from a string
     * @param str   String to de-space
     * @return de-spaced string
     */
    private static String DeSpaceString(String str) {
        String deSpaced = str.replaceAll("\\s", "");
        return deSpaced;
    }
}
