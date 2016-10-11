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
//
//    @Test
//    public void testOf() throws Exception {
//        for (Total total : Total.values()) {
//            assertThat(Total.of(total.toString()), is(total));
//        }
//    }

}
