/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.executeafm.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

@JsonRootName(MeasureGroupHeader.NAME)
public class MeasureGroupHeader extends Header {

    static final String NAME = "measureGroupHeader";

    private final List<MeasureHeaderItem> items;

    public MeasureGroupHeader(final List<MeasureHeaderItem> items) {
        this.items = items;
    }

    @JsonCreator
    public MeasureGroupHeader(@JsonProperty("items") final List<MeasureHeaderItem> items,
                              @JsonProperty("totalItems") final List<TotalHeaderItem> totalHeaderItems) {
        super(totalHeaderItems);
        this.items = items;
    }

    public List<MeasureHeaderItem> getItems() {
        return items;
    }
}
