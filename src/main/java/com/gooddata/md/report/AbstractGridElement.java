/*
 * Copyright (C) 2007-2014, GoodData(R) Corporation. All rights reserved.
 */
package com.gooddata.md.report;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import static com.gooddata.Validate.notNull;

/**
 * Grid element
 * (metricGroup | URI | AttributeInGrid ... URI is allowed for backward compatibility, metricGroup can be maximally
 * one time in rows or columns)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
abstract class AbstractGridElement {

    private final String uri;
    private final String alias;

    @JsonCreator
    public AbstractGridElement(@JsonProperty("uri") String uri, @JsonProperty("alias") String alias) {
        this.uri = notNull(uri, "uri");
        this.alias = alias;
    }

    public String getUri() {
        return uri;
    }

    public String getAlias() {
        return alias;
    }

}
