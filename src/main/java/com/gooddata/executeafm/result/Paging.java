/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.executeafm.result;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gooddata.util.GoodDataToStringBuilder;

import java.util.List;

import static com.gooddata.util.Validate.notEmpty;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.ArrayUtils.toObject;

public class Paging {
    private List<Integer> size;
    private List<Integer> offset;
    private List<Integer> total;

    public Paging() {
    }

    @JsonCreator
    public Paging(@JsonProperty("size") final List<Integer> size,
                  @JsonProperty("offset") final List<Integer> offset,
                  @JsonProperty("total") final List<Integer> total) {
        this.size = notEmpty(size, "size");
        this.offset = notEmpty(offset, "offset");
        this.total = notEmpty(total, "total");
    }

    public List<Integer> getSize() {
        return size;
    }

    public List<Integer> getOffset() {
        return offset;
    }

    public List<Integer> getTotal() {
        return total;
    }


    public void size(final int... size) {
        this.size = asList(toObject(size));
    }

    public void total(final int... total) {
        this.total = asList(toObject(total));
    }

    public void offset(final int... offset) {
        this.offset = asList(toObject(offset));
    }


    public String toString() {
        return GoodDataToStringBuilder.defaultToString(this);
    }
}
