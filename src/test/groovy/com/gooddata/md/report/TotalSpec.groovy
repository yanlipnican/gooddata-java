package com.gooddata.md.report

import spock.lang.Specification


class TotalSpec extends Specification {

    void "of() should throw for unknown"() {
        when:
        Total.of("unknownValue")

        then:
        def e = thrown(UnsupportedOperationException)
        e.message ==~ /.*"unknownValue".*/
    }

    void "of should work for all values"() {
        expect:
        total == Total.of(value)

        where:
        value << Total.values()*.toString()
        total << Total.values()
    }

}
