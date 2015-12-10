/*
 * Copyright (C) 2007-2015, GoodData(R) Corporation. All rights reserved.
 */

package com.gooddata.dataset;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * TODO
 */
public class UploadColumns {

    private final List<String> columns;

    @JsonCreator
    public UploadColumns(@JsonProperty("columns") List<String> columns) {
        this.columns = columns;
    }

    public List<String> getColumns() {
        return columns;
    }
}
