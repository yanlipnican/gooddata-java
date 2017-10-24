/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.executeafm.result

import spock.lang.Specification

import static com.gooddata.util.ResourceUtils.readObjectFromResource

class ComputationResultTest extends Specification {

    // todo serialize

    def "should deserialize"() {
        when:
        ComputationResult result = readObjectFromResource('/executeafm/result/computationResult.json', ComputationResult)

        then:
        result.offset == [0]
        result.size == [4]
        result.overallSize == [4]

        result.data == [ "-12958511.8099999", "25315434.8199999", "-2748323.76", "-7252542.67"]

        result.dimensions.size() == 1
        result.dimensions[0].name == "x"
        result.dimensions[0].headers.size() == 3

        Header.AttributeHeader account = result.dimensions[0].headers[0] as Header.AttributeHeader
        account.name == "Account"
        account.uri == "/gdc/md/FoodMartDemo/obj/124"

        List<HeaderItem.AttributeHeaderItem> accountHeaderItems = account.items as List<HeaderItem.AttributeHeaderItem>
        accountHeaderItems[0].uri == "/gdc/md/FoodMartDemo/obj/124/elements?id=3200"
        accountHeaderItems[0].name == "Cost of Goods Sold"
        accountHeaderItems[1].uri == "/gdc/md/FoodMartDemo/obj/124/elements?id=6000"
        accountHeaderItems[1].name == "Salaries"

        Header.AttributeHeader accountType = result.dimensions[0].headers[1] as Header.AttributeHeader
        accountType.name == "Account Type"
        accountType.uri == "/gdc/md/FoodMartDemo/obj/113"

        List<HeaderItem.AttributeHeaderItem> accountTypeHeaderItems = accountType.items as List<HeaderItem.AttributeHeaderItem>
        accountTypeHeaderItems[0].uri == "/gdc/md/FoodMartDemo/obj/113/elements?id=14"
        accountTypeHeaderItems[0].name == "Income"
        accountTypeHeaderItems[1].uri == "/gdc/md/FoodMartDemo/obj/113/elements?id=15"
        accountTypeHeaderItems[1].name == "Expense"

        Header.MeasureGroupHeader measureGroupHeader = result.dimensions[0].headers[2] as Header.MeasureGroupHeader

        List<HeaderItem.MeasureHeaderItem> metricHeaderItems = measureGroupHeader.items as List<HeaderItem.MeasureHeaderItem>
        metricHeaderItems[0].uri == "0" // todo
        metricHeaderItems[0].name == "Accounting Amount [Sum]"
        metricHeaderItems[1].uri == "0" // todo
        metricHeaderItems[1].name == "Accounting Amount [Sum]"

    }

}
