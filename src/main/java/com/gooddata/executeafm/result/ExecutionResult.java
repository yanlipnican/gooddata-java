/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.executeafm.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gooddata.util.GoodDataToStringBuilder;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.ArrayUtils.toObject;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName("executionResult")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExecutionResult {

    private Object data;

    private List<List<List<AttributeHeaderItem>>> attributeHeaderItems;

    // todo totals

    // todo encapsulate to paging
    private List<Integer> size;
    private List<Integer> offset;
    private List<Integer> overallSize;

    public Object getData() {
        return data;
    }

    public List<List<List<AttributeHeaderItem>>> getAttributeHeaderItems() {
        return attributeHeaderItems;
    }

    public void setAttributeHeaderItems(final List<List<List<AttributeHeaderItem>>> attributeHeaderItems) {
        this.attributeHeaderItems = attributeHeaderItems;
    }

    public void addAttributeHeaderItems(final List<List<AttributeHeaderItem>> items) {
        if (attributeHeaderItems == null) {
            setAttributeHeaderItems(new ArrayList<>());
        }
        attributeHeaderItems.add(items);
    }

    public List<Integer> getSize() {
        return size;
    }

    public List<Integer> getOffset() {
        return offset;
    }

    public List<Integer> getOverallSize() {
        return overallSize;
    }

    public void setData(final Object data) {
        this.data = data;
    }

    public void setSize(final List<Integer> size) {
        this.size = size;
    }

    public void setOffset(final List<Integer> offset) {
        this.offset = offset;
    }

    public void setOverallSize(final List<Integer> overallSize) {
        this.overallSize = overallSize;
    }

    public void size(final int... size) {
        this.size = asList(toObject(size));
    }

    public void overallSize(final int... overallSize) {
        this.overallSize = asList(toObject(overallSize));
    }

    public void offset(final int... offset) {
        this.offset = asList(toObject(offset));
    }

    public String toString() {
        return GoodDataToStringBuilder.defaultToString(this);
    }
}
