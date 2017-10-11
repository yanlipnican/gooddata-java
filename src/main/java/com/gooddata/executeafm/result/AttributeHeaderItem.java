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
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gooddata.util.GoodDataToStringBuilder;

@JsonTypeName("attributeHeaderItem")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class AttributeHeaderItem {

    private final String name;

    private final String uri;

    private final String identifier;

    @JsonCreator
    public AttributeHeaderItem(@JsonProperty("name") final String name, @JsonProperty("uri") final String uri,
                               @JsonProperty("identifier") final String identifier) {
        this.name = name;
        this.uri = uri;
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String toString() {
        return GoodDataToStringBuilder.defaultToString(this);
    }
}
