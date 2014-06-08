/*
 * Copyright (C) 2007-2014, GoodData(R) Corporation. All rights reserved.
 */
package com.gooddata.md.report;

import com.gooddata.md.Metric;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import static com.gooddata.Validate.notNull;

/**
 * {@link Metric} Grid Element
 */
public class MetricGridElement extends AbstractGridElement {

    @JsonCreator
    MetricGridElement(@JsonProperty("uri") final String uri, @JsonProperty("alias") final String alias) {
        super(uri, alias);
    }

    public MetricGridElement(String uri) {
        this(uri, "");
    }

    public MetricGridElement(Metric metric) {
        this(notNull(metric, "metric").getUri());
    }

}
