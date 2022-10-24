/*                                                            Employee.java
 *                                                                TimeClock
 * ------------------------------------------------------------------------
 *
 * ABSTRACT:
 * --------
 *  Employee model (POJO) class
 * ------------------------------------------------------------------------
 *
 * COPYRIGHT:
 * ---------
 *  Copyright (C) 2022 Greg Winton
 * ------------------------------------------------------------------------
 *
 * LICENSE:
 * -------
 *  This program is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation, either version 3 of
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.
 *
 *  If not, see http://www.gnu.org/licenses/.
 * ------------------------------------------------------------------------ */
package com.gregsprogrammingworks.timeclock.model;

/**
 * Employee model class. In order to use the system, a user
 * *MUST* have an employee record
 */
public class Employee {

    /// Tag for logging and exceptions
    static private final String TAG = Employee.class.getSimpleName();

    /// Unique identifier of employee - read only
    private final String mEmployeeId;

    /// Free-form text employee name - may not be unique and may be edited
    private String mName;

    /**
     * Employee constructor with unique employeeId and name
     * @param employeeId    Employee unique identifier
     * @param name  Employee name
     */
    public Employee(String employeeId, String name) throws IllegalArgumentException {

        // Check employee employeeId
        ValidIdOrThrow(employeeId);

        // Check employee name
        ValidNameOrThrow(name);

        // If we're still here, they're valid => store them
        mEmployeeId = employeeId;
        mName = name;
    }

    /**
     * Employee id get accessor
     * @return unique identifier of employee
     */
    public String getEmployeeId() {
        return mEmployeeId;
    }

    /**
     * Get the employee name
     * @return employee name
     */
    public String getName() {
        return mName;
    }

    /**
     * Set the employee's name
     * @param name  new value for employee's name
     * @throws IllegalArgumentException if name is invalid
     */
    public void setName(String name) throws IllegalArgumentException {
        ValidNameOrThrow(name);
        mName = name;
    }

    /**
     * Check an employee id value, and throw an error if its invalid.
     * - ids must not be null
     * - ids must not be empty
     * - ids must not contain whitespace
     * @param employeeId    employee id to check
     * @throws IllegalArgumentException if employeeId is invalid
     */
    private static void ValidIdOrThrow(String employeeId) throws IllegalArgumentException {
        // Can't be null or empty
        if (null == employeeId || 0 == employeeId.length()) {
            // employeeId is null or empty. Throw an exception
            throw new IllegalArgumentException(TAG + ": Employee employeeId MUST NOT be null or empty");
        }

        // There should be no whitespace
        String deSpacedName = DeSpaceString(employeeId);
        if (! deSpacedName.equals(employeeId)) {    // Strings should still match
            // They don't => employeeId has whitespace Throw an exception.
            throw new IllegalArgumentException(TAG + ": Employee employeeId MUST NOT contain whitespace");
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
            // yes, throw an exception
            throw new IllegalArgumentException(TAG + ": Employee name MUST NOT be null");
        }

        // Make sure name less whitespace is not empty
        String deSpacedName = DeSpaceString(name);
        if (0 == deSpacedName.length()) {
            // Name less whitespace is empty - throw an exception
            throw new IllegalArgumentException(TAG + ": Employee name MUST NOT be empty or only whitespace");
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
