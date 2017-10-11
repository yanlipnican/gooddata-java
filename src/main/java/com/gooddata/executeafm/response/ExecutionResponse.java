/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.executeafm.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gooddata.util.GoodDataToStringBuilder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.gooddata.util.Validate.notEmpty;
import static com.gooddata.util.Validate.notNull;
import static com.gooddata.util.Validate.notNullState;
import static org.apache.commons.lang3.ArrayUtils.toObject;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName("executionResponse")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExecutionResponse {

    private static final String DATA_RESULT_LINK = "dataResult";

    private final List<ResultDimension> dimensions;
    private final Map<String, String> links;

    public ExecutionResponse(final List<ResultDimension> dimensions, final String dataResultUri) {
        this(dimensions, new LinkedHashMap<>());
        links.put(DATA_RESULT_LINK, notEmpty(dataResultUri, "dataResultUri"));
    }

    @JsonCreator
    public ExecutionResponse(@JsonProperty("dimensions") final List<ResultDimension> dimensions,
                             @JsonProperty("links") final Map<String, String> links) {
        this.dimensions = notNull(dimensions, "dimensions");
        this.links = notNull(links, "links");
    }
    public List<ResultDimension> getDimensions() {
        return dimensions;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    @JsonIgnore
    public String getDataResultUri() {
        return notNullState(links, "links").get(DATA_RESULT_LINK);
    }

    public String toString() {
        return GoodDataToStringBuilder.defaultToString(this);
    }

}
