/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.compute.afm

import com.gooddata.compute.ObjIdentifierQualifier
import com.gooddata.compute.ObjUriQualifier
import spock.lang.Specification
import spock.lang.Unroll

import static com.gooddata.util.ResourceUtils.readObjectFromResource
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource
import static spock.util.matcher.HamcrestSupport.that

class ObjectAfmTest extends Specification {

    private static final ObjUriQualifier QUALIFIER = new ObjUriQualifier('/gdc/md/projectId/obj/1')

    def "should serialize full"() {
        expect:
        that new ObjectAfm(
                [new AttributeItem(QUALIFIER)],
                [new ExpressionFilter('some expression')],
                [new MeasureItem(new SimpleMeasureDefinition(QUALIFIER))],
                [new NativeTotalItem('mId', 'a1', 'a2')]
        ),
                jsonEquals(resource('compute/afm/objectAfm.json'))
    }

    def "should serialize"() {
        expect:
        that new ObjectAfm(),
                jsonEquals(resource('compute/empty.json'))
    }

    def "should deserialize"() {
        when:
        ObjectAfm objectAfm = readObjectFromResource('/compute/empty.json', ObjectAfm)

        then:
        objectAfm.measures == null
        objectAfm.filters == null
        objectAfm.attributes == null
        objectAfm.nativeTotals == null
    }

    def "should deserialize full"() {
        when:
        ObjectAfm objectAfm = readObjectFromResource('/compute/afm/objectAfm.json', ObjectAfm)

        then:
        objectAfm.measures.size() == 1
        objectAfm.filters.size() == 1
        objectAfm.attributes.size() == 1
        objectAfm.nativeTotals.size() == 1
    }

    def "should get attribute and measure"() {
        given:
        def attribute = new AttributeItem(QUALIFIER, 'localIdA')
        def measure = new MeasureItem(new SimpleMeasureDefinition(QUALIFIER), 'localIdM')
        def afm = new ObjectAfm([attribute], null, [measure], null)

        expect:
        afm.getAttribute('localIdA') == attribute
        afm.getMeasure('localIdM') == measure
    }

    @Unroll
    def "should throw when get for nonexistent #item"() {
        when:
        new ObjectAfm()."get$item"()

        then:
        thrown(IllegalArgumentException)

        where:
        item << ['Attribute', 'Measure']
    }

    def "should add properties"() {
        when:
        def afm = new ObjectAfm()
        afm.addFilter(Spy(CompatibilityFilter))
        afm.addAttribute(new AttributeItem(new ObjIdentifierQualifier("id")))
        afm.addMeasure(new MeasureItem(Spy(MeasureDefinition)))
        afm.addNativeTotal(new NativeTotalItem("mId"))

        then:
        !afm.getFilters().isEmpty()
        !afm.getAttributes().isEmpty()
        !afm.getMeasures().isEmpty()
        !afm.getNativeTotals().isEmpty()
    }
}
