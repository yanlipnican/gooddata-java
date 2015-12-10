/*
 * Copyright (C) 2007-2015, GoodData(R) Corporation. All rights reserved.
 */

package com.gooddata.dataset;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TODO
 */
public class FlushData {

    private final boolean incremental;


    @JsonCreator
    public FlushData(@JsonProperty("incremental") boolean incremental) {
        this.incremental = incremental;
    }

    public boolean isIncremental() {
        return incremental;
    }
}
