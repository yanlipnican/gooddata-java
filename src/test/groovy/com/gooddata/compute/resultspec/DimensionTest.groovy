/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.compute.resultspec

import com.gooddata.md.report.Total
import spock.lang.Specification

import static com.gooddata.util.ResourceUtils.readObjectFromResource
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource
import static spock.util.matcher.HamcrestSupport.that

class DimensionTest extends Specification {


    private static final TotalItem TOTAL = new TotalItem('m1', Total.AVG, 'i1')

    def "should serialize"() {
        expect:
        that new Dimension('dName', 'i1', 'i2'),
                jsonEquals(resource('compute/resultspec/dimension.json'))
    }

    def "should serialize full"() {
        expect:
        that new Dimension('dName', 'i1', 'i2')
                .addTotal(TOTAL),
                    jsonEquals(resource('compute/resultspec/dimensionFull.json'))
    }

    def "should deserialize"() {
        when:
        Dimension dimension = readObjectFromResource('/compute/resultspec/dimension.json', Dimension)

        then:
        dimension.name == 'dName'
        dimension.itemIdentifiers == ['i1', 'i2']
        dimension.totals == null
    }

    def "should deserialize full"() {
        when:
        Dimension dimension = readObjectFromResource('/compute/resultspec/dimensionFull.json', Dimension)

        then:
        dimension.name == 'dName'
        dimension.itemIdentifiers == ['i1', 'i2']
        dimension.totals.size() == 1
        dimension.totals.first() == TOTAL
        dimension.toString()
    }

    def "should add and find total"() {
        given:
        Dimension dimension = new Dimension('d1', 'i1', 'i2')

        when:
        dimension.addTotal(TOTAL)
            .addTotal(new TotalItem('m1', Total.AVG, 'i2'))

        then:
        dimension.totals.size() == 2
        dimension.findTotals('i1').first() == TOTAL

        when:
        dimension.setTotals(null)

        then:
        dimension.totals == null
        dimension.findTotals('i1')?.empty
    }

}
