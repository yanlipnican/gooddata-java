package com.gooddata.sdk.model.executeafm.afm;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.gooddata.util.GoodDataToStringBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@JsonRootName("uris")
public class UriAttributeFilterElements implements AttributeFilterElements, Serializable {

    private static final long serialVersionUID = -588170788038973574L;
    private final List<String> uris;

    public UriAttributeFilterElements(final List<String> uris) {
        this.uris = uris;
    }

    @JsonValue
    public List<String> getUris() {
        return uris;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UriAttributeFilterElements that = (UriAttributeFilterElements) o;
        return Objects.equals(uris, that.uris);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uris);
    }

    @Override
    public String toString() {
        return GoodDataToStringBuilder.defaultToString(this);
    }
}
