/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.executeafm.afm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gooddata.util.GoodDataToStringBuilder;

/**
 * Represents measure within {@link ObjectAfm}
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeasureItem implements LocallyIdentifiable {

    private final MeasureDefinition definition;
    private final String localIdentifier;
    private String alias;
    private String format;

    public MeasureItem(final MeasureDefinition definition, final String localIdentifier) {
        this.definition = definition;
        this.localIdentifier = localIdentifier;
    }

    @JsonCreator
    public MeasureItem(@JsonProperty("definition") final MeasureDefinition definition,
                       @JsonProperty("localIdentifier") final String localIdentifier,
                       @JsonProperty("alias") final String alias,
                       @JsonProperty("format") final String format) {
        this.definition = definition;
        this.localIdentifier = localIdentifier;
        this.alias = alias;
        this.format = format;
    }

    public MeasureDefinition getDefinition() {
        return definition;
    }

    public String getLocalIdentifier() {
        return localIdentifier;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(final String alias) {
        this.alias = alias;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return GoodDataToStringBuilder.defaultToString(this);
    }

    @JsonIgnore
    public boolean isAdHoc() {
        return definition.isAdHoc();
    }
}
