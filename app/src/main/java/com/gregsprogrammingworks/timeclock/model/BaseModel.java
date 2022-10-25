package com.gregsprogrammingworks.timeclock.model;

import java.util.UUID;

public class BaseModel {

    /// Unique id of shift instance
    private final UUID mUuid;

    /// Get accessor for uuid
    public UUID getUuid() {
        return mUuid;
    }

    protected BaseModel(UUID uuid) {
        super();
        mUuid = uuid;
    }

    protected BaseModel(String uuidStr) {
        this(UUID.fromString(uuidStr));
    }

    protected BaseModel() {
        this(UUID.randomUUID());
    }
}
