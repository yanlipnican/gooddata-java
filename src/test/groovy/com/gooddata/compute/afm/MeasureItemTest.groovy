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

class MeasureItemTest extends Specification {

    private static final ObjUriQualifier QUALIFIER = new ObjUriQualifier('/gdc/md/projectId/obj/1')

    def "should serialize full"() {
        expect:
        that new MeasureItem(new SimpleMeasureDefinition(QUALIFIER), "lId", "alias"),
                jsonEquals(resource('compute/afm/measureItemFull.json'))
    }

    def "should serialize"() {
        expect:
        that new MeasureItem(new SimpleMeasureDefinition(QUALIFIER)),
                jsonEquals(resource('compute/afm/measureItem.json'))
    }

    def "should deserialize"() {
        when:
        MeasureItem measureItem = readObjectFromResource('/compute/afm/measureItem.json', MeasureItem)

        then:
        with(measureItem.definition as SimpleMeasureDefinition) {
            (item as ObjUriQualifier).uri == QUALIFIER.uri
        }
        !measureItem.isAdHoc()
    }

    def "should deserialize full"() {
        when:
        MeasureItem measureItem = readObjectFromResource('/compute/afm/measureItemFull.json', MeasureItem)

        then:
        with(measureItem.definition as SimpleMeasureDefinition) {
            (item as ObjUriQualifier).uri == QUALIFIER.uri
        }
        measureItem.localIdentifier == 'lId'
        measureItem.alias == 'alias'
        !measureItem.isAdHoc()
    }

    def "should set properties"() {
        when:
        def measureItem = new MeasureItem(new SimpleMeasureDefinition(new ObjIdentifierQualifier("id")))
        measureItem.setAlias("alias")
        measureItem.setLocalIdentifier("lId")

        then:
        measureItem.localIdentifier == 'lId'
        measureItem.alias == 'alias'
        measureItem.toString()
    }
}
