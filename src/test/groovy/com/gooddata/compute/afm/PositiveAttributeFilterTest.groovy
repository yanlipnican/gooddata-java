/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.compute.afm

import com.gooddata.compute.ObjIdentifierQualifier
import com.gooddata.compute.ObjQualifier
import com.gooddata.compute.ObjUriQualifier
import spock.lang.Specification

import static com.gooddata.util.ResourceUtils.readObjectFromResource
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource
import static spock.util.matcher.HamcrestSupport.that

class PositiveAttributeFilterTest extends Specification {

    private static final ObjQualifier QUALIFIER = new ObjIdentifierQualifier('df.bum.bac')

    def "should serialize"() {
        expect:
        that new PositiveAttributeFilter(QUALIFIER, 'a', 'b'),
                jsonEquals(resource("compute/afm/positiveAttributeFilter.json"))
    }

    def "should deserialize"() {
        when:
        PositiveAttributeFilter filter = readObjectFromResource('/compute/afm/positiveAttributeFilter.json', PositiveAttributeFilter)

        then:
        with(filter) {
            displayForm == QUALIFIER
            getIn() == ['a', 'b']
        }
        filter.toString()
    }

    def "should copy"() {
        when:
        def filter = new PositiveAttributeFilter(new ObjIdentifierQualifier("id"))
        def copy = filter.withObjUriQualifier(new ObjUriQualifier("uri"))

        then:
        copy.getObjQualifier().getUri() == "uri"
    }
}
