/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.compute.afm

import com.gooddata.compute.ObjIdentifierQualifier
import com.gooddata.compute.ObjUriQualifier
import org.joda.time.LocalDate
import spock.lang.Specification

import static com.gooddata.util.ResourceUtils.readObjectFromResource
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource
import static org.joda.time.LocalDate.now
import static spock.util.matcher.HamcrestSupport.that

class AbsoluteDateFilterTest extends Specification {

    private static final LocalDate FROM = new LocalDate(2017, 9, 25)
    private static final LocalDate TO = new LocalDate(2017, 9, 30)

    def "should serialize"() {
        expect:
        that new AbsoluteDateFilter(new ObjIdentifierQualifier('date.attr'), FROM, TO),
                jsonEquals(resource("compute/afm/absoluteDateFilter.json"))
    }

    def "should deserialize"() {
        when:
        AbsoluteDateFilter filter = readObjectFromResource('/compute/afm/absoluteDateFilter.json', AbsoluteDateFilter)

        then:
        with(filter) {
            (dataSet as ObjIdentifierQualifier).identifier == 'date.attr'
            from == FROM
            to == TO
        }
        filter.toString()
    }


    def "should copy"() {
        when:
        def filter = new AbsoluteDateFilter(new ObjIdentifierQualifier("id"), now(), now())
        def copy = filter.withObjUriQualifier(new ObjUriQualifier("uri"))

        then:
        copy.getObjQualifier().getUri() == "uri"
    }

}
