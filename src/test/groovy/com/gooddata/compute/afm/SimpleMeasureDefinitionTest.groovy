/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.compute.afm

import com.gooddata.compute.ObjIdentifierQualifier
import com.gooddata.compute.ObjUriQualifier
import spock.lang.Specification

import static com.gooddata.util.ResourceUtils.readObjectFromResource
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource
import static spock.util.matcher.HamcrestSupport.that

class SimpleMeasureDefinitionTest extends Specification {

    private static final ObjUriQualifier QUALIFIER = new ObjUriQualifier('/gdc/md/projectId/obj/1')

    def "should serialize full"() {
        expect:
        that new SimpleMeasureDefinition(QUALIFIER, Aggregation.AVG,
                true, new PositiveAttributeFilter(QUALIFIER,'foo')),
                jsonEquals(resource('compute/afm/simpleMeasureDefinitionFull.json'))
    }

    def "should serialize"() {
        expect:
        that new SimpleMeasureDefinition(QUALIFIER),
                jsonEquals(resource('compute/afm/simpleMeasureDefinition.json'))
    }

    def "should deserialize"() {
        when:
        SimpleMeasureDefinition definition = readObjectFromResource('/compute/afm/simpleMeasureDefinition.json', SimpleMeasureDefinition)

        then:
        with(definition.item as ObjUriQualifier) {
            uri == QUALIFIER.uri
        }
    }

    def "should deserialize full"() {
        when:
        SimpleMeasureDefinition definition = readObjectFromResource('/compute/afm/simpleMeasureDefinitionFull.json', SimpleMeasureDefinition)

        then:
        with(definition.item as ObjUriQualifier) {
            uri == QUALIFIER.uri
        }
        definition.aggregation == 'avg'
        definition.showInPercent
        with(definition.filters[0] as PositiveAttributeFilter) {
            (it.displayForm as ObjUriQualifier).uri == QUALIFIER.uri
            it.in == ['foo']
        }
    }

    def "should have default properties"() {
        when:
        def definition = new SimpleMeasureDefinition(new ObjIdentifierQualifier("id"))
        definition.setFilters(null)

        then:
        !definition.hasAggregation()
        !definition.hasShowInPercent()
        !definition.hasFilters()
        !definition.isAdHoc()

        definition.toString()
    }

    def "should set properties"() {
        when:
        def definition = new SimpleMeasureDefinition(new ObjIdentifierQualifier("id"))
        definition.setAggregation(Aggregation.AVG)
        definition.setShowInPercent(true)
        definition.addFilter(new PositiveAttributeFilter(new ObjIdentifierQualifier("f")))

        then:
        definition.isAdHoc()

        definition.hasAggregation()
        definition.aggregation == 'avg'

        definition.hasShowInPercent()
        definition.getShowInPercent()

        definition.hasFilters()
    }

    def "should copy with uri"() {
        when:
        def definition = new SimpleMeasureDefinition(new ObjIdentifierQualifier("id"))
        def copy = definition.withObjUriQualifier(new ObjUriQualifier("uri"))

        then:
        copy.getUri() == 'uri'
        copy.getObjQualifier().getUri() == 'uri'
    }
}
