/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.executeafm.result;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gooddata.util.GoodDataToStringBuilder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Header.MeasureGroupHeader.class, name = Header.MeasureGroupHeader.NAME),
        @JsonSubTypes.Type(value = Header.AttributeHeader.class, name = Header.AttributeHeader.NAME)
})
public abstract class Header {
    private final List<HeaderItem> items;

    protected Header(final List<HeaderItem> items) {
        this.items = items;
    }

    public List<HeaderItem> getItems() {
        return items;
    }

    public String toString() {
        return GoodDataToStringBuilder.defaultToString(this);
    }

    @JsonRootName(MeasureGroupHeader.NAME)
    public static class MeasureGroupHeader extends Header {

        static final String NAME = "measureGroupHeader";

        @JsonCreator
        public MeasureGroupHeader(@JsonProperty("items") final List<HeaderItem> items) {
            super(items);
        }
    }

    @JsonRootName(AttributeHeader.NAME)
    public static class AttributeHeader extends Header {

        static final String NAME = "attributeHeader";

        private final String uri;
        private final String name;

        @JsonCreator
        public AttributeHeader(@JsonProperty("uri") final String uri, @JsonProperty("name") final String name,
                               @JsonProperty("items") final List<HeaderItem> items) {
            super(items);
            this.uri = uri;
            this.name = name;
        }

        public String getUri() {
            return uri;
        }

        public String getName() {
            return name;
        }

    }
}
