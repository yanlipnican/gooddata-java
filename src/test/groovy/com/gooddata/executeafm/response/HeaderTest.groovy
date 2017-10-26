package com.gooddata.executeafm.response

import spock.lang.Specification
import spock.lang.Unroll

import static com.gooddata.util.ResourceUtils.readObjectFromResource

class HeaderTest extends Specification {

    @Unroll
    def "should deserialize as #type"() {
        when:
        Header header = readObjectFromResource("/executeafm/response/${type}.json", Header)

        then:
        typeClass.isInstance(header)

        where:
        typeClass << [AttributeHeader, MeasureGroupHeader]
        type = typeClass.simpleName.uncapitalize()
    }
}
