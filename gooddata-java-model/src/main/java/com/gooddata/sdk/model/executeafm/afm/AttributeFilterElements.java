package com.gooddata.sdk.model.executeafm.afm;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Represents elements, which can be used as value of "in" or "notIn" by {@link AttributeFilter}.
 */
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UriAttributeFilterElements.class, name = "uris"),
        @JsonSubTypes.Type(value = ValueAttributeFilterElements.class, name = "values")
})
public interface AttributeFilterElements {
}
