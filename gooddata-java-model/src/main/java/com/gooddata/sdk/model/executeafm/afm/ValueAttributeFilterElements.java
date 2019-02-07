package com.gooddata.sdk.model.executeafm.afm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.gooddata.util.GoodDataToStringBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@JsonRootName("values")
public final class ValueAttributeFilterElements implements AttributeFilterElements, Serializable {

    private static final long serialVersionUID = 8162844914489089022L;
    private final List<String> values;

    @JsonCreator
    public ValueAttributeFilterElements(final List<String> values) {
        this.values = values;
    }

    @JsonValue
    public List<String> getValues() {
        return values;
    }

    @Override
    public List<String> getElements() {
        return getValues();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValueAttributeFilterElements that = (ValueAttributeFilterElements) o;
        return Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

    @Override
    public String toString() {
        return GoodDataToStringBuilder.defaultToString(this);
    }
}
