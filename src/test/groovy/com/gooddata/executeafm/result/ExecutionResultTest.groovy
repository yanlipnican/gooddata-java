/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.executeafm.result

import spock.lang.Specification

import static com.gooddata.util.ResourceUtils.readObjectFromResource

class ExecutionResultTest extends Specification {

    // todo serialize

    def "should deserialize"() {
        when:
        ExecutionResult result = readObjectFromResource('/executeafm/result/executionResult.json', ExecutionResult)

        then:
        result.offset == [0]
        result.size == [4]
        result.overallSize == [4]

        result.data == [ "-12958511.8099999", "25315434.8199999", "-2748323.76", "-7252542.67"]

        List<List<List<AttributeHeaderItem>>> accountHeaderItems = result.attributeHeaderItems
        accountHeaderItems[0][0][0].uri == "/gdc/md/FoodMartDemo/obj/124/elements?id=3200"
        accountHeaderItems[0][0][0].name == "Cost of Goods Sold"
        accountHeaderItems[0][0][1].uri == "/gdc/md/FoodMartDemo/obj/124/elements?id=6000"
        accountHeaderItems[0][0][1].name == "Salaries"

    }

}
