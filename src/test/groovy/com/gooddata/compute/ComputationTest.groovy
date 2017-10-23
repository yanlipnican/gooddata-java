/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.compute

import com.gooddata.md.report.Total
import com.gooddata.compute.afm.*
import com.gooddata.compute.resultspec.*
import spock.lang.Specification

import static com.gooddata.util.ResourceUtils.readObjectFromResource
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource
import static spock.util.matcher.HamcrestSupport.that

class ComputationTest extends Specification {

    private static final ObjUriQualifier QUALIFIER = new ObjUriQualifier('/gdc/md/projectId/obj/1')
    private static final String COMPUTATION_JSON = 'compute/computation.json'
    private static final String COMPUTATION_FULL_JSON = 'compute/computationFull.json'

    def "should serialize"() {
        expect:
        that new Computation(
                new ObjectAfm(
                        [new AttributeItem(QUALIFIER)],
                        [new ExpressionFilter('some expression')],
                        [new MeasureItem(new SimpleMeasureDefinition(QUALIFIER))],
                        [new NativeTotalItem('mId', 'a1', 'a2')]
                )
        ),
                jsonEquals(resource(COMPUTATION_JSON))
    }

    def "should serialize full"() {
        expect:
        that new Computation(
                new ObjectAfm(
                        [new AttributeItem(QUALIFIER)],
                        [new ExpressionFilter('some expression')],
                        [new MeasureItem(new SimpleMeasureDefinition(QUALIFIER))],
                        [new NativeTotalItem('mId', 'a1', 'a2')]
                ),
                new ResultSpec(
                        [new MeasureDescription('mId')],
                        [new Dimension('dName', ['i1'], [new TotalItem('mId', Total.AVG, 'a1')] as Set)],
                        [
                                new AttributeSortItem(Direction.ASC, 'aId'),
                                new MeasureSortItem(Direction.ASC,
                                        new MeasureLocatorItem('mId'),
                                        new AttributeLocatorItem('aId', 'a1'))
                        ]
                )
        ),
                jsonEquals(resource(COMPUTATION_FULL_JSON))
    }

    def "should deserialize"() {
        when:
        Computation computation = readObjectFromResource("/$COMPUTATION_JSON", Computation)

        then:
        computation.afm != null
        computation.resultSpec == null
        computation.toString()
    }

    def "should deserialize full"() {
        when:
        Computation computation = readObjectFromResource("/$COMPUTATION_FULL_JSON", Computation)

        then:
        computation.afm != null
        computation.resultSpec != null
        computation.toString()
    }

    def "should change resultSpec"() {
        given:
        Computation computation = new Computation(new ObjectAfm())
        ResultSpec spec = new ResultSpec()

        when:
        computation.resultSpec = spec

        then:
        computation.resultSpec == spec
    }
}
