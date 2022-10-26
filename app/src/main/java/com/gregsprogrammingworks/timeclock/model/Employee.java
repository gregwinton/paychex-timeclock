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

import java.util.Objects;
import java.util.UUID;

/**
 * Employee model class. In order to use the system, a user
 * *MUST* have an employee record
 */
public class Employee extends BaseModel {

    /// Tag for logging and exceptions
    static private final String TAG = Employee.class.getSimpleName();

    /// Free-form text employee name - may not be unique and may be edited
    private String mName;

    /**
     * Employee constructor for existing employee
     * @param uuid  Employee unique identifier
     * @param name  Employee name
     * @#throws IllegalArgumentException if name is invalid
     */
    public Employee(UUID uuid, String name) throws IllegalArgumentException {

        // Have to call super first
        // (super will validate uuid)
        super(uuid);

        // Validate employee name
        ValidNameOrThrow(name);

        // If we're still here, they're valid => store them
        mName = name;
    }

    /**
     * Employee constructor with unique employeeId and name
     * @param name  Employee name
     * @throws IllegalArgumentException if name is invalid
     */
    public Employee(String name) throws IllegalArgumentException {
        super();
        setName(name);
    }

    /**
     * Employee id get accessor
     * @return unique identifier of employee
     */
    public String getEmployeeId() {
        return getUuid().toString();
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

    @Override
    public int hashCode() {
        int hash = Objects.hash(getUuid(), getName());
        return hash;
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
