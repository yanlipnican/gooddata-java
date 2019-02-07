package com.gooddata.sdk.model.executeafm.afm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.List;

import static com.fasterxml.jackson.databind.JsonMappingException.from;

@Deprecated
public interface CompatibilityAttributeFilterElements {

    @JsonIgnore
    List<String> getElements();

    class Deserializer extends JsonDeserializer<CompatibilityAttributeFilterElements> {
        @Override
        @SuppressWarnings("unchecked")
        public AttributeFilterElements deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            switch (jp.currentToken()) {
                case START_ARRAY:
                    return new UriAttributeFilterElements(ctxt.readValue(jp, List.class));
                case START_OBJECT:
                    return ctxt.readValue(jp, AttributeFilterElements.class);
                default:
                    throw from(jp, "Unknown value of type: " + jp.currentToken());
            }
        }
    }
}
