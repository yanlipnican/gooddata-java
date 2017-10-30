/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.executeafm.result;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gooddata.executeafm.afm.ObjectAfm;
import com.gooddata.util.GoodDataToStringBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.gooddata.util.Validate.notNull;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.ArrayUtils.toObject;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName("executionResult")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExecutionResult {

    private Object data;
    private final Paging paging;

    private List<List<List<AttributeHeaderItem>>> attributeHeaderItems;
    private List<List<List<Object>>> totals;

    @JsonCreator
    public ExecutionResult(@JsonProperty("data") final Object data,
                           @JsonProperty("paging") final Paging paging,
                           @JsonProperty("attributeHeaderItesm") final List<List<List<AttributeHeaderItem>>> attributeHeaderItems,
                           @JsonProperty("totals") final List<List<List<Object>>> totals) {
        this(data, paging);
        this.attributeHeaderItems = attributeHeaderItems;
        this.totals = totals;
    }

    public ExecutionResult(final Object data, final Paging paging) {
        this.data = notNull(data, "data");
        this.paging = notNull(paging, "paging");
    }

    public Object getData() {
        return data;
    }

    public Paging getPaging() {
        return paging;
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

    public List<List<List<Object>>> getTotals() {
        return totals;
    }

    public void setTotals(final List<List<List<Object>>> totals) {
        this.totals = totals;
    }

    public String toString() {
        return GoodDataToStringBuilder.defaultToString(this);
    }
}
