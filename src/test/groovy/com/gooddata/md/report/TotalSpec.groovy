package com.gooddata.md.report

import spock.lang.Specification
import spock.lang.Unroll


class TotalSpec extends Specification {

    void "of() should throw for unknown"() {
        when:
        Total.of(unknown)

        then:
        def e = thrown(UnsupportedOperationException)
        e.message ==~ regex

        where:
        unknown   | regex
        'unknown' | /.*"unknown".*/
        ''        | /.*"".*/

    }

    @Unroll('of("#value") is #total')
    void "of should work for all values"() {
        expect:
        total == Total.of(value)

        where:
        value << Total.values()*.toString()
        total << Total.values()
    }

}
