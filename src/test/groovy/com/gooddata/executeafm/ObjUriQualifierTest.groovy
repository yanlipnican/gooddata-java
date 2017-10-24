/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.executeafm

import nl.jqno.equalsverifier.EqualsVerifier
import spock.lang.Specification

import static com.gooddata.util.ResourceUtils.readObjectFromResource
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource
import static spock.util.matcher.HamcrestSupport.that

class ObjUriQualifierTest extends Specification {

    private static final String QUALIFIER_JSON = 'executeafm/objUriQualifier.json'

    def "should serialize"() {
        expect:
        that new ObjUriQualifier('/gdc/md/projectId/obj/1'), jsonEquals(resource(QUALIFIER_JSON))
    }

    def "should deserialize"() {
        when:
        ObjUriQualifier uriQualifier = readObjectFromResource("/$QUALIFIER_JSON", ObjUriQualifier)

        then:
        uriQualifier.uri == '/gdc/md/projectId/obj/1'
    }

    def "should verify equals"() {
        expect:
        EqualsVerifier.forClass(ObjUriQualifier).verify()
    }
}
