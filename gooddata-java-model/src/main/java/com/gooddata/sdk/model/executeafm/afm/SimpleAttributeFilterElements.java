package com.gooddata.sdk.model.executeafm.afm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.gooddata.util.Validate.notNull;

@Deprecated
public class SimpleAttributeFilterElements extends ArrayList<String> implements CompatibilityAttributeFilterElements, Serializable {

    private static final long serialVersionUID = -2935674265292888490L;

    public SimpleAttributeFilterElements(final List<String> elements) {
        super(notNull(elements, "elements"));
    }

    @Override
    public List<String> getElements() {
        return this;
    }
}
