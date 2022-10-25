/*                                                           BaseModel.java
 *                                                                TimeClock
 * ------------------------------------------------------------------------
 *
 * ABSTRACT:
 * --------
 *  Base model class - manages object id
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

// language, os, platform imports
import java.util.UUID;

/**
 * Base model class - manages object id
 */
public class BaseModel {

    /// Unique id of shift instance
    private final UUID mUuid;

    /// Get accessor for uuid
    public UUID getUuid() {
        return mUuid;
    }

    /**
     * Parameterized constructor
     * @param uuid  Unique identifier of object
     */
    protected BaseModel(UUID uuid) {
        super();
        mUuid = uuid;
    }

    /**
     * Default constructor - generates UUID internally
     */
    protected BaseModel() {
        // Generate a uuid and call parameterized constructor
        this(UUID.randomUUID());
    }
}
