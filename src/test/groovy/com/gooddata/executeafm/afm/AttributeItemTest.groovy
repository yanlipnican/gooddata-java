/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.executeafm.afm

import com.gooddata.executeafm.IdentifierObjQualifier
import com.gooddata.executeafm.UriObjQualifier
import nl.jqno.equalsverifier.EqualsVerifier
import spock.lang.Specification
import spock.lang.Unroll

import static com.gooddata.util.ResourceUtils.readObjectFromResource
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource
import static spock.util.matcher.HamcrestSupport.that

class AttributeItemTest extends Specification {

    private static final String ATTRIBUTE_ITEM_JSON = 'executeafm/afm/attributeItem.json'
    private static final String ATTRIBUTE_ITEM_FULL_JSON = 'executeafm/afm/attributeItemFull.json'

    private static final UriObjQualifier QUALIFIER = new UriObjQualifier('/gdc/md/projectId/obj/1')

    @Unroll
    def "should serialize #set values"() {
        expect:
        that item, jsonEquals(resource(file))

        where:
        set        | item                                             | file
        'all'      | new AttributeItem(QUALIFIER, 'localId', 'alias') | ATTRIBUTE_ITEM_FULL_JSON
        'required' | new AttributeItem(QUALIFIER, 'localId')          | ATTRIBUTE_ITEM_JSON
    }

    def "should deserialize all values"() {
        when:
        AttributeItem item = readObjectFromResource("/$ATTRIBUTE_ITEM_FULL_JSON", AttributeItem)

        then:
        item.displayForm == QUALIFIER
        item.localIdentifier == 'localId'
        item.toString()
    }

    def "should deserialize required values"() {
        when:
        AttributeItem item = readObjectFromResource("/$ATTRIBUTE_ITEM_JSON", AttributeItem)

        then:
        item.displayForm == QUALIFIER
        item.localIdentifier == 'localId'
    }

    def "should set properties"() {
        when:
        def item = new AttributeItem(new IdentifierObjQualifier("id"), 'aId')
        item.setAlias('alias')

        then:
        item.alias == "alias"
    }

    def "should verify equals"() {
        expect:
        EqualsVerifier.forClass(AttributeItem).usingGetClass()
                .withIgnoredFields("alias")
                .verify()
    }
}
