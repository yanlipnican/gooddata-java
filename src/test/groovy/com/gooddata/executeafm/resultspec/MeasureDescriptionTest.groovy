/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.executeafm.resultspec

import spock.lang.Specification

import static com.gooddata.util.ResourceUtils.readObjectFromResource
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource
import static spock.util.matcher.HamcrestSupport.that

class MeasureDescriptionTest extends Specification {

    def "should serialize all values"() {
        expect:
        that new MeasureDescription('mId', 'als', 'fmt'),
                jsonEquals(resource('executeafm/resultspec/measureDescriptionFull.json'))
    }

    def "should serialize required values"() {
        expect:
        that new MeasureDescription('mId'),
                jsonEquals(resource('executeafm/resultspec/measureDescription.json'))
    }

    def "should deserialize all values"() {
        when:
        MeasureDescription measure = readObjectFromResource('/executeafm/resultspec/measureDescriptionFull.json', MeasureDescription)

        then:
        measure.measureIdentifier == 'mId'
        measure.alias == 'als'
        measure.format == 'fmt'
        measure.toString()
    }

    def "should deserialize required values"() {
        when:
        MeasureDescription measure = readObjectFromResource('/executeafm/resultspec/measureDescription.json', MeasureDescription)

        then:
        measure.measureIdentifier == 'mId'
    }

    def "should set values"() {
        given:
        MeasureDescription measure = new MeasureDescription('id')

        when:
        measure.measureIdentifier = 'setId'
        measure.alias = 'setAlias'
        measure.format = 'setFormat'

        then:
        with(measure) {
            measureIdentifier == 'setId'
            alias == 'setAlias'
            format == 'setFormat'
        }
    }

}
