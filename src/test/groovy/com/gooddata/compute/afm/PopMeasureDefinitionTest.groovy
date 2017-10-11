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

class PopMeasureDefinitionTest extends Specification {

    def "should serialize"() {
        expect:
        that new PopMeasureDefinition('mId', new ObjUriQualifier('/gdc/md/projectId/obj/1')),
                jsonEquals(resource('compute/afm/popMeasureDefinition.json'))
    }

    def "should deserialize"() {
        when:
        PopMeasureDefinition measure = readObjectFromResource('/compute/afm/popMeasureDefinition.json', PopMeasureDefinition)

        then:
        measure.measureIdentifier == 'mId'
        (measure.popAttribute  as ObjUriQualifier).uri == '/gdc/md/projectId/obj/1'
        measure.isAdHoc()
        measure.toString()
    }

    def "should copy"() {
        when:
        def measure = new PopMeasureDefinition("mid", new ObjIdentifierQualifier("id"))
        def copy = measure.withObjUriQualifier(new ObjUriQualifier("uri"))

        then:
        copy.objQualifier.uri == 'uri'
    }
}
