package com.gooddata.executeafm.response

import spock.lang.Specification

import static com.gooddata.util.ResourceUtils.readObjectFromResource
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource
import static spock.util.matcher.HamcrestSupport.that

class TotalHeaderItemTest extends Specification {

    private static final String TOTAL_HEADER_ITEM_JSON = 'executeafm/response/totalHeaderItem.json'

    def "should serialize"() {
        expect:
        that new TotalHeaderItem('avg'),
                jsonEquals(resource(TOTAL_HEADER_ITEM_JSON))
    }

    def "should deserialize"() {
        when:
        TotalHeaderItem item = readObjectFromResource("/$TOTAL_HEADER_ITEM_JSON", TotalHeaderItem)

        then:
        item.name == 'avg'
        item.toString()
    }
}
