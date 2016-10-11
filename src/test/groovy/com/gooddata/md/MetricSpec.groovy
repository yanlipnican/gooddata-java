package com.gooddata.md

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Shared
import spock.lang.Specification

import static com.gooddata.JsonMatchers.serializesToJson
import static spock.util.matcher.HamcrestSupport.that

class MetricSpec extends Specification {

    @Shared mapper = new ObjectMapper()

    void "can be deserialized"() {
        given:
        def serialized = getClass().getResourceAsStream("/md/metric-out.json")

        when:
        final Metric metric = mapper.readValue(serialized, Metric)

        then:
        metric
        metric.expression == "SELECT AVG([/gdc/md/PROJECT_ID/obj/EXPR_ID])"
        metric.format == "#,##0"
        with(metric.maqlAst) {
            position.line == 2
            position.column == 1
            type == 'metric'
        }
        // and so on
    }

    void "should deserialize with folder"() {
        when:
        def metric = mapper.readValue(getClass().getResourceAsStream("/md/metric-folder.json"), Metric)

        then:
        "/gdc/md/ge06jy0jr6h1hzaxei6d53evw276p3xc/obj/51430" in metric?.folders
    }

    void "can be serialized"() {
        expect:
        that new Metric("Person Name", "SELECT SUM([/gdc/md/PROJECT_ID/obj/EXPR_ID])", "FORMAT"),
                serializesToJson("/md/metric-new.json")
    }
//
//    @Test
//    public void fullJsonShouldStayTheSameAfterDeserializationAndSerializationBack() throws Exception {
//        final Metric metric = new ObjectMapper()
//                .readValue(getClass().getResourceAsStream("/md/metric-out.json"), Metric.class);
//
//        assertThat(metric, jsonNodeAbsent("metric.content.tree.content[0].content[0].content[0].content"));
//        assertThat(metric, serializesToJson("/md/metric-out.json"));
//    }
//
//    @Test
//    public void shouldIgnoreLinksProperty() throws Exception {
//        final Metric metric = new ObjectMapper()
//                .readValue(getClass().getResourceAsStream("/md/metric-links.json"), Metric.class);
//        assertThat(metric, serializesToJson("/md/metric-out.json"));
//    }
}
