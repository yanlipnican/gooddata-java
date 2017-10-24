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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = HeaderItem.AttributeHeaderItem.class, name = HeaderItem.AttributeHeaderItem.NAME),
        @JsonSubTypes.Type(value = HeaderItem.MeasureHeaderItem.class, name = HeaderItem.MeasureHeaderItem.NAME),
        @JsonSubTypes.Type(value = HeaderItem.TotalHeaderItem.class, name = HeaderItem.TotalHeaderItem.NAME)
})
public abstract class HeaderItem {
    private final String name;

    protected HeaderItem(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return GoodDataToStringBuilder.defaultToString(this);
    }

    public static abstract class MDHeaderItem extends HeaderItem {

        private final String uri;

        private final String identifier;

        protected MDHeaderItem(final String name, final String uri, final String identifier) {
            super(name);
            this.uri = uri;
            this.identifier = identifier;
        }

        public String getUri() {
            return uri;
        }

        public String getIdentifier() {
            return identifier;
        }
    }

    @JsonRootName(AttributeHeaderItem.NAME)
    public static class AttributeHeaderItem extends MDHeaderItem {

        static final String NAME = "attributeHeaderItem";

        @JsonCreator
        public AttributeHeaderItem(@JsonProperty("name") final String name, @JsonProperty("uri") final String uri,
                                   @JsonProperty("identifier") final String identifier) {
            super(name, uri, identifier);
        }
    }

    @JsonRootName(MeasureHeaderItem.NAME)
    public static class MeasureHeaderItem extends MDHeaderItem {

        static final String NAME = "measureHeaderItem";

        private final String format;

        @JsonCreator
        public MeasureHeaderItem(@JsonProperty("name") final String name, @JsonProperty("uri") final String uri,
                                 @JsonProperty("identifier") final String identifier, @JsonProperty("format") final String format) {
            super(name, uri, identifier);
            this.format = format;
        }

        public String getFormat() {
            return format;
        }
    }

    @JsonRootName(TotalHeaderItem.NAME)
    public static class TotalHeaderItem extends HeaderItem {

        static final String NAME = "totalHeaderItem";

        @JsonCreator
        public TotalHeaderItem(@JsonProperty("name") final String name) {
            super(name);
        }
    }
}
